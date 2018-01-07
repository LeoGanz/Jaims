package jaims_development_studio.jaims.client.networking;

import java.awt.Cursor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableUUID;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ListenForInput implements Runnable {

	private static final Logger	LOG	= LoggerFactory.getLogger(ListenForInput.class);

	Socket						so;
	ObjectInputStream			ois;
	Sendable					s;
	ClientMain					cm;

	/**
	 * Constructor of this class. Initialises only the files, reading the socket
	 * input has to be started by running a thread.
	 *
	 * @param so
	 *            Socket which represents the connection to the database
	 * @param cm
	 *            Object representing ClientMain class
	 */
	public ListenForInput(Socket so, ClientMain cm) {

		this.so = so;
		this.cm = cm;
	}

	/**
	 * Creates a new ObjectInputStream by using the socket's input stream. Then
	 * tries to read a new <code>Sendable</code> Object from the input stream. When
	 * a sendable is successfully read the method {@link #handleSendable()
	 * handleSendable}.
	 */
	private void readConnection() {

		try {
			ois = new ObjectInputStream(so.getInputStream());
			while (so.isConnected()) {
				try {
					s = (Sendable) ois.readObject();
					handleSendable();
				} catch (ClassNotFoundException e) {
					LOG.error("Class was not found", e);
				} catch (NullPointerException npe) {
					LOG.error("Socket isn't initialised", npe);
				} catch (SocketException se) {
					LOG.error("Socket isn't connected", se);
				} catch (IOException ie) {
					LOG.error("IO Exception", ie);
					break;
				}

			}
		} catch (NullPointerException npe) {
			LOG.error("Socket isn't initialised!", npe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Socket isn't connected", e);
		}

	}

	/**
	 * Gets the Type of the <code>Sendable</code> and casts it to the explicit
	 * sendable.
	 */
	private void handleSendable() {

		try {
			switch (s.getType().getValue()) {
			case "MESSAGE_RESPONSE":
				SendableMessageResponse smr = (SendableMessageResponse) s;
				LOG.info("Received sendable of type " + smr.getType().toString());
				break;
			case "EXCEPTION":
				SendableException se = (SendableException) s;
				LOG.info("Received Sendable of type " + se.getType().toString());
				LOG.error(se.getException().getMessage());
				cm.getJaimsFrame().getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				break;
			case "STORED_UUID":
				SendableUUID su = (SendableUUID) s;
				LOG.info("Received Sendable of type " + su.getType().toString());
				ClientMain.userProfile.setUUID(su.getUuid());
				break;
			case "PROFILE":
				break;
			case "CONFIRMATION":
				SendableConfirmation sc = (SendableConfirmation) s;
				LOG.info("Received Sendable of type " + sc.getConfirmationType().toString());
				if (sc.getConfirmationType().getValue().equals("REGISTRATION_SUCCESSFUL")) {
					if (cm.getLoginPanel().getRegistrationWindow() != null) {
						cm.getLoginPanel().getRegistrationWindow().dispose();
					}
				} else if (sc.getConfirmationType().getValue().equals("LOGIN_SUCCESSFUL")) {
					cm.startCreatingChatWindow(cm.getLoginPanel().getUsername());
				}
				break;
			case "OTHER":
				break;
			default:
				break;

			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

	}

	@Override
	public void run() {

		readConnection();

	}

}
