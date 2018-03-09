package jaims_development_studio.jaims.client.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.TextMessage;
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
					if (so.isClosed())
						break;
					else {
						cm.getServerConnection().initConnection();
						break;
					}
				} catch (EOFException e) {
					LOG.error("Server connection closed. Trying to reconnect");
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
			LOG.error("Server connection closed. Trying to reconnect");
			if (so.isClosed() == false)
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
				LOG.info("Recieved sendable of type " + sm.getType());
				if (sm.getMessageType().equals(EMessageType.TEXT)) {
					TextMessage tm = (TextMessage) sm.getMessage();
					tm.setTimestampDelivered();
					LOG.info("Cast to " + tm.getMessageType());
					cm.saveTextMessage(tm);
				} else if (sm.getMessageType().equals(EMessageType.IMAGE)) {

				} else if (sm.getMessageType().equals(EMessageType.VOICE)) {

				} else if (sm.getMessageType().equals(EMessageType.FILE)) {

				} else if (sm.getMessageType().equals(EMessageType.LOCATION)) {

				} else if (sm.getMessageType().equals(EMessageType.OTHER)) {

				}
				cm.addMessageToChat(sm.getMessage(), sm.getMessageType());
				break;
			case "MESSAGE_RESPONSE":
				SendableMessageResponse smr = (SendableMessageResponse) s;
				LOG.info("Received sendable of type " + smr.getType().toString());
				break;
			case "EXCEPTION":
				SendableException se = (SendableException) s;
				LOG.info("Received Sendable of type " + se.getType().toString());
				LOG.error("There goes the exception... ", se.getException());
				if (se.getException() instanceof IncorrectPasswordException) {
					cm.setWrongPassword(true);
					cm.setWrongUsername(false);
				}
				if (se.getException() instanceof UserNameNotAvailableException) {
					cm.setWrongUsername(true);
				}
				break;
			case "STORED_UUID":
				SendableUUID su = (SendableUUID) s;
				LOG.info("Received Sendable of type " + su.getType().toString());
				if (firstSendable) {
					ClientMain.serverUUID = su.getStoredUuid();
					firstSendable = false;
				}

				break;
			case "PROFILE":
				SendableProfile sp = (SendableProfile) s;
				if (cm.isAddingNewContact()) {
					cm.showAvailableUsersForAdding(sp.getProfile());
				} else {
					if (cm.hasEntry(sp.getProfile().getUuid()) == false)
						cm.saveProfile(sp.getProfile());
					else
						cm.updateProfile(sp.getProfile());
				}
				break;
			case "CONFIRMATION":
				SendableConfirmation sc = (SendableConfirmation) s;
				LOG.info("Received Sendable of type " + sc.getConfirmationType().toString());
				if (sc.getConfirmationType().getValue().equals("REGISTRATION_SUCCESSFUL")) {
					cm.succesfullRegistration(sc.getStoredUuid());
				} else if (sc.getConfirmationType().getValue().equals("LOGIN_SUCCESSFUL")) {
					cm.loginSuccesful(sc.getStoredUuid());
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

	public void closeSocket() {

		try {
			so.close();
			LOG.info("Socket closed, no longer listening for input");
		} catch (IOException e) {
			closeSocket();
		}
	}

	@Override
	public void run() {

		readConnection();

	}

}
