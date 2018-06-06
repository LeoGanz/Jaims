package jaims_development_studio.jaims.client.database;

import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.DFEObject;
import jaims_development_studio.jaims.client.logic.EFileType;
import jaims_development_studio.jaims.client.logic.SimpleContact;

/**
 * This class is responsible for every action that needs to fetch something from
 * the database. Every request issued somewhere in the program is forwarded to
 * this class where the necessary actions to download the wanted content are
 * taken in the appropriate methods.
 * 
 * @author Bu88le
 *
 * @since v0.1.0
 */
public class ReadFromDatabase {

	private static final Logger	LOG				= LoggerFactory.getLogger(ReadFromDatabase.class);

	private Connection			con;
	private ResultSet			rs;
	private Statement			statement;
	private PreparedStatement	pStatement;
	private boolean				messagesExist	= false, contactsExist = false, userExists = false, imageExists = false,
			informationsExists = false, largeFilesExists = false;

	public ReadFromDatabase(Connection con) {

		this.con = con;
	}

	/**
	 * Checks if all the necessary tables in the database exists.
	 * 
	 * @return boolean whether tables exist
	 */
	public boolean hasTables() {

		try {
			// Gets all the tables available in the db
			rs = con.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while (rs.next()) {
				String tName = rs.getString("TABLE_NAME");
				if (tName != null && tName.equals("MESSAGES")) {
					messagesExist = true;
				} else if (tName != null && tName.equals("CONTACTS")) {
					contactsExist = true;
				} else if (tName != null && tName.equals("USER"))
					userExists = true;
				else if (tName != null && tName.equals("IMAGE"))
					imageExists = true;
				else if (tName != null && tName.equals("INFORMATIONS"))
					informationsExists = true;
				else if (tName != null && tName.equals("LARGE_FILES"))
					largeFilesExists = true;
			}
			if (messagesExist && contactsExist && userExists && imageExists && informationsExists)
				return true;
			else
				return false;
		} catch (SQLException e) {
			LOG.error("Failed to create resultSet!", e);
			return false;
		}
	}

