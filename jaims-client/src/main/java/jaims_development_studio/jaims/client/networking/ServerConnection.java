package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;

//import serverConnection.ListenForInput;

public class ServerConnection implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(ServerConnection.class);
	private static Socket server;
	private static ObjectOutputStream oos;
	private static Thread threadListenForInput;

	@Override
	public void run() {
		//initConnection();

	}

	public static void initConnection(DatabaseManagement dm) {
		try {
			//opens up a connection to the server
			server = new Socket("localhost", 6000);
		} catch (IOException e) {
			LOG.error("Couldn't connect to server", e);
		}
		
		//starts a thread which is only there for listening to inputs from the server
		threadListenForInput = new Thread(new ListenForInput(server, dm));
		threadListenForInput.start();
	}

	public void disconnect() {
		try {
			server.close();
		} catch (IOException e) {
			LOG.error("Failed to close server!", e);
		}
	}

	public static void sendSendable() {
		try {
			oos = new ObjectOutputStream(server.getOutputStream());
			//oos.writeObject();
			oos.flush();
		} catch (IOException e) {
			LOG.error("Failed to open ObjectOutputStream!",e);
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					LOG.error("Failed to close ObjectOutputStream!", e);
				}
		}
	}

}
