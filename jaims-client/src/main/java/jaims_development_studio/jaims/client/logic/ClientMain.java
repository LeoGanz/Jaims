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
import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.directFileExchange.DFEManager;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.dfe.DFEObject;
import jaims_development_studio.jaims.client.networking.ServerConnection;

/**
 * ClientMain is, as the name suggests, the entry-point for the program. It
 * furthermore is the gathering place and caretaker for everything concerning
 * the logic behind the Client. This class also handles, as the one half of the
 * logic - gui inteface, every action between logic and gui.
 * 
 * @author Bu88le
 *
 * @since v0.1.0
 */
public class ClientMain {

	private static final Logger	LOG						= LoggerFactory.getLogger(ClientMain.class);

	public static boolean		confirmationRecieved	= false;
	private ServerConnection	sc;
	private DatabaseConnection	databaseConnection;
	private GUIMain				guiMain;
	private String				loggedInUsername;
	private Settings			settings;
	private boolean				addingNewContact		= false;
	private boolean				profileChanged			= false;
	private DFEManager			dfeManager;

	public SimpleContact		userContact;
	public static UUID			serverUUID;

	/**
	 * Main entry point for program. Creates new Instance of ClientMain.
	 * 
	 * @param args String array with arguments for the program
	 */
	public static void main(String[] args) {

		try {
			new ClientMain();
		} catch (Exception e) {
			LOG.error("Fatal error!", e);
		}
	}

	/**
	 * Constructor of class <code>ClientMain</code>. Calls
	 * <code>initProgram()</code> which initialises the program.
	 */
	public ClientMain() {

		initProgram(true);
	}

	/**
	 * Initialises the program by:
	 * <ul>
	 * <li>Starting the connection to the server in a separate
	 * <code>{@link Thread}</code></li>
	 * <li>Creating a new <code>GUIMain</code> instance for handling the client's
	 * gui</li>
	 * </ul>
	 * 
	 * @param showSplashScreen a boolean deciding whether the splash screen should
	 * be shown when starting the program
	 * @see GUIMain
	 */
	private void initProgram(boolean showSplashScreen) {

		new Thread(sc = new ServerConnection(this), "Thread-ServerConnection").start();
		;

		SwingUtilities.invokeLater(guiMain = new GUIMain(this, showSplashScreen));
	}

	/**
	 * Method checks if the client already has a connection to the database. <br>
	 * When it isn't connected a new connection will be started.
	 * 
	 * @see DatabaseConnection
	 */
	private void connectToDatabase() {

		if (databaseConnection == null)
			databaseConnection = new DatabaseConnection(loggedInUsername);
	}

	public String[] getLogin() {

		// return databaseConnection.getLogin();
		return new String[] {};
	}

	public void setRememberMe(boolean remember, String username, String password) {

		databaseConnection.setRememberMe(remember, username, password);
	}

	/**
	 * Delegate method which only calls <code>DatabaseConnection</code>'s method
	 * <code>getSimpleContacts()</code> and immediately returns an
	 * <code>ArrayList</code> filled with all for this user available contacts.
	 * 
	 * @return An ArrayList filled with <code>SimpleContact</code>'s
	 * 
	 * @see DatabaseConnection#getSimpleContacts()
	 * @see ArrayList
	 */
	public ArrayList<SimpleContact> getSimpleContacts() {

		return databaseConnection.getSimpleContacts();
	}

	/**
	 * Delegate method which only calls <code>DatabaseConnection</code>'s method
	 * <code>getSimpleChatContacts</code> and immediately returns an
	 * <code>ArrayList</code> filled with all contacts that have an existing chat.
	 * 
	 * @return An ArrayList filled with <code>SimpleContact</code>s
	 * 
	 * @see DatabaseConnection#getSimpleChatContacts()
	 * @see ArrayList
	 */
	public ArrayList<SimpleContact> getSimpleChatContacts() {

		return databaseConnection.getSimpleChatContacts();
	}