	/**
	 * If {@link #hasTables()} has returned false, this method is called and creates
	 * all the necessary tabled with the needed columns in the database.
	 * 
	 * @see Statement
	 */
	public void createTables() {

		try {
			Statement s = con.createStatement();
			if (messagesExist == false) {
				s.execute(
						"CREATE TABLE MESSAGES (MESSAGE_ID UUID PRIMARY KEY NOT NULL, SENDER_ID UUID NOT NULL, RECIPIENT_ID UUID NOT NULL, MESSAGE_TYPE VARCHAR(64) NOT NULL,TIMESTAMP_SENT TIMESTAMP, TIMESTAMP_READ TIMESTAMP, TIMESTAMP_RECIEVED TIMESTAMP, MESSAGE_STRING VARCHAR(8096))");
				con.commit();
			}
			if (contactsExist == false) {
				s.execute(
						"CREATE TABLE CONTACTS (CONTACT_ID UUID PRIMARY KEY NOT NULL, NICKNAME VARCHAR(256), DESCRIPTION VARCHAR(4096), STATUS VARCHAR(4096), PROFILE_PICTURE BLOB, LAST_UPDATED TIMESTAMP NOT NULL, HAS_CHAT BOOLEAN NOT NULL)");
				con.commit();
			}
			if (userExists == false) {
				s.execute(
						"CREATE TABLE USER (USER_ID UUID PRIMARY KEY NOT NULL, NICKNAME VARCHAR(256), DESCRIPTION VARCHAR(4096), STATUS VARCHAR(4096), PROFILE_PICTURE BLOB, LAST_UPDATED TIMESTAMP NOT NULL)");
				con.commit();
			}
			if (imageExists == false) {
				s.execute("CREATE TABLE IMAGE (BACKGROUND_IMAGE BLOB)");
				con.commit();
			}
			if (informationsExists == false) {
				s.execute(
						"CREATE TABLE INFORMATIONS (USER_ID UUID PRIMARY KEY NOT NULL, TOTAL_NUMBER_MESSAGES INTEGER NOT NULL, NUMBER_OWN_MESSAGES INTEGER NOT NULL, NUMBER_CONTACT_MESSAGES INTEGER NOT NULL, NUMBER_TOTAL_TEXT_MESSAGES INTEGER NOT NULL, NUMBER_OWN_TEXT_MESSAGES INTEGER NOT NULL, NUMBER_CONTACT_TEXT_MESSAGES INTEGER NOT NULL, NUMBER_WORDS_USED INTEGER NOT NULL, NUMBER_OWN_WORDS_USED INTEGER NOT NULL, NUMBER_CONTACTS_WORDS_USED INTEGER NOT NULL, NUMBER_TOTAL_VOICE_MESSAGES INTEGER NOT NULL, NUMBER_OWN_VOICE_MESSAGES INTEGER NOT NULL, NUMBER_CONTACT_VOICE_MESSAGES INTEGER NOT NULL, NUMBER_TOTAL_IMAGE_MESSAGES INTEGER NOT NULL, NUMBER_OWN_IMAGE_MESSAGES INTEGER NOT NULL, NUMBER_CONTACT_IMAGE_MESSAGES INTEGER NOT NULL, NUMBER_TOTAL_FILES_SENT INTEGER NOT NULL, NUMBER_OWN_FILES_SENT INTEGER NOT NULL, NUMBER_CONTACT_FILES_SENT INTEGER NOT NULL, NUMBER_TOTAL_LOCATIONS_SENT INTEGER NOT NULL, NUMBER_OWN_LOCATIONS_SENT INTEGER NOT NULL, NUMBER_CONTACT_LOCATIONS_SENT INTEGER NOT NULL, NUMBER_TOTAL_CONTACTS_SENT INTEGER NOT NULL, NUMBER_OWN_CONTACTS_SENT INTEGER NOT NULL, NUMBER_CONTACT_CONTACTS_SENT INTEGER NOT NULL, NUMBER_TOTAL_CALENDERES_SENT INTEGER NOT NULL, NUMBER_OWN_CALENDERES_SENT INTEGER NOT NULL, NUMBER_CONTACT_CALENDERES_SENT INTEGER NOT NULL);");
				con.commit();
			}
			if (largeFilesExists == false) {
				s.execute(
						"CREATE TABLE LARGE_FILES (FILE_ID UUID NOT NULL PRIMARY KEY, SENDER UUID NOT NULL, RECIPIENT UUID NOT NULL, FILENAME VARCHAR(256) NOT NULL, PATH VARCHAR(512) NOT NULL, EXTENSION VARCHAR(32) NOT NULL, FILE_TYPE VARCHAR(64) NOT NULL, DATE_SENT TIMESTAMP NOT NULL)");
				con.commit();
			}
		} catch (SQLException e) {
			LOG.error("Failed to execute the statement", e);
		}
	}

	/**
	 * Gets the logged-in-user's <code>SimpleContact</code> from the database. If
	 * the database doesn't have a profile of the user saved it requests a profile
	 * from the server and returns the <code>SimpleContact</code> with the least
	 * needed information - the UUID and the username.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @param username
	 *            the user's nickname
	 * @param cm
	 *            ClientMain instance
	 * @return the user's SimpleContact
	 * 
	 * @see SimpleContact
	 * @see UUID
	 * @see ClientMain
	 */
	public SimpleContact getUserContact(UUID uuid, String username, ClientMain cm) {

		try {
			pStatement = con.prepareStatement("SELECT * from USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			if (rs.next() && rs.getObject(1) != null) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2));
				return simpleContact;
			} else {
				Thread thread = new Thread("Thread-RequestUserProfile") {
					@Override
					public void run() {

						cm.requestUserProfile(uuid);
					}
				};
				thread.start();
				return new SimpleContact(uuid, username);
			}

		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
			new Thread() {
				@Override
				public void run() {

					cm.requestUserProfile(uuid);
				}
			}.start();
			return new SimpleContact(uuid, username);
		}
	}

