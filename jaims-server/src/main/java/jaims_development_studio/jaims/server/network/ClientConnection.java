package jaims_development_studio.jaims.server.network;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.account.IncorrectPasswordException;
import jaims_development_studio.jaims.server.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.network.sendables.Sendable;
import jaims_development_studio.jaims.server.network.sendables.SendableException;
import jaims_development_studio.jaims.server.network.sendables.SendableLogin;
import jaims_development_studio.jaims.server.network.sendables.SendableMessage;
import jaims_development_studio.jaims.server.network.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.server.network.sendables.SendableRegistration;
import jaims_development_studio.jaims.server.network.sendables.ESendableType;
import jaims_development_studio.jaims.server.network.sendables.SendableUUID;
import jaims_development_studio.jaims.server.user.User;
import jaims_development_studio.jaims.server.user.UserNotFoundException;

public class ClientConnection implements Runnable {

	private final Logger		LOG	= LoggerFactory.getLogger(ClientConnection.class);
	private final Socket		clientSocket;
	private final ClientManager	clientManager;
	private ObjectOutputStream	out;
	private ObjectInputStream	in;
	private int					connectionID;
	private User				user;
	private boolean				connectionTerminated;

	public ClientConnection(Socket clientSocket, ClientManager clientManager) {
		this.clientSocket = clientSocket;
		this.clientManager = clientManager;

		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			LOG.error("Couldn't open client connection " + connectionID, e);
			terminate();
		}

		final Thread sendableSender = new Thread(() -> {
			while (true)
				synchronized (user) {
					try {
						while (user.noSendableQueued())
							user.wait();

						if (connectionTerminated)
							break;

						Sendable sendable = user.getSendable();
						
						manageSendSendable(sendable);

					} catch (@SuppressWarnings("unused") InterruptedException e) {
						
					}
				}
		});
		sendableSender.setDaemon(true);
		sendableSender.start();
	}

	@Override
	public void run() {
		try {

			//HANDLE DATA RECIEVING
			Sendable sendable = null;
			while (true)
				try {
					sendable = (Sendable) in.readObject();

					if (user == null)
						if (!((sendable.getType() == ESendableType.LOGIN) || (sendable.getType() == ESendableType.REGISTRATION))) {
							InvalidSendableTypeException invalidSendableTypeException = new InvalidSendableTypeException("No user is logged in!", sendable);
							SendableException sendableException = new SendableException(invalidSendableTypeException);
							manageSendSendable(sendableException);
							continue;
						}
					
					switch (sendable.getType()) {
						case REGISTRATION:
							manageReceiveRegistration((SendableRegistration) sendable);
							break;
						case LOGIN:
							manageReceiveLogin((SendableLogin) sendable);
							break;
						case MESSAGE:
							manageReceiveMessage((SendableMessage) sendable);
							break;
						case DELETE_ACCOUNT:
							manageReceiveAccountDeletion();
							break;
						default:
							manageReceiveInvalidSendable(sendable);
							break;
					}

				} catch (ClassCastException e) {
					LOG.warn("Recieved data that's no instance of Sendable from client connection " + connectionID, e);
				} catch (InvalidClassException e) {
					//TODO Inform client that it needs to update
					LOG.warn("Recieved data with improper serialVersionUID from client connection " + connectionID
							+ "\nClient needs to update!", e);
				} catch (@SuppressWarnings("unused") IOException e) {
					//Client probably lost/terminated connection
					break;
				}

		} catch (Exception e) {
			LOG.error("An unexpected exception occurred in client connection " + connectionID, e);
		} finally {
			terminate();
		}
	}

	private void manageReceiveRegistration(SendableRegistration registration) {
		try {
			user = clientManager.registerNewUser(registration);

			final SendableUUID sendableUUID = new SendableUUID(user.getAccount().getUuid());
			manageSendSendable(sendableUUID);
		} catch (NullPointerException | UserNameNotAvailableException e) {
			SendableException sendableException = new SendableException(e);
			manageSendSendable(sendableException);
		}
	}

	private void manageReceiveLogin(SendableLogin login) {
		try {
			user = clientManager.loginUser(login);
		} catch (NullPointerException | UserNotFoundException | IncorrectPasswordException e) {
			SendableException sendableException = new SendableException(e);
			manageSendSendable(sendableException);
		}
	}

	private void manageReceiveMessage(SendableMessage message) {
		try {
			message.setTimestampServerReceived();
			clientManager.deliverMessage(message);
			SendableMessageResponse messageResponse = message.buildMessageResponse();
			manageSendSendable(messageResponse);
		} catch (UserNotFoundException e) {
			SendableException sendableException = new SendableException(e);
			manageSendSendable(sendableException);
		}
	}
	
	private void manageReceiveAccountDeletion() {
		if (user != null) {
			clientManager.deleteUserAndAccount(user.getAccount().getUuid());
			user = null;
		} else {
			UserNotFoundException userNotFoundException = new UserNotFoundException("User not logged in!");
			SendableException sendableException = new SendableException(userNotFoundException);
			manageSendSendable(sendableException);
		}
	}

	private void manageReceiveInvalidSendable(Sendable sendable) {
		LOG.warn("Recieved invalid data from client connection " + connectionID
				+ ((user != null) ? " (" + user.toString() + ")" : ""));

		InvalidSendableTypeException invalidSendableTypeException = new InvalidSendableTypeException(
				"Unknown sendable type!", sendable);
		SendableException sendableException = new SendableException(invalidSendableTypeException);
		manageSendSendable(sendableException);
	}

	private void manageSendSendable(Sendable sendable) {
		if (sendable.getType() == ESendableType.MESSAGE)
			((SendableMessage) sendable).setTimestampServerSent();
		else if (sendable.getType() == ESendableType.MESSAGE_RESPONSE) {
		} else if (sendable.getTimestampSent() == null)
			sendable.setTimestampSent();

		try {
			out.writeObject(sendable);
		} catch (@SuppressWarnings("unused") IOException e) {
			//Couldn't send sendable to client
			terminate();
			if (user != null)
				user.enqueueAsFirstElement(sendable);
		}
	}

	public synchronized void terminate() {
		if (connectionTerminated)
			return;

		try {
			clientSocket.close();
			clientManager.connectionTerminated(this);
			connectionTerminated = true;
		} catch (IOException e) {
			LOG.error("An unexpected exception occurred while closing client connection" + connectionID, e);
		}
	}

	@Override
	protected void finalize() {
		terminate();
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
