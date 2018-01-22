package jaims_development_studio.jaims.server.network;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.profile.NoProfileAvailableException;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableTypeException;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.sendables.SendableUUID;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.util.ObservableList;
import jaims_development_studio.jaims.server.ITickable;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.user.UserManager;

public class ClientManager implements ITickable {

	private final Logger					LOG					= LoggerFactory.getLogger(ClientManager.class);
	private final Server					server;
	private final List<ClientConnection>	connections;
	private int								nextConnectionID	= 0;
	private int								nrConnections		= 0;
	private final UserManager				userManager;
	private boolean							terminatingAll;

	public ClientManager(Server server) {
		this.server = server;
		connections = new ObservableList<>(new LinkedList<>());
		userManager = new UserManager(server);
		server.subscribeToTicker(this);
	}

	public void newConnection(Socket clientSocket) {
		ClientConnection clientConnection = new ClientConnection(clientSocket, this);
		clientConnection.setConnectionID(nextConnectionID++);

		connections.add(clientConnection);
		Thread thread = new Thread(clientConnection, "ClientConnection-" + clientConnection.getConnectionID());
		thread.start();

		nrConnections++;
	}

	public void connectionTerminated(ClientConnection clientConnection) {
		if (!terminatingAll)
			connections.remove(clientConnection);
		nrConnections--;
		userManager.save(clientConnection.getUser());
		userManager.logoutUser(clientConnection.getUser());
	}

	public void terminateAllConnections() {
		terminatingAll = true;
		connections.forEach(c -> c.terminate());
		terminatingAll = false;
		// TODO Handle connections not being removed from collection
	}

	public Server getServer() {
		return server;
	}
	
	public List<ClientConnection> getConnections() {
		return connections;
	}

	public int getNrConnections() {
		return nrConnections;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void shutdown() {
		LOG.info("Disconnecting all clients");
		terminateAllConnections();
	}

	@Override
	public void tick() {
		// TODO save batches of users from connections list
		// userManager.save(user batch);
	}

	// Method forwarding

	public User registerNewUser(SendableRegistration registration) throws UserNameNotAvailableException {
		User user = userManager.registerNewUser(registration);

		SendableConfirmation sendableConfirmation = new SendableConfirmation(EConfirmationType.REGISTRATION_SUCCESSFUL);
		user.enqueueSendable(sendableConfirmation);

		SendableUUID sendableUUID = new SendableUUID(user.getAccount().getUuid());
		user.enqueueSendable(sendableUUID);

		return user;
	}

	public User loginUser(SendableLogin login) throws UserNotFoundException, IncorrectPasswordException {
		User user = userManager.loginUser(login);

		SendableConfirmation sendableConfirmation = new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL);
		user.enqueueSendable(sendableConfirmation);

		SendableUUID sendableUUID = new SendableUUID(user.getAccount().getUuid());
		user.enqueueSendable(sendableUUID);
		return user;
	}

	public void deleteUserAndAccount(UUID uuid) {
		userManager.deleteUserAndAccount(uuid);
	}

	public void manageRequest(SendableRequest request) throws InvalidSendableTypeException, NoProfileAvailableException {
		userManager.manageRequest(request);
	}
	
	public void manageReceiveProfile(SendableProfile profile) {
		userManager.manageReceiveProfile(profile);
	}
	
	public void deliverMessage(SendableMessage message) throws UserNotFoundException {
		userManager.deliverMessage(message);
	}


}