	/**
	 * Updates the user's profile in the database and return an updated version. If
	 * the statements can't be executed and an exception is thrown the method is
	 * called again until the profile was successfully updated and retrieved again.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @return the user's profile
	 * 
	 * @see UUID
	 * @see Profile
	 */
	public Profile getAndUpdateUser(UUID uuid) {

		try {
			pStatement = con.prepareStatement("UPDATE USER SET LAST_UPDATED=? WHERE CONTACT_ID=?");
			pStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pStatement.setObject(2, uuid);

			pStatement = con.prepareStatement("SELECT * from USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			Profile user = new Profile((UUID) rs.getObject(1), null, rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getBytes(5), convertToDate(rs.getTimestamp(6)));
			return user;
		} catch (SQLException e) {
			LOG.error("Couldn't execute statement properly! Trying again...", e);
			getAndUpdateUser(uuid);
		}

		return null;
	}

	/**
	 * Gets the date the user's profile in the database was last updated. This needs
	 * to be done in order to get the newest version of the profile from the server.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @return the date of the last update
	 * 
	 * @see Date
	 * @see UUID
	 * @see #convertToDate(Timestamp)
	 */
	public Date getUserLastUpdatedDate(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT LAST_UPDATED FROM USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			if (rs.getBytes(1) != null) {
				return convertToDate(rs.getTimestamp(1));
			} else {
				return convertToDate(new Timestamp(0L));
			}
		} catch (SQLException e) {
			LOG.error("Failed to retrieve date from the database", e);
			return convertToDate(new Timestamp(0L));
		}
	}

