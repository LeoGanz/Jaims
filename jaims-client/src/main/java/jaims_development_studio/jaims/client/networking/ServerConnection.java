package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ServerConnection implements Runnable {

	private static final Logger			LOG	= LoggerFactory.getLogger(ServerConnection.class);
	private static Socket				server;
	private static ObjectOutputStream	oos;
	private ClientMain					cm;

	/**
	 * Constructor of this class. Initialises only fields, Connection has to be
	 * started by running a thread
	 *
	 * @param cm
	 *            Object representing the ClientMain class
	 */
	public ServerConnection(ClientMain cm) {
		this.cm = cm;
	}

	/**
	 * Initialises the connection to the server and then listens for input from the
	 * server
	 */
	@Override
	public void run() {
		initConnection();

		Thread thread = new Thread(new ListenForInput(server, cm));
		thread.start();
	}

	/**
	 * Opens a connection to the server and creates a new ObjectOutputStream with
	 * the server's output stream
	 */
	public static void initConnection() {
		try {
			// opens up a connection to the server
			server = new Socket();
			server.connect(new InetSocketAddress("188.194.21.33", 6000), 2000);
			while (server.isConnected() == false) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					LOG.info("Thread couldn't sleep", e);
				}
			}
			oos = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {
			LOG.error("Couldn't connect to server", e);
		}
	}

	/**
	 * Closes the connection to the server
	 */
	public void disconnect() {
		try {
			server.close();
			LOG.info("Closed socket");
		} catch (IOException e) {
			LOG.error("Failed to close server!", e);
		}
	}

	/**
	 * Sends a <code>Sendable</code> to the server.
	 *
	 * @param s
	 *            the sendable to be sent
	 */
	public static void sendSendable(Sendable s) {
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			LOG.error("Failed to open ObjectOutputStream!", e);
		} catch (NullPointerException npe) {
			LOG.error("Socket isn't initialised", npe);
		}

	}

	/**
	 * @return returns the socket
	 */
	public Socket getSocket() {
		return server;
	}

	/**
	 * Gets a new socket and sets it as the global socket
	 *
	 * @param s
	 *            new socket which is connected
	 */
	public void setSocket(Socket s) {
		server = s;
		try {
			oos = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {
			LOG.error("Error when trying to create ObjectOutputStream", e);
		}
	}

}
