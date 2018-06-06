package jaims_development_studio.jaims.client.database;

import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.DFEObject;
import jaims_development_studio.jaims.client.logic.SimpleContact;

/**
 * This class handles the connection to and from the database. After
 * initialising a connection it acts as the interface between the client and the
 * database. It takes requests for "writing to" and "reading from" the database
 * and forwards it to the specific methods
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see #initConnection(String)
 *
 */
public class DatabaseConnection {

	private static final Logger	LOG	= LoggerFactory.getLogger(DatabaseConnection.class);

	private static Connection	con;
	private ReadFromDatabase	readFromDatabase;
	private WriteToDatabase		writeToDatabase;

	public DatabaseConnection(String username) {

		initConnection(username);
	}

	/**
	 * Builds a connection to the database and creates instances of
	 * {@link ReadFromDatabase} and {@link WriteToDatabase}.
	 * 
	 * @param username
	 *            the logged-in-user's name
	 */
	private void initConnection(String username) {

		File f = new File(System.getProperty("user.home").replace("\\", "/") + "/Jaims/" + username + "/database/");
		if (!f.exists())
			f.mkdirs();

		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:" + f.getAbsolutePath() + "/", "JAIMS_Client",
					"LBOgHFk0HOOcDVDL1oOW");
		} catch (SQLException e) {
			LOG.error("Failed to connect to database", e);
			JOptionPane.showMessageDialog(null, "User is already logged in!", "Database connection error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		readFromDatabase = new ReadFromDatabase(con);
		writeToDatabase = new WriteToDatabase(con);
	}

	/**
	 * Closes the connection to the database.
	 * 
	 * @return returns true after connection was successfully closed.
	 */
	public boolean closeConnection() {

		try {
			con.close();
		} catch (SQLException e) {
			closeConnection();
		}

		LOG.info("Closed database-connection");

		return true;

	}

	/**
	 * Gets all available contacts from the database.
	 * 
	 * @return ArrayList filled with SimpleContacts
	 * 
	 * @see ReadFromDatabase#getSimpleContacts()
	 * @see ArrayList
	 * @see SimpleContact
	 */
	public ArrayList<SimpleContact> getSimpleContacts() {

		if (readFromDatabase.hasTables() == false)
			readFromDatabase.createTables();

		return readFromDatabase.getSimpleContacts();
	}

	/**
	 * Gets all with whom the user has an existing chat.
	 * 
	 * @return ArrayList filles with SimpleContacts
	 * 
	 * @see ReadFromDatabase#getSimpleChatContacts()
	 * @see ArrayList
	 * @see SimpleContact
	 */
	public ArrayList<SimpleContact> getSimpleChatContacts() {

		return readFromDatabase.getSimpleChatContacts();
	}

	/**
	 * Gets all messages with the contact specified by the UUID.
	 * 
	 * @param uuid
	 *            the contact's UUID.
	 * @return ArrayList filled with messages
	 * 
	 * @see jaims_development_studio.jaims.client.chatObjects.ClientInternMessage
	 *      ClientInternMessage
	 * @see ReadFromDatabase#getContactMessages(UUID)
	 * @see UUID
	 * @see ArrayList
	 */
	public ArrayList<jaims_development_studio.jaims.client.chatObjects.ClientInternMessage> getContactMessages(
			UUID uuid) {

		return readFromDatabase.getContactMessages(uuid);
	}

	/**
	 * Gets the contact representing the logged-in-user
	 * 
	 * @param uuid
	 *            The logged-in-user's UUID
	 * @param username
	 *            the logged-in-users nickname
	 * @param cm
	 *            instance representing the ClientMain class
	 * @return logged-in-user's <code>SimpleContact</code> equivalent
	 * 
	 * @see ReadFromDatabase#getUserContact(UUID, String, ClientMain)
	 * @see UUID
	 * @see ClientMain
	 */
	public SimpleContact getUserContact(UUID uuid, String username, ClientMain cm) {

		if (readFromDatabase.hasTables() == false)
			readFromDatabase.createTables();

		return readFromDatabase.getUserContact(uuid, username, cm);
	}

	/**
	 * Gets a contact's profile picture.
	 * 
	 * @param uuid
	 *            the contact's UUID whose picture the program is looking for
	 * @return a contact's profile picture
	 * 
	 * @see ReadFromDatabase#getProfileImage(UUID)
	 * @see UUID
	 * @see Image
	 */
	public Image getProfileImage(UUID uuid) {

		return readFromDatabase.getProfileImage(uuid);
	}

	/**
	 * Gets the user's profile picture.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @return the user's profile picture
	 * 
	 * @see ReadFromDatabase#getUserProfileImage(UUID)
	 * @see UUID
	 * @see Image
	 */
	public Image getUserProfileImage(UUID uuid) {

		return readFromDatabase.getUserProfileImage(uuid);
	}

	/**
	 * @return the chat background image
	 * 
	 * @see ReadFromDatabase#getChatBackground()
	 * @see Image
	 */
	public Image getChatBackground() {

		return readFromDatabase.getChatBackground();
	}

	/**
	 * 
	 * @param uuid
	 *            The contact's UUId whose status has to be retrieved
	 * @return the contacts status
	 * 
	 * @see ReadFromDatabase#getContactStatus(UUID)
	 */
	public String getContactStatus(UUID uuid) {

		return readFromDatabase.getContactStatus(uuid);
	}

	/**
	 * Returns a boolean which indicated whether a chat history with the user
	 * specified by the <code>UUID</code> exists.
	 * 
	 * @param uuid
	 *            the contact's UUID
	 * @return true if chat exists, otherwise false
	 * 
	 * @see ReadFromDatabase#hasEntry(UUID)
	 * @see UUID
	 */
	public boolean hasEntry(UUID uuid) {

		return readFromDatabase.hasEntry(uuid);
	}

	/**
	 * Saves the given profile to the database.
	 * 
	 * @param pf
	 *            the profile to be saved
	 * @param contact
	 *            indicated whether to save as contact or as user profile
	 * @return true if saved successfully
	 * 
	 * @see WriteToDatabase#saveProfile(Profile, boolean)
	 * @see Profile
	 */
	public boolean saveProfile(Profile pf, boolean contact) {

		return writeToDatabase.saveProfile(pf, contact);
	}

	/**
	 * Deletes the <code>Profile</code> associated with the given <code>UUID</code>
	 * from the database.
	 * 
	 * @param uuid
	 *            the profile's UUID
	 * @return true if successfully deleted
	 * 
	 * @see WriteToDatabase#deleteContact(UUID)
	 * @see UUID
	 * @see Profile
	 */
	public boolean deleteProfile(UUID uuid) {

		return writeToDatabase.deleteContact(uuid);
	}

	/**
	 * Updates the <code>Profile</code> associated with the given <code>UUID</code>
	 * in the database.
	 * 
	 * @param pf
	 *            the new version of the profile
	 * @param contact
	 *            update a contact's profile or the user's profile
	 * 
	 * @see WriteToDatabase#updateProfile(Profile, boolean)
	 * @see Profile
	 * @see UUID
	 */
	public void updateProfile(Profile pf, boolean contact) {

		writeToDatabase.updateProfile(pf, contact);
	}

	/**
	 * Saves,as the name suggests, a new text message to the database.
	 * 
	 * @param m
	 *            the message to be saved
	 * 
	 * @see WriteToDatabase#saveTextMessage(ClientInternMessage)
	 * @see ClientInternMessage
	 */
	public void saveTextMessage(Message m) {

		writeToDatabase.saveTextMessage(m);
	}

	/**
	 * Saves the given <code>VoiceMessage</code> to the database.
	 * 
	 * @param m
	 *            the voice message to be saved
	 * @param pathToFile
	 *            the path to the folder where the audio is saved
	 */
	public void saveVoiceMessage(Message m, String pathToFile) {

		writeToDatabase.saveVoiceMessage(m, pathToFile);
	}

	/**
	 * Updates the contacts table in the database and sets the boolean hasChat of
	 * the contact specified by the UUID to the new value.
	 * 
	 * @param hasChat
	 *            the new boolean value
	 * @param contactID
	 *            the contact's UUID
	 * 
	 * @see WriteToDatabase#updateHasChat(boolean, UUID)
	 * @see UUID
	 */
	public void updateHasChat(boolean hasChat, UUID contactID) {

		writeToDatabase.updateHasChat(hasChat, contactID);
	}

	/**
	 * Updates the user's profile in the database and return the updated version.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @return the updated user profile
	 * 
	 * @see ReadFromDatabase#getAndUpdateUser(UUID)
	 * @see UUID
	 * @see Profile
	 */
	public Profile getAndUpdateProfile(UUID uuid) {

		return readFromDatabase.getAndUpdateUser(uuid);
	}

	/**
	 * Returns the {@link Connection} object which holds the connection to the
	 * database.
	 * 
	 * @return a <code>Connection</code> object
	 * 
	 * @deprecated
	 */
	public static Connection getConnection() {

		return con;
	}

	public ArrayList<DFEObject> getDFEObjects(UUID user) {

		return readFromDatabase.getDFEObjects(user);
	}
}