	/**
	 * Delegate method which only calls <code>DatabaseConnection</code>'s method
	 * <code>getMessageList()</code> passing on the {@link UUID} argument. <br>
	 * Immediately returns an <code>ArrayList</code> filled with all
	 * {@link ClientInternMessage}s that have the given <code>UUID</code> as either
	 * recipient or sender.
	 * 
	 * @param uuid specifies the contact for whose messages the client is looking
	 * @return an ArrayList filled with <code>ClientInternMessage</code>s to or from
	 * this contact
	 * 
	 * @see DatabaseConnection#getContactMessages(UUID)
	 * @see ArrayList
	 */
	public ArrayList<ClientInternMessage> getMessageList(UUID uuid) {

		return databaseConnection.getContactMessages(uuid);
	}

	/**
	 * Immediately returns the <code>ServerConnection</code> object.
	 * 
	 * @return the ServerConnection object
	 * 
	 * @see ServerConnection
	 */
	public ServerConnection getServerConnection() {

		return sc;
	}

	/**
	 * Immediately returns the <code>GUIMain</code> object
	 * 
	 * @return the GUIMain object
	 * 
	 * @see GUIMain
	 */
	public GUIMain getGUIMain() {

		return guiMain;
	}

	/**
	 * This method gets the username and password the user enters when logging in as
	 * Strings. <br>
	 * It then builds a <code>SendableLogin</code> which is sent to the server to
	 * check whether the login information is correct.
	 * 
	 * 
	 * @param username the username the user enters when logging in
	 * @param password the password the user enters when logging in
	 * 
	 * @see ServerConnection#sendSendable(Sendable)
	 */
	public void sendLogin(String username, String password) {

		loggedInUsername = username;
		SendableLogin sl = new SendableLogin(username, password);
		sc.sendSendable(sl);
	}

	/**
	 * This method immediately return the logged-in-user's {@link UUID} to the
	 * caller.
	 * 
	 * @return the logged-in-user's UUID
	 * 
	 * @see SimpleContact#getContactID()
	 */
	public UUID getUserUUID() {

		return userContact.getContactID();
	}

	/**
	 * This delegate method calls three other methods after a successful login:
	 * <ul>
	 * <li>{@link #setUserContact(UUID)}</li>
	 * <li>{@link #loadSettings()}</li>
	 * <li>{@link GUIMain#loginSuccessful()}</li>
	 * </ul>
	 * 
	 * @param uuid the logged-in-user's UUID
	 * 
	 * @see GUIMain
	 */
	public void loginSuccessful(UUID uuid) {

		setUserContact(uuid);
		loadSettings();
		guiMain.loginSuccessful();
	}

	/**
	 * This method immediately returns the {@link Settings} object to the caller.
	 * 
	 * @return the Settings object
	 */
	public Settings getSettings() {

		return settings;
	}

	/**
	 * When called this method starts the connection to the database and then loads
	 * the logged-in-user's <code>SimpleContact</code> from the database.
	 * 
	 * @param uuid the logged-in-user's UUID
	 * 
	 * @see #connectToDatabase()
	 * @see DatabaseConnection#getUserContact(UUID, String, ClientMain)
	 * @see UUID
	 */
	public void setUserContact(UUID uuid) {

		connectToDatabase();
		userContact = databaseConnection.getUserContact(uuid, loggedInUsername, this);
	}

	/**
	 * Returns the logged-in-user's <SimpleContact>
	 * 
	 * @return the logged-in-user's SimpleContact
	 * 
	 * @see SimpleContact
	 */
	public SimpleContact getUserContact() {

		return userContact;
	}

	/**
	 * Returns the logged-in-user's username.
	 * 
	 * @return the logged-in-user's username
	 * 
	 * @see String
	 */
	public String getUsername() {

		return loggedInUsername;
	}

	/**
	 * Returns the profile picture available in the database from the user
	 * represented by the {@link UUID}.
	 * 
	 * @param uuid the contact's UUID whose profile picture the caller is looking
	 * for
	 * @return the contact's profile picture
	 * 
	 * @see DatabaseConnection#getProfileImage(UUID)
	 * @see Image
	 */
	public Image getProfileImage(UUID uuid) {

		return databaseConnection.getProfileImage(uuid);
	}

