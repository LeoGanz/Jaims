package jaims_development_studio.jaims.server.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.profile.InsufficientPermissionException;
import jaims_development_studio.jaims.api.profile.NoProfileAvailableException;
import jaims_development_studio.jaims.api.sendables.ERequestType;
import jaims_development_studio.jaims.api.sendables.ESendableType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableTypeException;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.sendables.SendableUUID;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.server.user.UserNotLoggedInException;

public class ClientConnection implements Runnable {
	
	private final Logger		LOG	= LoggerFactory.getLogger(ClientConnection.class);
	private final Socket		clientSocket;
	private final ClientManager	clientManager;
	private ObjectOutputStream	out;
	private ObjectInputStream	in;
	private int					connectionID;
	private User				user;
	private boolean				connectionTerminated;
	private boolean				autoSendSendables;
	
	public ClientConnection(Socket clientSocket, ClientManager clientManager) {
		this.clientSocket = clientSocket;
		this.clientManager = clientManager;
		connectionTerminated = false;

		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			UUID serverUUID = clientManager.getServer().getServerUUID();
			SendableUUID sendableServerUUID = new SendableUUID(serverUUID);
			manageSendSendable(sendableServerUUID);
		} catch (@SuppressWarnings("unused") SocketException e) {
			LOG.debug("Client disconnected without having sent anything!");
			terminate();
		} catch (@SuppressWarnings("unused") EOFException | StreamCorruptedException e) {
			LOG.debug("Caught EOFException. Could be caused by someone executing port checks or pinging the server.");
			terminate();
		} catch (IOException e) {
			LOG.warn("Couldn't open client connection " + connectionID, e);
			terminate();
		}
	}
	
	public void startSendableAutoSender() throws UserNotLoggedInException {
		
		if (user == null)
			throw new UserNotLoggedInException("SendableAutoSender can only be activated for logged in users!");
		
		autoSendSendables = true;

		Thread sendableSender = new Thread(() -> {
			while (autoSendSendables)
				synchronized (user) {
					try {
						while (user.noSendableQueued())
							user.wait();
						if (connectionTerminated)
							break;
						Sendable sendable = user.takeSendable();
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
			
			if (!connectionTerminated)
				LOG.debug("ClientConnection " + connectionID + " running");
			
			//HANDLE DATA RECEIVING
			Sendable sendable = null;
			while (!connectionTerminated)
				try {
					sendable = (Sendable) in.readObject();

					if (user != null)
						synchronized (user) {
							processReceivedSendable(sendable);
						}
					else
						processReceivedSendable(sendable);
					
				} catch (ClassCastException e) {
					LOG.warn("Recieved data that's no instance of Sendable from client connection " + connectionID, e);
					terminate();
				} catch (InvalidClassException e) {
					//TODO Inform client that it needs to update
					LOG.warn("Recieved data with improper serialVersionUID from client connection " + connectionID
							+ "\nClient needs to update!", e);
					terminate();
				} catch (@SuppressWarnings("unused") SocketException e) {
					terminate();
				} catch (IOException e) {
					//Client probably lost/terminated connection
					LOG.warn("IOEXception in ClientConnection-" + connectionID + ": ", e);
					terminate();
				}
			
		} catch (Exception e) {
			LOG.error("An unexpected exception occurred in client connection " + connectionID, e);
		} finally {
			terminate();
		}
	}
	
	private void processReceivedSendable(Sendable sendable) {
		
		LOG.debug("Received Sendable of type: " + sendable.getType());
		
		if (user == null)
			if (!((sendable.getType() == ESendableType.LOGIN) || (sendable.getType() == ESendableType.REGISTRATION))) {
				InvalidSendableTypeException invalidSendableTypeException = new InvalidSendableTypeException("No user is logged in!", sendable);
				error(invalidSendableTypeException);
				return;
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
			case REQUEST:
				manageReceiveRequest((SendableRequest) sendable);
				break;
			case DELETE_ACCOUNT:
				manageReceiveAccountDeletion();
				break;
			case PROFILE:
				manageReceiveProfile((SendableProfile) sendable);
				break;
			default:
				manageReceiveInvalidSendable(sendable);
				break;
		}
	}
	
	private void manageReceiveRegistration(SendableRegistration registration) {
		try {
			user = clientManager.registerNewUser(registration);
			
			startSendableAutoSender();
		} catch (NullPointerException | UserNameNotAvailableException e) {
			error(e);
		} catch (UserNotLoggedInException e) {
			LOG.warn("Could not start SendableAutoSender eventhough the user should be logged in by now", e);
		}
	}
	
	private void manageReceiveLogin(SendableLogin login) {
		try {
			user = clientManager.loginUser(login);
			
			startSendableAutoSender();
		} catch (NullPointerException | UserNotFoundException | IncorrectPasswordException e) {
			error(e);
		} catch (UserNotLoggedInException e) {
			LOG.warn("Could not start SendableAutoSender eventhough the user should be logged in by now", e);
		}
	}
	
	private void manageReceiveMessage(SendableMessage message) {
		try {
			message.setTimestampServerReceived();
			clientManager.deliverMessage(message);
			SendableMessageResponse messageResponse = message.buildMessageResponse();
			manageSendSendable(messageResponse);
		} catch (UserNotFoundException e) {
			error(e);
		}
	}

	private void manageReceiveRequest(SendableRequest request) {
		try {
			if (user != null) {
				switch (request.getRequestType()) {
					case DELETE_ACCOUNT:
						request.setUniversalUuid(user.getAccount().getUuid());
						user = null;
						break;
					case PROFILE:
						if (request.getUniversalUuid() == null)
							request.setUniversalUuid(user.getAccount().getUuid());
						break;
					default:
						clientManager.manageRequest(request);
				}

				clientManager.manageRequest(request);
			} else if (request.getRequestType() == ERequestType.DELETE_ACCOUNT)
				throw new UserNotFoundException("User not logged in!");
			else
				clientManager.manageRequest(request);
		} catch (InvalidSendableTypeException e) {
			error(e);
		} catch (UserNotFoundException e) {
			error(e);
		} catch (NoProfileAvailableException e) {
			error(e);
		}
	}

	@Deprecated
	private void manageReceiveAccountDeletion() {
		if (user != null) {
			clientManager.deleteUserAndAccount(user.getAccount().getUuid());
			user = null;
		} else {
			UserNotFoundException userNotFoundException = new UserNotFoundException("User not logged in!");
			error(userNotFoundException);
		}
	}

	private void manageReceiveProfile(SendableProfile profile) {
		if (user != null) {
			if (profile.getProfile() == null) {
				NullPointerException nullPointerException = new NullPointerException("SendableProfile contains no profile");
				error(nullPointerException);
				return;
			}
			
			if (!user.getAccount().getUuid().equals(profile.getProfile().getUuid())) {
				InsufficientPermissionException insufficientPermissionException = new InsufficientPermissionException("No permission to alter other user's profiles!");
				error(insufficientPermissionException);
				return;
			}
			
			if (profile.getProfile().getAccount() != null)
				profile.getProfile().setAccount(null);
			
			clientManager.manageReceiveProfile(profile);
		} else {
			UserNotFoundException userNotFoundException = new UserNotFoundException("User not logged in!");
			error(userNotFoundException);
		}
	}
	
	private void manageReceiveInvalidSendable(Sendable sendable) {
		LOG.warn("Recieved invalid data from client connection " + connectionID
				+ ((user != null) ? " (" + user.toString() + ")" : ""));
		
		InvalidSendableTypeException invalidSendableTypeException = new InvalidSendableTypeException(
				"Unknown sendable type!", sendable);
		error(invalidSendableTypeException);
	}

	private void error(Exception e) {
		SendableException sendableException = new SendableException(e);
		manageSendSendable(sendableException);
	}

	private void manageSendSendable(Sendable sendable) {
		LOG.debug("Sending sendable of type: " + sendable.getType());
		
		if (sendable.getType() == ESendableType.MESSAGE)
			((SendableMessage) sendable).setTimestampServerSent();
		else if (sendable.getType() == ESendableType.MESSAGE_RESPONSE) {
		} else if (sendable.getType() == ESendableType.EXCEPTION)
			LOG.debug("Sendable Exception: " + ((SendableException) sendable).getException().getClass()
					+ ": " + ((SendableException) sendable).getException().getMessage());
		else if (sendable.getTimestampSent() == null)
			sendable.setTimestampSent();
		
		try {
			out.writeObject(sendable);
			out.flush();
		} catch (@SuppressWarnings("unused") IOException e) {
			//Couldn't send sendable to client
			LOG.debug("Couldn't deliver sendable!");
			
			autoSendSendables = false;

			if (user != null)
				user.enqueueAsFirstElement(sendable);
			terminate();
		}
	}

	public synchronized void terminate() {
		if (connectionTerminated)
			return;
		
		try {

			connectionTerminated = true;
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			clientSocket.close();
			clientManager.connectionTerminated(this);
		} catch (IOException e) {
			LOG.error("An unexpected exception occurred while closing client connection" + connectionID, e);
		}
	}

	@Override
	protected void finalize() {
		terminate();
	}

	public User getUser() {
		return user;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