	/**
	 * Gets all available contacts from the database and adds them to an
	 * <code>ArrayList</code>.
	 * 
	 * @return ArrayList filled with all available contacts
	 * 
	 * @see SimpleContact
	 * @see ArrayList
	 */
	public ArrayList<SimpleContact> getSimpleContacts() {

		ArrayList<SimpleContact> simpleContactList = new ArrayList<>();

		try {
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * from CONTACTS");
			while (rs.next()) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2),
						rs.getBoolean(7));
				simpleContactList.add(simpleContact);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}
		return simpleContactList;
	}

	/**
	 * Gets all contact's that have an existing chat and adds them to an
	 * <code>ArrayList</code>.
	 * 
	 * @return ArrayList filled with contacts having an existing chat
	 * 
	 * @see SimpleContact
	 * @see ArrayList
	 */
	public ArrayList<SimpleContact> getSimpleChatContacts() {

		ArrayList<SimpleContact> getSimpleChatContact = new ArrayList<>();

		try {
			pStatement = con.prepareStatement("SELECT * from CONTACTS WHERE HAS_CHAT=?");
			pStatement.setBoolean(1, true);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2),
						rs.getBoolean(7));
				getSimpleChatContact.add(simpleContact);
			}
			pStatement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		return getSimpleChatContact;
	}

	/**
	 * Gets all messages from the database where the user specified by the UUID is
	 * either the recipient or the sender. After having retrieved this messages they
	 * are added to an ArrayList an sorted by the date they where received. In case
	 * of own messages the 'received date' is the date the message was created.
	 * 
	 * @param uuid
	 *            the contact's UUID
	 * @return an ArrayList with messages
	 * 
	 * @see ClientInterMessage
	 * @see UUID
	 * @see ArrayList
	 */
	public ArrayList<ClientInternMessage> getContactMessages(UUID uuid) {

		ArrayList<ClientInternMessage> mList = new ArrayList<>();

		try {
			pStatement = con.prepareStatement("SELECT * from MESSAGES WHERE SENDER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				ClientInternMessage message = new ClientInternMessage((UUID) rs.getObject(1), (UUID) rs.getObject(2),
						(UUID) rs.getObject(3), rs.getString(4), convertToDate(rs.getTimestamp(5)),
						convertToDate(rs.getTimestamp(6)), convertToDate(rs.getTimestamp(7)), rs.getString(8));
				mList.add(message);
			}
			pStatement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		try {
			pStatement = con.prepareStatement("SELECT * from MESSAGES WHERE RECIPIENT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				ClientInternMessage message = new ClientInternMessage((UUID) rs.getObject(1), (UUID) rs.getObject(2),
						(UUID) rs.getObject(3), rs.getString(4), convertToDate(rs.getTimestamp(5)),
						convertToDate(rs.getTimestamp(6)), convertToDate(rs.getTimestamp(5)), rs.getString(8));
				mList.add(message);
			}
			pStatement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		try {
			mList.sort((m1, m2) -> {

				if (m1.getRecieved().compareTo(m2.getRecieved()) > 0)
					return 1;
				else if (m1.getRecieved().compareTo(m2.getRecieved()) < 0)
					return -1;
				else
					return 0;
			});
		} catch (NullPointerException npe) {
			LOG.error("Couldn't sort list!", npe);
		}

		return mList;
	}

	/**
	 * Loads the profile picture of the contact specified by the UUID from the
	 * database and converts it to an image. If there is no profile picture in the
	 * database or the loading fails, the standard picture is loaded.
	 * 
	 * @param uuid
	 *            the contact's UUID whose picture the program is looking for
	 * @return the contact's profile picture
	 * 
	 * @see UUID
	 * @see Image
	 */
	public Image getProfileImage(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT PROFILE_PICTURE FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
			}
		} catch (SQLException e) {
			LOG.error("Couldn't load image from database");
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
		}
	}

	/**
	 * Gets the user's profile picture from the database and converts it to an
	 * image. If there is no profile picture or the loading fails, the standard
	 * picture is loaded.
	 * 
	 * @param uuid
	 *            the user's UUID
	 * @return the user's profile picture
	 * 
	 * @see UUID
	 * @see Image
	 */
	public Image getUserProfileImage(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT PROFILE_PICTURE FROM USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();
			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
			}
		} catch (SQLException e) {
			LOG.error("Couldn't load profile picture from database");
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
		}
	}

	/**
	 * Loads the user's choice of the chat background image from the database. If
	 * the user hasn't yet selected a custom background image or if the loading
	 * fails, the standard chat background will be loaded.
	 * 
	 * @return the chat background image
	 * 
	 * @see Image
	 */
	public Image getChatBackground() {

		try {
			pStatement = con.prepareStatement("SELECT BACKGROUND_IMAGE FROM IMAGE");
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();
			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/LoginBackground.png"));
			}
		} catch (SQLException e) {
			LOG.error("Couldn't load the chat background image");
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/LoginBackground.png"));
		} catch (NullPointerException npe) {
			LOG.error("NullPointerException while loading chat background", npe);
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/LoginBackground.png"));
		}
	}

	/**
	 * 
	 * @param uuid
	 *            the contact's UUID whose status the program is looking for
	 * @return the contact's status
	 * 
	 * @see UUID
	 */
	public String getContactStatus(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT STATUS FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			return rs.getString(1);
		} catch (SQLException e) {
			LOG.error("Failed to retrieve contact's status", e);
			return "Available";
		}
	}

	/**
	 * @param uuid
	 *            the contact's UUID whose description the program is looking for
	 * @return the contact's description
	 * 
	 * @see UUID
	 */
	public String getContactDescription(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT DESCRIPTION FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			return rs.getString(1);
		} catch (SQLException e) {
			LOG.error("Couldn't load description", e);
			return "About me.";
		}
	}

	/**
	 * Checks the database whether an entry for the given UUID exists.
	 * 
	 * @param uuid
	 *            the UUID to be checked
	 * @return true if an entry exists otherwise false
	 * 
	 * @see UUID
	 */
	public boolean hasEntry(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT * from CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();

			if (rs.next())
				return true;
			else {
				pStatement = con.prepareStatement("SELECT * from USER WHERE USER_ID=?");
				pStatement.setObject(1, uuid);
				rs = pStatement.executeQuery();
				if (rs.next())
					return true;
				else
					return false;
			}

		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
			return false;
		}
	}

	public ArrayList<DFEObject> getDFEObjects(UUID user) {

		ArrayList<DFEObject> list = new ArrayList<>();
		try {
			pStatement = con.prepareStatement("SELECT * FROM LARGE_FILES WHERE (CONTACT_ID=? OR USER_ID=?)");
			pStatement.setObject(1, user);
			pStatement.setObject(2, user);
			rs = pStatement.executeQuery();
			con.commit();

			while (rs.next()) {
				DFEObject dfeo = new DFEObject((UUID) rs.getObject(1), (UUID) rs.getObject(2), (UUID) rs.getObject(3),
						rs.getString(4), rs.getString(5), rs.getString(6), EFileType.valueOf(rs.getString(7)),
						convertToDate(rs.getTimestamp(8)));
				list.add(dfeo);
			}

			return list;

		} catch (SQLException e) {
			LOG.error("SQL Exception", e);
			return list;
		}
	}

	/**
	 * Converts a sql Timestamp retrieved from the database to a java util Date.
	 * 
	 * @param ts
	 *            the timestamp to be converted
	 * @return a date equivalent to the given timestamp
	 * 
	 * @see java.sql.Timestamp Timestamp
	 * @see java.util.Date Date
	 */
	private Date convertToDate(Timestamp ts) {

		if (ts != null) {
			long millis = ts.getTime();
			return new Date(millis);
		} else
			return null;
	}

}
