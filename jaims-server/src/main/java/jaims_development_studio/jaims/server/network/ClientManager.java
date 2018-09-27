package jaims_development_studio.jaims.server.network;

import java.net.Socket;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;

import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.message.MessageAlreadyExistsException;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableDirectDelivery;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.user.UserNotOnlineException;
import jaims_development_studio.jaims.api.util.NoEntityAvailableException;
import jaims_development_studio.jaims.server.ITickable;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.DirectDeliveryManager;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 * The ClientManager maintains all {@link ClientConnection}s on the {@link Server}. All connections are provided with
 * IDs. It provides access to every connection on the server.
 *
 * @author WilliGross
 */
public class ClientManager implements ITickable {
	
	private final Logger									LOG							= LoggerFactory.getLogger(ClientManager.class);
	private final Server									server;
	private final ObservableMap<Integer, ClientConnection>	connections;
	private final BiMap<Integer, ClientConnection>			connectionsUnderlyingBiMap	= HashBiMap.create();
	private final BiMap<Integer, UUID>						connectionIdUserUUIDMap		= HashBiMap.create();
	private int												nextConnectionID			= 0;
	private int												nrConnections				= 0;
	private final UserManager								userManager;
	private final DirectDeliveryManager						directDeliveryManager;
	private boolean											terminatingAll;
	
	public ClientManager(Server server) {
		this.server = server;
		connections = FXCollections.synchronizedObservableMap(FXCollections.observableMap(connectionsUnderlyingBiMap));
		userManager = new UserManager(server);
		directDeliveryManager = new DirectDeliveryManager(this);
		server.subscribeToTicker(this);
	}
	
	/**
	 * Establishes a new {@link ClientConnection} with the provided {@link Socket}. This method starts a new thread for
	 * every connection.
	 *
	 * @param clientSocket socket to build a ClientConnection with
	 */
	public void newConnection(Socket clientSocket) {
		ClientConnection clientConnection = new ClientConnection(clientSocket, this);
		clientConnection.setConnectionID(nextConnectionID++);
		
		connections.put(clientConnection.getConnectionID(), clientConnection);
		Thread thread = new Thread(clientConnection, "ClientConnection-" + clientConnection.getConnectionID());
		thread.start();
		
		nrConnections++;
	}
	
	/**
	 * Called when a {@link ClientConnection} is terminated. This is needed to maintain the list of connected clients
	 * and save all data for the {@link User}.
	 *
	 * @param clientConnection the connetion that has (been) terminated
	 */
	public void connectionTerminated(ClientConnection clientConnection) {
		if (!terminatingAll) {
			connections.remove(clientConnection.getConnectionID());
			connectionIdUserUUIDMap.remove(clientConnection.getConnectionID());
		}
		nrConnections--;
		userManager.save(clientConnection.getUser());
		userManager.logoutUser(clientConnection.getUser());
	}
	
	/**
	 * Instructs all connections on the {@link Server} to terminate.
	 */
	public void terminateAllConnections() {
		terminatingAll = true;
		connections.keySet().forEach(c -> connections.get(c).terminate());
		terminatingAll = false;
		// TODO Handle connections not being removed from collection
	}
	
	public Server getServer() {
		return server;
	}
	
	public ClientConnection getConnection(int connectionID) {
		return connections.get(connectionID);
	}
	
	public ClientConnection getConnection(UUID userUuid) {
		int connectionID = connectionIdUserUUIDMap.inverse().get(userUuid);
		return getConnection(connectionID);
	}
	
	/**
	 * @return immutable set with all connections
	 */
	public Set<ClientConnection> getConnections() {
		return ImmutableSet.copyOf(connectionsUnderlyingBiMap.inverse().keySet());
	}
	
	public int getNrConnections() {
		return nrConnections;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	
	/**
	 * Shuts down the manager and gives the order to terminate all connections.
	 */
	public void shutdown() {
		LOG.info("Disconnecting all clients");
		terminateAllConnections();
	}
	
	@Override
	public void tick() {
		// TODO save batches of users from connections list
		// userManager.save(user batch);
	}
	
	/**
	 * Used to get notified of updates to the connection list. Every time a new client connects or a connections is
	 * terminated an update is fired.
	 *
	 * @param listener the observer that will be notified of changes
	 */
	public void subscribeToConnectionListUpdates(MapChangeListener<Integer, ClientConnection> listener) {
		connections.addListener(listener);
	}
	
	/**
	 * Used to no longer get updates from the connection list.
	 * 
	 * @param listener the observer that shall no longer receive notifications
	 */
	public void unsubscribeFromConnectionListUpdates(MapChangeListener<Integer, ClientConnection> listener) {
		connections.removeListener(listener);
	}
	
	/*
	 * Method forwarding
	 */
	
	public User registerNewUser(SendableRegistration registration, int connectionID) throws UserNameNotAvailableException {
		User user = userManager.registerNewUser(registration);
		
		connectionIdUserUUIDMap.put(connectionID, user.getUuid());
		
		SendableConfirmation sendableConfirmation = new SendableConfirmation(EConfirmationType.REGISTRATION_SUCCESSFUL, user.getAccount().getUuid());
		user.enqueueSendable(sendableConfirmation);
		
		return user;
	}
	
	public User loginUser(SendableLogin login, int connectionID) throws UserNotFoundException, IncorrectPasswordException {
		User user = userManager.loginUser(login);
		
		connectionIdUserUUIDMap.put(connectionID, user.getUuid());
		
		SendableConfirmation sendableConfirmation = new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL, user.getAccount().getUuid());
		user.enqueueSendable(sendableConfirmation);
		
		return user;
	}
	
	public void deleteUserAndAccount(UUID uuid) {
		userManager.deleteUserAndAccount(uuid);
	}
	
	public void manageRequest(SendableRequest request, UUID requester) throws InvalidSendableException, NoEntityAvailableException {
		userManager.manageRequest(request, requester);
	}
	
	public void manageReceiveProfile(SendableProfile profile) {
		userManager.manageReceiveProfile(profile);
	}
	
	public void deliverMessage(SendableMessage message) throws UserNotFoundException, InvalidSendableException, MessageAlreadyExistsException {
		userManager.deliverMessage(message);
	}
	
	public void manageResponse(SendableMessageResponse messageResponse) throws InvalidSendableException, UserNotFoundException {
		userManager.manageResponse(messageResponse);
	}
	
	public void deliverDirectDelivery(SendableDirectDelivery delivery) throws InvalidSendableException, UserNotFoundException, UserNotOnlineException {
		directDeliveryManager.deliver(delivery);
	}
	
}
