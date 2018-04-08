package jaims_development_studio.jaims.client.logic;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.sound.sampled.Mixer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.ERequestType;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.database.ReadFromDatabase;
import jaims_development_studio.jaims.client.database.WriteToDatabase;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.JaimsFrame;
import jaims_development_studio.jaims.client.networking.ServerConnection;

public class ClientMain {

	private static final Logger	LOG						= LoggerFactory.getLogger(ClientMain.class);
	public static boolean		confirmationRecieved	= false;
	private final JPanel		userUILeftSide			= new JPanel();
	private ServerConnection	sc;
	private WriteToDatabase		wtd;
	private Thread				threadDatabaseManagement, threadPCC;
	private DatabaseConnection	databaseConnection;
	private JaimsFrame			jf;
	private GUIMain				guiMain;
	private String				loggedInUsername;
	private ReadFromDatabase	readFromDatabase;
	private Settings			settings;
	private Mixer				systemOutputMixer;
	private boolean				loggedIn				= false, addingNewContact = false;
	private boolean				profileChanged			= false;

	/**
	 * Static profile which represents the logged-in user.
	 */
	public SimpleContact		userContact;
	public static UUID			serverUUID;

	/**
	 * Main method of program which takes args as given start arguments and creates
	 * a new instance of <code>ClientMain</code>.
	 *
	 * @param args
	 *            given start arguments.
	 */
	public static void main(String[] args) {

		try {
			new ClientMain();
		} catch (Exception e) {
			LOG.error("Fatal error!", e);
		}
	}

	/*
	 * Constructor of class <code>ClientMain</code>. Calls a method which
	 * initialises the program.
	 */
	public ClientMain() {

		initProgram(true);
	}

	/**
	 * Begins to initialise the program by doing the following:
	 * <ul>
	 * <li>initialising a new <code>JaimsFrame</code></li>
	 * <li>opening up a new <code>DatabaseConnection</code></li>
	 * <li>starting a new Thread which tries to connect to the server</li>
	 * <li>initialising a new <code>ServerConnection</code></li>
	 * <li>adding a new <code>LoginPanel</code> to the JFrame</li>
	 * </ul>
	 */
	private void initProgram(boolean showSplashScreen) {

		LOG.info("Starting...");

		Thread thread = new Thread(sc = new ServerConnection(this));
		thread.start();

		SwingUtilities.invokeLater(guiMain = new GUIMain(this, showSplashScreen));

	}

	public void connectToDatabase() {

		if (databaseConnection == null)
			databaseConnection = new DatabaseConnection(loggedInUsername);
	}

	public ArrayList<SimpleContact> getSimpleContacts() {

		return databaseConnection.getSimpleContacts();
	}

	public ArrayList<SimpleContact> getSimpleChatContacts() {

		return databaseConnection.getSimpleChatContacts();
	}

	public ArrayList<Message> getMessageList(UUID uuid) {

		return databaseConnection.getContactMessages(uuid);
	}

	/**
	 *
	 * @return a WriteToDatabase object
	 */
	public WriteToDatabase getWTD() {

		return wtd;
	}

	public ServerConnection getServerConnection() {

		return sc;
	}

	public GUIMain getGUIMain() {

		return guiMain;
	}

	public void sendLogin(String username, String password) {

		loggedInUsername = username;
		SendableLogin sl = new SendableLogin(username, password);
		sc.sendSendable(sl);
	}

	public UUID getUserUUID() {

		return userContact.getContactID();
	}

	public void loginSuccesful(UUID uuid) {

		setUserContact(uuid);
		loadSettings();
		guiMain.loginSuccessful();
	}

	public Settings getSettings() {

		return settings;
	}

	public void setUserContact(UUID uuid) {

		connectToDatabase();
		userContact = databaseConnection.getUserContact(uuid, loggedInUsername, this);
	}

	public SimpleContact getUserContact() {

		return userContact;
	}

	public String getUsername() {

		return loggedInUsername;
	}

	public Image getProfileImage(UUID uuid) {

		return databaseConnection.getProfileImage(uuid);
	}

	public Image getUserProfileImage(UUID uuid) {

		return databaseConnection.getUserProfileImage(uuid);
	}

	public Image getChatBackground() {

		return databaseConnection.getChatBackground();
	}

	public String getContactStatus(UUID uuid) {

		return databaseConnection.getContactStatus(uuid);
	}

	public boolean hasEntry(UUID uuid) {

		return databaseConnection.hasEntry(uuid);
	}

	public boolean isServerConnected() {

		return sc.isServerConnected();
	}

