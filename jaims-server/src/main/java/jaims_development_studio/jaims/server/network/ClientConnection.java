package jaims_development_studio.jaims.server.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
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
import jaims_development_studio.jaims.api.message.MessageAlreadyExistsException;
import jaims_development_studio.jaims.api.profile.InsufficientPermissionException;
import jaims_development_studio.jaims.api.sendables.ESendableType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableDirectDelivery;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableFriendRequestResponse;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.sendables.SendableUUID;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.user.UserNotOnlineException;
import jaims_development_studio.jaims.api.util.NoEntityAvailableException;
import jaims_development_studio.jaims.server.user.UserNotLoggedInException;

/**
 * A ClientConnection represents a client on the server side. It is responsible for managing the socket and any in or
 * outgoing traffic. Therefore all communication with a client is done via a ClientConnection. For every new connection
 * a fresh instance of ClientConnection is created. Every ClientConnection runs as its own {@link Thread} to be able to
 * receive incoming data. All ClientConnections are created and managed via the {@link ClientManager}.
 *
 * @author WilliGross
 */
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
	
	/**
	 * Builds a new ClientConnection. Basic 'infrastructure' like input and output streams is set up. Furthermore the
	 * client is notified of the server's current UUID.
	 *
	 * @param clientSocket the socket a ClientConnection should be built with
	 * @param clientManager the server's ClientManager that maintains all ClientConnections
	 */
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
	
	/**
	 * This starts a separate {@link Thread} the only job of which is to send all {@link Sendable}s that are queued by
	 * the {@link User} to the client. When no Sendable is queued the Thread waits.
	 *
	 * @throws UserNotLoggedInException if no user is logged in for this connection as sending Sendables requires access
	 *             to the User object
	 */
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
		}, "SendableAutoSender-" + connectionID);
		sendableSender.setDaemon(true);
		sendableSender.start();
	}
	
	/**
	 * The main loop that keeps a ClientConnection alive. It reads from the connection's {@link InputStream} and
	 * processes received data. Furthermore it handles different exceptions and properly terminates the connection if it
	 * is instructed to do so.
	 */
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
				} catch (@SuppressWarnings("unused") IOException e) {
					//Client probably lost/terminated connection
					LOG.info("ClientConnection-" + connectionID + " disconnected!");
					terminate();
				}
			
		} catch (Exception e) {
			LOG.error("An unexpected exception occurred in client connection " + connectionID, e);
		} finally {
			terminate();
		}
	}
	
	/**
	 * All received {@link Sendable}s are forwarded to this method to be processed. It sorts them by their type and
	 * takes actions accordingly.
	 *
	 * @param sendable the data to process
	 */
	private void processReceivedSendable(Sendable sendable) {
		
		LOG.debug("Received Sendable of type: " + sendable.getType());
		
		if (user == null)
			if (!((sendable.getType() == ESendableType.LOGIN) || (sendable.getType() == ESendableType.REGISTRATION))) {
				InvalidSendableException invalidSendableException = new InvalidSendableException("No user is logged in!", sendable);
				error(invalidSendableException);
				return;
			}

		try {
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
				case MESSAGE_RESPONSE:
					manageReceiveResponse((SendableMessageResponse) sendable);
					break;
				case FRIEND_REQUEST_RESPONSE:
					manageReceiveResponse((SendableFriendRequestResponse) sendable);
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
				case DIRECT_DELIVERY:
					manageReceiveDirectDelivery((SendableDirectDelivery) sendable);
					break;
				default:
					manageReceiveInvalidSendable(sendable);
					break;
			}
		} catch (ClassCastException e) {
			LOG.warn("Caught ClassCastException while managing sendables", e);
			manageReceiveInvalidSendable(sendable);
		}
	}
	
	/*
	 * The following are methods for further processing received Sendables. Most actions are passed on to the
	 * ClientManager.
	 */
	
	private void manageReceiveRegistration(SendableRegistration registration) {
		try {
			user = clientManager.registerNewUser(registration, connectionID);
			
			startSendableAutoSender();
		} catch (NullPointerException | UserNameNotAvailableException e) {
			error(e);
		} catch (UserNotLoggedInException e) {
			LOG.warn("Could not start SendableAutoSender eventhough the user should be logged in by now", e);
		}
	}
	
	private void manageReceiveLogin(SendableLogin login) {
		try {
			user = clientManager.loginUser(login, connectionID);
			
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
		} catch (UserNotFoundException | InvalidSendableException | MessageAlreadyExistsException e) {
			error(e);
		}
	}
	
	private void manageReceiveResponse(SendableMessageResponse messageResponse) {
		try {
			clientManager.manageResponse(messageResponse);
		} catch (InvalidSendableException | UserNotFoundException e) {
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
					case SETTINGS:
					case CONTACTS:
						request.setUniversalUuid(user.getAccount().getUuid());
						break;
					case PROFILE:
						if ((request.getUniversalUuid() == null) && (request.getUniversalString() == null))
							request.setUniversalUuid(user.getAccount().getUuid());
						break;
					default:
						break;
				}

				clientManager.manageRequest(request, user.getAccount().getUuid());
			} else
				throw new UserNotFoundException("User not logged in!");
		} catch (InvalidSendableException e) {
			error(e);
		} catch (UserNotFoundException e) {
			error(e);
		} catch (NoEntityAvailableException e) {
			error(e);
		}
	}

	/**
	 * @deprecated use {@link SendableRequest} instead
	 */
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
			
			try {
				clientManager.manageReceiveProfile(profile);
			} catch (IllegalArgumentException e) {
				error(e);
			}
		} else {
			UserNotFoundException userNotFoundException = new UserNotFoundException("User not logged in!");
			error(userNotFoundException);
		}
	}
	
	private void manageReceiveDirectDelivery(SendableDirectDelivery delivery) {
		if (user != null)
			try {
				clientManager.deliverDirectDelivery(delivery);
			} catch (UserNotOnlineException | UserNotFoundException | InvalidSendableException e) {
				error(e);
			}
		else {
			UserNotFoundException userNotFoundException = new UserNotFoundException("User not logged in!");
			error(userNotFoundException);
		}
	}

	private void manageReceiveInvalidSendable(Sendable sendable) {
		LOG.warn("Recieved invalid data from client connection " + connectionID
				+ ((user != null) ? " (" + user.toString() + ")" : ""));
		
		InvalidSendableException invalidSendableTypeException = new InvalidSendableException(
				"Unknown sendable type! Make sure you use the latest API version.", sendable);
		error(invalidSendableTypeException);
	}

	/**
	 * Helper method for delivering exceptions to the client
	 *
	 * @param e the exception to send to the client
	 */
	private void error(Exception e) {
		SendableException sendableException = new SendableException(e);
		manageSendSendable(sendableException);
	}

	/**
	 * This method sends data to the client via the connection's {@link ObjectOutputStream}
	 *
	 * @param sendable the data to send
	 */
	public synchronized void manageSendSendable(Sendable sendable) {
		LOG.debug("Sending sendable of type: " + sendable.getType());
		
		if (sendable.getType() == ESendableType.MESSAGE)
			((SendableMessage) sendable).setTimestampServerSent();
		else if (sendable.getType() == ESendableType.MESSAGE_RESPONSE) {
		} else if (sendable.getType() == ESendableType.EXCEPTION)
			LOG.debug("Sendable Exception: " + ((SendableException) sendable).getException().getClass()
					+ ": " + ((SendableException) sendable).getException().getMessage());

		if ((sendable.getTimestampSent() == null) && (sendable.getType() != ESendableType.MESSAGE_RESPONSE))
			sendable.setTimestampSent();
		
		try {
			out.writeObject(sendable);
			out.flush();
		} catch (@SuppressWarnings("unused") IOException e) {
			//Couldn't send sendable to client
			LOG.debug("Couldn't deliver sendable!");
			
			autoSendSendables = false;

			if (user != null)
				user.enqueueSendable(sendable);
			terminate();
		}
	}

	/**
	 * Instructs the connection to terminate and closes existing data streams.
	 */
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

	/**
	 * Automatic termination when object is deleted from cash by the GC. Just in case.
	 */
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
