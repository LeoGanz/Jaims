package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;

//import serverConnection.ListenForInput;

public class ServerConnection implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(ServerConnection.class);
	private static Socket server;
	private static ObjectOutputStream oos;

	@Override
	public void run() {
		initConnection();
		
		Thread thread = new Thread(new ListenForInput(server));
		thread.start();
	}

	public static void initConnection() {
		try {
			//opens up a connection to the server
			server = new Socket("188.194.21.33", 6000);
			oos = new ObjectOutputStream(server.getOutputStream());
		} catch (IOException e) {
			LOG.error("Couldn't connect to server", e);
		}
	}

	public void disconnect() {
		try {
			server.close();
			LOG.info("Closed socket");
		} catch (IOException e) {
			LOG.error("Failed to close server!", e);
		}
	}

	public static void sendSendable(Sendable s) {
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			LOG.error("Failed to open ObjectOutputStream!",e);
		} finally {
		}
		
	}
	
	public Socket getSocket() {
		return server;
	}

}
