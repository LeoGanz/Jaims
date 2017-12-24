package jaims_development_studio.jaims.server.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.Server;

public class NetworkSystem implements Runnable, Closeable {

	private final Logger	LOG	= LoggerFactory.getLogger(NetworkSystem.class);
	private final Server	server;
	private final int		port;
	private ServerSocket	serverSocket;
	private ClientManager	clientManager;

	public NetworkSystem(Server server) {
		this.server = server;
		port = server.getServerPort();
		if (port <= 0)
			throw new IllegalArgumentException("Port can't be a negative number!");

		try {
			serverSocket = new ServerSocket(port);
			clientManager = new ClientManager(server);

			Thread thread = new Thread(this, "Network System");
			thread.setDaemon(true);
			thread.start();

		} catch (@SuppressWarnings("unused") IOException e) {
			LOG.warn("FAILED TO BIND TO PORT!");
			LOG.warn("Perhaps a server is already running on that port?");
			server.initiateShutdown();
		}

	}

	@SuppressWarnings("resource")
	@Override
	public void run() {

		LOG.info("Network system initialized");

		Socket clientSocket = null;

		while (server.isServerRunning()) {
			try {
				clientSocket = serverSocket.accept();
				LOG.debug("clientSocket accepted");
			} catch (@SuppressWarnings("unused") SocketException e) {
				LOG.info("Shutting down network system");
				break;
			} catch (IOException e) {
				LOG.error("An unexpected IOException occurred while accepting clients", e);
			}

			clientManager.newConnection(clientSocket);
		}

	}

	@Override
	public void close() {
		try {
			if (serverSocket != null)
				serverSocket.close();
			if (clientManager != null)
				clientManager.shutdown();
		} catch (IOException e) {
			LOG.error("An unexpected IOException occurred while closing NetworkSystem", e);
		}
	}

	public ClientManager getClientManager() {
		return clientManager;
	}

}
