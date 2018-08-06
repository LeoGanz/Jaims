package jaims_development_studio.jaims.client.directFileExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import jaims_development_studio.jaims.client.directFileExchange.directFileExchangeSendables.DFESendable;
import jaims_development_studio.jaims.client.directFileExchange.directFileExchangeSendables.FileInformation;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class DirectFileExchangeServer {

	private int					port;
	private ServerSocket		server;
	private Socket				client;
	private ObjectInputStream	ois;
	private ClientMain			cm;

	public DirectFileExchangeServer(int port, ClientMain cm) {

		this.port = port;
		this.cm = cm;
		initServer();
		listenForInput();
	}

	private void initServer() {

		try {
			server = new ServerSocket(port);
			client = server.accept();
			ois = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void listenForInput() {

		new Thread() {
			@Override
			public void run() {

				try {
					DFESendable s = (DFESendable) ois.readObject();
					handleSendable(s);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	private synchronized void handleSendable(DFESendable s) {

		switch (s.getType().getValue()) {
		case EFileExchangeType.Values.TYPE_FILEINFO:
			receiveFiles((FileInformation) s);
		default:
		}
	}

	private void receiveFiles(FileInformation fi) {

		for (int i = 0; i < fi.getNumberOfFiles(); i++) {
			DFEObject dfeo = new DFEObject(fi.getUUIDAtPosition(i), fi.getSender(), fi.getRecipient(),
					fi.getFilenameAtPosition(i), null, fi.getExtensionAtPosition(i), fi.getTypeAtPosition(i),
					new Date(System.currentTimeMillis()));
		}

	}

}
