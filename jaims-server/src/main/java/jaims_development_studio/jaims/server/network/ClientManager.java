package jaims_development_studio.jaims.server.network;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.ITickable;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.user.UserManager;

public class ClientManager implements ITickable {
	
	private final Logger					LOG					= LoggerFactory.getLogger(ClientManager.class);
	private final List<ClientConnection>	connections;
	private int								nextConnectionID	= 0;
	private int								nrConnections		= 0;
	private final UserManager				userManager;
	
	public ClientManager(Server server) {
		connections = new LinkedList<>();
		userManager = UserManager.load(server);
		server.subscribeToTicker(this);
	}
	
	public void newConnection(Socket clientSocket) {
		ClientConnection clientConnection = new ClientConnection(clientSocket, this);
		clientConnection.setConnectionID(nextConnectionID++);
		
		Thread thread = new Thread(clientConnection, "ClientConnection-" + clientConnection.getConnectionID());
		thread.start();
		
		nrConnections++;
	}
	
	public void connectionTerminated(ClientConnection clientConnection) {
		connections.remove(clientConnection);
		nrConnections--;
		//save user data because it won't be accessed for some time? Or just have the UserManager do the saving?
	}
	
	public void terminateConnection(ClientConnection clientConnection) {
		clientConnection.terminate();
		connectionTerminated(clientConnection);
	}
	
	public void terminateAllConnections() {
		connections.forEach(c -> terminateConnection(c));
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
		userManager.saveAll();
	}
	
	@Override
	public void tick() {
		userManager.save();
	}
	
}