	/**
	 * Returns the profile picture in the database for the logged-in-user
	 * represented by his {@link UUID}.
	 * 
	 * @param uuid the logged-in-user's UUID
	 * @return the logged-in-user's profile picture
	 * 
	 * @see DatabaseConnection#getUserProfileImage(UUID)
	 * @see Image
	 */
	public Image getUserProfileImage(UUID uuid) {

		return databaseConnection.getUserProfileImage(uuid);
	}

	/**
	 * Returns the chat background picture the user has selected for its chats
	 * 
	 * @return the background picture for chats
	 * 
	 * @see DatabaseConnection#getChatBackground()
	 * @see Image
	 */
	public Image getChatBackground() {

		return databaseConnection.getChatBackground();
	}

	/**
	 * Returns the status of a contact represented by the {@link UUID} as a String
	 * 
	 * @param uuid the contact's UUID whose status the caller is looking for
	 * @return the contact's status
	 * 
	 * @see DatabaseConnection#getContactStatus(UUID)
	 * @see String
	 */
	public String getContactStatus(UUID uuid) {

		return databaseConnection.getContactStatus(uuid);
	}

	/**
	 * Returns whether the contact represented by the {@link UUID} is saved in the
	 * database
	 * 
	 * @param uuid the contact's UUID the caller is searching for
	 * @return boolean whether the contact is saved
	 * 
	 * @see DatabaseConnection#hasEntry(UUID)
	 */
	public boolean hasEntry(UUID uuid) {

		return databaseConnection.hasEntry(uuid);
	}

	/**
	 * Returns whether the client is connected to the server
	 * 
	 * @return boolean whether the client is connected to the server
	 * 
	 * @see ServerConnection#isServerConnected()
	 */
	public boolean isServerConnected() {

		return sc.isServerConnected();
	}

	/**
	 * Sets the login button enabled/disabled
	 * 
	 * @param enabled whether de login button should be enabled or disabled
	 * 
	 * @see GUIMain#setLoginEnabled(boolean)
	 */
	public void setLoginEnabled(boolean enabled) {

		guiMain.setLoginEnabled(enabled);
	}

	/**
	 * Notifies the user when the login failed because of a wrong username.
	 * 
	 * @param wrong is true when the username is wrong
	 * 
	 * @see GUIMain#setWrongUsername(boolean)
	 */
	public void setWrongUsername(boolean wrong) {

		guiMain.setWrongUsername(wrong);
	}

	/**
	 * Notifies the user when the login failes because of a wrong password.
	 * 
	 * @param wrong is true when the password is wrong
	 * 
	 * @see GUIMain#setWrongPassword(boolean)
	 */
	public void setWrongPassword(boolean wrong) {

		guiMain.setWrongPassword(wrong);
	}

	/**
	 * Saves a given {@link Profile} to the database and decides on the basis of the
	 * profile's UUID whether to save it as a contact's profile or as the
	 * logged-in-user's profile.
	 * 
	 * @param p the profile that has to be saved
	 * @return true if the profile has successfully been saved otherwise false
	 * 
	 * @see DatabaseConnection#saveProfile(Profile, boolean)
	 */
	public boolean saveProfile(Profile p) {

		if (p.getUuid().equals(userContact.getContactID()))
			return databaseConnection.saveProfile(p, false);
		else
			return databaseConnection.saveProfile(p, true);
	}

	/**
	 * Updates the given {@link Profile}'s counterpart in the database and decides
	 * on the basis of the profile's UUID whether to update the logged-in-user's
	 * profile or a contact one's.
	 * 
	 * @param p the profile that has to be updated
	 * 
	 * @see DatabaseConnection#updateProfile(Profile, boolean)
	 */
	public void updateProfile(Profile p) {

		if (p.getUuid().equals(userContact.getContactID()))
			databaseConnection.updateProfile(p, false);
		else
			databaseConnection.updateProfile(p, true);
	}

	/**
	 * Sends a request for a profile with the given {@link UUID} to the server.
	 * 
	 * @param uuid the profile's UUID that has to be requested
	 * 
	 * @see SendableRequest
	 * @see ServerConnection#sendSendable(Sendable)
	 */
	public void requestUserProfile(UUID uuid) {

		SendableRequest sr = new SendableRequest(ERequestType.PROFILE, uuid);
		sc.sendSendable(sr);
	}

