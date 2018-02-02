package jaims_development_studio.jaims.client.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableUUID;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ListenForInput implements Runnable {

	private static final Logger	LOG				= LoggerFactory.getLogger(ListenForInput.class);

	Socket						so;
	ObjectInputStream			ois;
	ClientMain					cm;
	boolean						firstSendable	= true;

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
					handleSendable((Sendable) ois.readObject());
				} catch (ClassNotFoundException e) {
					LOG.error("Class was not found", e);
				} catch (NullPointerException npe) {
					LOG.error("Socket isn't initialised", npe);
				} catch (SocketException se) {
					LOG.error("Socket isn't connected", se);
				} catch (EOFException e) {
					LOG.error("Server connection closed. Trying to reconnect", e);
					firstSendable = true;
					cm.getServerConnection().initConnection();
					break;
				} catch (IOException ie) {
					LOG.error("IO Exception", ie);
				}

			}
		} catch (NullPointerException npe) {
			LOG.error("Socket isn't initialised!", npe);
		} catch (EOFException e) {
			LOG.error("Server connection closed. Trying to reconnect", e);
			cm.getServerConnection().initConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Socket isn't connected", e);
		}

	}

	/**
	 * Gets the Type of the {@link Sendable} and casts it to the explicit sendable.
	 */
	private void handleSendable(Sendable s) {

		try {
			switch (s.getType().getValue()) {
			case "MESSAGE":
				SendableMessage sm = (SendableMessage) s;
				switch (sm.getMessageType().getValue()) {
				case "TEXT":
					LOG.info("Received sendable of type " + sm.getType().toString());
					break;
				case "IMAGE":

				case "VOICE":

				case "FILE":

				case "LOCATION":

				case "OTHER":

				}
				break;
			case "MESSAGE_RESPONSE":
				SendableMessageResponse smr = (SendableMessageResponse) s;
				LOG.info("Received sendable of type " + smr.getType().toString());
				break;
			case "EXCEPTION":
				SendableException se = (SendableException) s;
				LOG.info("Received Sendable of type " + se.getType().toString());
				LOG.error(se.getException().getMessage());
				break;
			case "STORED_UUID":
				SendableUUID su = (SendableUUID) s;
				LOG.info("Received Sendable of type " + su.getType().toString());
				if (firstSendable) {
					ClientMain.serverUUID = su.getStoredUuid();
					firstSendable = false;
				} else
					cm.setUserContact(su.getStoredUuid());

				break;
			case "PROFILE":
				System.out.println("Got profile");
				SendableProfile sp = (SendableProfile) s;
				System.out.println(sp.getProfile().getNickname());
				break;
			case "CONFIRMATION":
				SendableConfirmation sc = (SendableConfirmation) s;
				LOG.info("Received Sendable of type " + sc.getConfirmationType().toString());
				if (sc.getConfirmationType().getValue().equals("REGISTRATION_SUCCESSFUL")) {
				} else if (sc.getConfirmationType().getValue().equals("LOGIN_SUCCESSFUL")) {
					cm.loginSuccesful();
				}
				break;
			case "OTHER":
				break;
			default:
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		readConnection();

	}

}
