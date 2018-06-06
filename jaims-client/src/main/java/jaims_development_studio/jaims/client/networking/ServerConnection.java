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

/**
 * This class handles the connection to the server. That means connecting on the
 * program's startup, reconnecting to the server if the connection was closed
 * and notifying the client if the connection throws an exception.
 * 
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class ServerConnection implements Runnable {

	private static final Logger			LOG			= LoggerFactory.getLogger(ServerConnection.class);
	private static Socket				server;
	private static ObjectOutputStream	oos;
	private ClientMain					cm;
	private InetSocketAddress			is;
	private int							duration	= 500;
	private boolean						connected	= false;
	private ListenForInput				lfi;

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
			server.connect(is = new InetSocketAddress(/* "188.192.194.35" */ "localhost" /* "188.193.157.228" */, 6000),
					500);
			while (server.isConnected() == false) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					LOG.info("Thread couldn't sleep", e);
				}
			}
			connected = true;
			oos = new ObjectOutputStream(server.getOutputStream());
			Thread thread2 = new Thread(lfi = new ListenForInput(server, cm), "Thread-ListenForInput");
			thread2.start();
		} catch (IOException e) {
			/**
			 * IOException occurs when there is no open connection to the server. If that
			 * happens a new thread is started which will check after increasing time
			 * periods have passed (shortest: 500ms, longest: 60s) if a connection is
			 * available and when there is one a new connection will be created.
			 */
			LOG.error("Couldn't connect to server. Will keep on trying!");
			connected = false;
			cm.setLoginEnabled(false);
			Thread thread = new Thread("Thread-CheckForServerConnection") {
				@Override
				public void run() {

					try {
						while (!checkIfServerIsAvailable()) {
							Thread.sleep(duration);
							if (duration <= 60000)
								duration += 0.25 * duration;
							else
								duration = 60000;
						}
						connected = true;
						oos = new ObjectOutputStream(server.getOutputStream());

						LOG.info("Connected");
						Thread thread2 = new Thread(lfi = new ListenForInput(server, cm), "Thread-ListenForInput");
						thread2.start();
						cm.setLoginEnabled(true);
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
	 * Closes the connection to the server. If a IOError occurs method is called
	 * again.
	 */
	public void disconnect() {

		try {
			server.close();
			LOG.info("Closed socket");
			lfi.closeSocket();
		} catch (IOException e) {
			LOG.error("Failed to close socket!", e);
			disconnect();
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
			s.setTimestampSent();
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			LOG.error("Failed to open ObjectOutputStream!", e);
		} catch (NullPointerException npe) {
			LOG.error("Socket isn't initialised", npe);
		}

	}

	/**
	 * @return returns the socket which holds the connection to the server.
	 * 
	 * @see Socket
	 */
	public Socket getSocket() {

		return server;
	}

	/**
	 * Gets a new socket and sets it as the global socket
	 *
	 * @param s
	 *            new socket which is connected
	 * 
	 * @see Socket
	 */
	public void setSocket(Socket s) {

		server = s;
		try {
			oos = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {
			LOG.error("Error when trying to create ObjectOutputStream", e);
		}
	}

	public boolean isServerConnected() {

		return connected;
	}

	/**
	 * Checks if a connection to the server is available and returns the result.
	 * 
	 * @return boolean whether a connection is available
	 */
	private boolean checkIfServerIsAvailable() {

		server = new Socket();
		try {
			server.connect(is, 100);
		} catch (IOException e) {
		}
		return server.isConnected();
	}

}