	/**
	 * After the user has entered his sign-up informations, these information are
	 * sent to the server in order to register a new user.
	 * 
	 * @param username the new user's username
	 * @param password the new user's password
	 * @param email the new user's email
	 */
	public void registerNewUser(String username, String password, String email) {

		SendableRegistration sr = new SendableRegistration(username, password, email);
		sc.sendSendable(sr);
	}

	/**
	 * When the server sends a registration confirmation this method is called and
	 * calls two further methods:
	 * <ul>
	 * <li>{@link ClientMain#sendRegistrationProfile(UUID)}</li>
	 * <li>{@link GUIMain#succesfulRegistration()}</li>
	 * </ul>
	 * 
	 * @param uuid the UUID of the newly registered user
	 */
	public void succesfullRegistration(UUID uuid) {

		sendRegistrationProfile(uuid);
		guiMain.succesfulRegistration();
	}

	/**
	 * This method creates a {@link Profile} for the new user after a successful
	 * registration and sends it to the server.
	 * 
	 * @param uuid the user's UUID for the profile
	 * 
	 * @see Profile#setUuid(UUID)
	 * @see ServerConnection#sendSendable(Sendable)
	 * @see UUID
	 * 
	 */
	public void sendRegistrationProfile(UUID uuid) {

		Profile pf = new Profile(null, guiMain.getRegisteredUsername(), "", "", "", loggedInUsername, null, 0,
				new Date());
		pf.setUuid(uuid);
		SendableProfile sp = new SendableProfile(pf);
		sc.sendSendable(sp);
	}

	/**
	 * Saves a {@link jaims_development_studio.jaims.api.message.TextMessage
	 * TextMessage} which was sent from the server to the database.
	 * 
	 * @param m the TextMessage to be saved
	 */
	public void saveTextMessage(jaims_development_studio.jaims.api.message.TextMessage m) {

		databaseConnection.saveTextMessage(m);
	}

	/**
	 * Saves a {@link jaims_development_studio.jaims.api.message.VoiceMessage
	 * VoiceMessage} which was sent from the server to the database.
	 * 
	 * @param v The <code>VoiceMessage</code> to be saved
	 * @param pathToFile Path to the voice message on the disk
	 */
	public void saveVoiceMessage(jaims_development_studio.jaims.api.message.VoiceMessage v, String pathToFile) {

		databaseConnection.saveVoiceMessage(v, pathToFile);
	}

	/**
	 * Adds a {@link jaims_development_studio.jaims.api.message.Message
	 * ClientInternMessage} which was sent from the server to a chat.
	 * 
	 * @param m The message to be added
	 * @param messageType The type of the message
	 * 
	 * @see EMessageType
	 * @see GUIMain#addMessageToChat(jaims_development_studio.jaims.api.message.Message,
	 * EMessageType) addMessageToChat(ClientInternMessage, EMessageType)
	 * 
	 */
	public void addMessageToChat(jaims_development_studio.jaims.api.message.Message m, EMessageType messageType) {

		guiMain.addMessageToChat(m, messageType);
	}

	/**
	 * Updates a contact specified by the <code>UUID</code> in the database and
	 * changes its hasChat field to the given value.
	 * 
	 * @param hasChat The value to be written into the database
	 * @param contactID The UUID of the contact whose hasChat value has to be
	 * changed
	 * 
	 * @see DatabaseConnection#updateHasChat(boolean, UUID)
	 */
	public void updateHasChat(boolean hasChat, UUID contactID) {

		databaseConnection.updateHasChat(hasChat, contactID);
	}

	/**
	 * Indicates the program that the user is adding new contacts which means that
	 * profiles that the server send during this time aren't just saved to the
	 * database but are shown on the <code>PanelAddUser</code>.
	 * 
	 * @param b true if adding new contacts otherwise false
	 */
	public void setAddingNewContact(boolean b) {

		addingNewContact = b;
	}

	/**
	 * 
	 * @return returns whether the user is adding new contacts
	 */
	public boolean isAddingNewContact() {

		return addingNewContact;
	}