	public void setLoginEnabled(boolean enabled) {

		guiMain.setLoginEnabled(enabled);
	}

	public void setWrongUsername(boolean wrong) {

		guiMain.setWrongUsername(wrong);
	}

	public void setWrongPassword(boolean wrong) {

		guiMain.setWrongPassword(wrong);
	}

	public boolean saveProfile(Profile p) {

		if (p.getUuid().equals(userContact.getContactID()))
			return databaseConnection.saveProfile(p, false);
		else
			return databaseConnection.saveProfile(p, true);
	}

	public void updateProfile(Profile p) {

		if (p.getUuid().equals(userContact.getContactID()))
			databaseConnection.updateProfile(p, false);
		else
			databaseConnection.updateProfile(p, true);
	}

	public void requestUserProfile(UUID uuid) {

		SendableRequest sr = new SendableRequest(ERequestType.PROFILE, uuid);
		sc.sendSendable(sr);
	}

	public void registerNewUser(String username, String password, String email) {

		SendableRegistration sr = new SendableRegistration(username, password, email);
		sc.sendSendable(sr);
	}

	public void setLoggedIn(boolean loggedIn) {

		this.loggedIn = loggedIn;
	}

	public void succesfullRegistration(UUID uuid) {

		sendRegistrationProfile(uuid);
		guiMain.succesfulRegistration();
	}

	public void sendRegistrationProfile(UUID uuid) {

		Profile pf = new Profile(null, guiMain.getRegisteredUsername(), "", "", null, new Date());
		pf.setUuid(uuid);
		System.out.println(pf.getNickname());
		System.out.println(pf.getUuid());
		SendableProfile sp = new SendableProfile(pf);
		sc.sendSendable(sp);
	}

	public void saveTextMessage(jaims_development_studio.jaims.api.message.TextMessage m) {

		databaseConnection.saveTextMessage(m);
	}

	public void saveVoiceMessage(jaims_development_studio.jaims.api.message.VoiceMessage v, String pathToFile) {

		databaseConnection.saveVoiceMessage(v, pathToFile);
	}

	public void addMessageToChat(jaims_development_studio.jaims.api.message.Message m, EMessageType messageType) {

		guiMain.addMessageToChat(m, messageType);
	}

	public void updateHasChat(boolean hasChat, UUID contactID) {

		databaseConnection.updateHasChat(hasChat, contactID);
	}

	public void setAddingNewContact(boolean b) {

		addingNewContact = b;
	}

	public boolean isAddingNewContact() {

		return addingNewContact;
	}

	public boolean deleteProfile(UUID uuid) {

		return databaseConnection.deleteProfile(uuid);
	}

	public void showAvailableUsersForAdding(Profile... users) {

		guiMain.showAvailableUsersForAdding(users);
	}

	public String getUserPath() {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String path = userHome + "/Jaims/" + loggedInUsername + "/";

		return path;
	}

	private void loadSettings() {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String filename = userHome + "/Jaims/" + loggedInUsername + "/settings/settings.set";
		String path = userHome + "/Jaims/" + loggedInUsername;

		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = new FileInputStream(filename);
			ois = new ObjectInputStream(fin);
			settings = (Settings) ois.readObject();

		} catch (FileNotFoundException fnfe) {
			settings = new Settings();
			createDirectory(path);
			File settingFile = new File(filename);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingFile))) {

				oos.writeObject(settings);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			settings = new Settings();
			createDirectory(path);
			File settingFile = new File(filename);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingFile))) {

				oos.writeObject(settings);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {

			if (fin != null)
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
	}

	public void saveSettings() {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String filename = userHome + "/Jaims/" + loggedInUsername + "/settings/settings.set";

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			oos.writeObject(settings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createDirectory(String path) {

		File f = new File(path);
		if (!f.exists())
			f.mkdirs();

		File settings = new File(path + "/settings");
		if (!settings.exists())
			settings.mkdirs();
	}

	public void sendSendable(Sendable s) {

		sc.sendSendable(s);
	}

	public void setOutputMixer(Mixer mixer) {

		systemOutputMixer = mixer;
	}

	public void doLogout() {

		LOG.info("Initiating logout");

		if (profileChanged) {
			SendableProfile sp = new SendableProfile(
					databaseConnection.getAndUpdateProfile(userContact.getContactID()));
			sc.sendSendable(sp);
		}

		databaseConnection.closeConnection();
		databaseConnection = null;
		sc.disconnect();
		guiMain.closeGUI();
		guiMain = null;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			LOG.error("Interrupted sleep");
		}

		initProgram(false);

	}
}