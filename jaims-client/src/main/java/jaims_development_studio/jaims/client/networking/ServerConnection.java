package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ServerConnection implements Runnable {

	private static final Logger			LOG			= LoggerFactory.getLogger(ServerConnection.class);
	private static Socket				server;
	private static ObjectOutputStream	oos;
	private ClientMain					cm;
	private InetSocketAddress			is;
	private int							duration	= 500;
	private boolean						connected	= false;

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

	}

	/**
	 * Opens a connection to the server and creates a new ObjectOutputStream with
	 * the server's output stream
	 */
	public void initConnection() {

		try {
			// opens up a connection to the server
			server = new Socket();
			server.connect(is = new InetSocketAddress(/* "188.194.21.33" */ "localhost", 6000), 500);
			while (server.isConnected() == false) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					LOG.info("Thread couldn't sleep", e);
				}
			}
			oos = new ObjectOutputStream(server.getOutputStream());
			Thread thread2 = new Thread(new ListenForInput(server, cm));
			thread2.start();
		} catch (IOException e) {
			LOG.error("Couldn't connect to server. Will keep on trying!", e);
			Thread thread = new Thread() {
				@Override
				public void run() {

					try {
						while (!checkIfServerIsAvailable()) {
							Thread.sleep(duration);
							if (duration <= 120000)
								duration += 0.25 * duration;
							else
								duration = 120000;
						}
						oos = new ObjectOutputStream(server.getOutputStream());

						LOG.info("Connected");
						Thread thread2 = new Thread(new ListenForInput(server, cm));
						thread2.start();
					} catch (UnknownHostException e) {
						LOG.error("Unkown Host Name", e);
					} catch (IOException e) {
						LOG.error("Input / Output exception", e);
					} catch (InterruptedException e) {
						LOG.error("Interrupted Sleep", e);
					}

				}
			};
			thread.start();
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
	public void sendSendable(Sendable s) {

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

	public boolean checkIfServerIsAvailable() {

		server = new Socket();
		try {
			server.connect(is, 100);
		} catch (IOException e) {
		}
		return server.isConnected();
	}

}