	/**
	 * Calls a method to delete the profile belonging to this <code>UUID</code> from
	 * the database and returns a boolean whether it was successfully deleted.
	 * 
	 * @param uuid the UUID of the profile that has to be deleted
	 * @return true if profile was successfully deleted otherwise false
	 * 
	 * @see DatabaseConnection#deleteProfile(UUID)
	 * @see Profile
	 */
	public boolean deleteProfile(UUID uuid) {

		return databaseConnection.deleteProfile(uuid);
	}

	public void createDFEManager() {

		if (dfeManager != null)
			dfeManager = new DFEManager(this);
	}

	/**
	 * Gets a profile array the server has sent and forwards it to
	 * <code>GUIMain</code> to show these profiles on the <code>PanelAddUser</code>.
	 * 
	 * @param users the array of profiles to be displayed
	 * 
	 * @see GUIMain#showAvailableUsersForAdding(Profile...)
	 */
	public void showAvailableUsersForAdding(Profile... users) {

		guiMain.showAvailableUsersForAdding(users);
	}

	public ParentPanel getCurrentParentPanel() {

		return guiMain.getCurrentParentPanel();
	}

	/**
	 * 
	 * @return the path to the current user's folder on the disk as a String
	 */
	public String getUserPath() {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String path = userHome + "/Jaims/" + loggedInUsername + "/";

		return path;
	}

	/**
	 * Loads the user's settings saved on the hard drive into the settings object.
	 * <br>
	 * When the file cannot be loaded a new settings object is initialized and then
	 * immediately written to the hard drive.
	 * 
	 * @see Settings
	 */
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
				LOG.error("Failed to write file to disk", e);
			}
		} finally {

			if (fin != null)
				try {
					fin.close();
				} catch (IOException e) {
					LOG.error("FileInputStream couldn't be closed", e);
				}

			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					LOG.error("ObjectInputStream couldn't be closed", e);
				}

		}
	}

	/**
	 * Saves the user's settings to the hard drive under the user's home directory
	 * -> Jaims -> the logged-in-user's nickname -> settings
	 * 
	 * 
	 * @see Settings
	 */
	public void saveSettings() {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String filename = userHome + "/Jaims/" + loggedInUsername + "/settings/settings.set";
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			oos.writeObject(settings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Creates the directories JAIMS needs under the user's home directory
	 * 
	 * @param path the path where the directories have to be created
	 */
	private void createDirectory(String path) {

		File f = new File(path);
		if (!f.exists())
			f.mkdirs();

		File settings = new File(path + "/settings");
		if (!settings.exists())
			settings.mkdirs();
	}

	public ArrayList<DFEObject> getDFEObjectsForUser(UUID user) {

		return databaseConnection.getDFEObjects(user);
	}

	/**
	 * 
	 * @param s The Sendable that has to be sent to the server
	 * 
	 * @see ServerConnection#sendSendable(Sendable)
	 */
	public void sendSendable(Sendable s) {

		sc.sendSendable(s);
	}

	/**
	 * This method is called when the user logs out. <br>
	 * Before finally logging out, the following things are being done:
	 * <ul>
	 * <li>If the user's profile has been updated it is sent to the server</li>
	 * <li>The connection to the database is going to be closed</li>
	 * <li>The connection to the server is going to be closed</li>
	 * <li>The GUI is going to be disposed</li>
	 * <li>The program is initialised anew</li>
	 * </ul>
	 * 
	 * @see DatabaseConnection#getAndUpdateProfile(UUID)
	 * @see ServerConnection#sendSendable(Sendable)
	 * @see DatabaseConnection#closeConnection()
	 * @see ServerConnection#disconnect()
	 * @see GUIMain#closeGUI()
	 * @see ClientMain#initProgram(boolean)
	 */
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

	public void saveNewEvent(Sendable s, EEventType eventType) {

		databaseConnection.saveNewEvent(s, eventType);
		guiMain.addNewEvent();
	}

	public ArrayList<Event> getAllPendingEvents() {

		return databaseConnection.getAllPendingEvents();
	}

	public DatabaseConnection getDatabaseConnection() {

		return databaseConnection;
	}
}