package jaims_development_studio.jaims.client.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.profile.Profile;

/**
 * This class handles every request concerning writing something to the
 * database. Every request issued somewhere in the program is forwarded to this
 * class and dealt with in the appropriate method.
 * 
 * @author Bu88le
 *
 * @since v0.1.0
 *
 */
public class WriteToDatabase {

	private static final Logger	LOG	= LoggerFactory.getLogger(WriteToDatabase.class);

	private Connection			con;
	private PreparedStatement	pStatement;

	public WriteToDatabase(Connection con) {

		this.con = con;
	}

	/**
	 * Saved a given profile to the database and decides based on the given boolean
	 * whether to save it as a contact or an user. If the saving fails the method
	 * return false.
	 * 
	 * @param pf
	 *            the profile to be saved
	 * @param contact
	 *            save it as a contact or an user
	 * @return true if successfully saved otherwise false
	 * 
	 * @see Profile
	 */
	public boolean saveProfile(Profile pf, boolean contact) {

		String sql;
		if (contact)
			sql = "INSERT INTO CONTACTS VALUES (?,?,?,?,?,?,?)";
		else
			sql = "INSERT INTO USER VALUES (?,?,?,?,?,?)";

		try {
			pStatement = con.prepareStatement(sql);
			pStatement.setObject(1, pf.getUuid());
			pStatement.setString(2, pf.getNickname());
			pStatement.setString(3, pf.getDescription());
			pStatement.setString(4, pf.getStatus());
			pStatement.setBytes(5, pf.getProfilePicture());
			pStatement.setTimestamp(6, new Timestamp(pf.getLastUpdated().getTime()));
			if (contact)
				pStatement.setBoolean(7, false);
			pStatement.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException e) {
			LOG.error("Exception while saving profile", e);

			return false;
		}

	}

	/**
	 * Updates the given profile's entry in the database. The boolean 'contact'
	 * indicates whether it is a contact's profile or the user's profile that need
	 * to be updated.
	 * 
	 * @param pf
	 *            the updated profile to be written to the database
	 * @param contact
	 *            indicated whether a contact's profile or the user's profile needs
	 *            to be updated
	 * 
	 * @see Profile
	 */
	public void updateProfile(Profile pf, boolean contact) {

		String sql;
		if (contact)
			sql = "UPDATE CONTACTS SET NICKNAME=?, DESCRIPTION=?,STATUS=?,PROFILE_PICTURE=?,LAST_UPDATED=? WHERE CONTACT_ID=?";
		else
			sql = "UPDATE USER SET NICKNAME=?, DESCRIPTION=?,STATUS=?,PROFILE_PICTURE=?,LAST_UPDATED=? WHERE USER_ID=?";

		try {
			pStatement = con.prepareStatement(sql);
			pStatement.setString(1, pf.getNickname());
			pStatement.setString(2, pf.getDescription());
			pStatement.setString(3, pf.getStatus());
			pStatement.setBytes(4, pf.getProfilePicture());
			pStatement.setTimestamp(5, new Timestamp(pf.getLastUpdated().getTime()));
			pStatement.setObject(6, pf.getUuid());
			pStatement.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			LOG.error("Couldn't update profile", e);
		}
	}

	/**
	 * Updated a contact's 'hasChat'-field in the database. The boolean indicates,
	 * as the name suggests, whether the contact has an existing chat history with
	 * the user.
	 * 
	 * @param hasChat
	 *            indicated whether the contact has an existing chat
	 * @param contactID
	 *            the contact's UUID
	 * 
	 * @see UUID
	 */
	public void updateHasChat(boolean hasChat, UUID contactID) {

		try {
			pStatement = con.prepareStatement("UPDATE CONTACTS SET HAS_CHAT=? WHERE CONTACT_ID=?");
			pStatement.setBoolean(1, hasChat);
			pStatement.setObject(2, contactID);
			pStatement.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			LOG.error("Couldn't update 'hasChat'", e);
		}

	}

	/**
	 * This method saves a given text message to the database.
	 * 
	 * @param m
	 *            the message to be saved
	 * 
	 * @see Message
	 */
	public void saveTextMessage(Message m) {

		try {
			pStatement = con.prepareStatement("INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)");
			pStatement.setObject(1, m.getUuid());
			pStatement.setObject(2, m.getSender());
			pStatement.setObject(3, m.getRecipient());
			pStatement.setString(4, m.getMessageType().getValue());
			pStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			if (m.getTimestampRead() == null)
				pStatement.setTimestamp(6, null);
			else
				pStatement.setTimestamp(6, new Timestamp(m.getTimestampRead().getTime()));
			if (m.getTimestampDelivered() == null)
				pStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			else
				pStatement.setTimestamp(7, new Timestamp(m.getTimestampDelivered().getTime()));
			pStatement.setString(8, ((TextMessage) m).getMessage());
			pStatement.executeUpdate();
			con.commit();

		} catch (NullPointerException npe) {
			LOG.error("NullPointerException", npe);
		} catch (SQLException e) {
			LOG.error("Failed to save message", e);
		}
	}

	/**
	 * Saves the given voice message to the database where the message field is set
	 * to the audio's path on the hard drive.
	 * 
	 * @param m
	 *            the message to be saved
	 * @param pathToFile
	 *            the path to the audio
	 * 
	 * @see Message
	 */
	public void saveVoiceMessage(Message m, String pathToFile) {

		try {
			pStatement = con.prepareStatement("INSERT INTO MESSAGES VALUES (?,?,?,?,?,?,?,?)");
			pStatement.setObject(1, UUID.randomUUID());
			pStatement.setObject(2, m.getSender());
			pStatement.setObject(3, m.getRecipient());
			pStatement.setString(4, m.getMessageType().getValue());
			pStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			if (m.getTimestampRead() != null && m.getTimestampDelivered() != null) {
				pStatement.setTimestamp(6, new Timestamp(m.getTimestampRead().getTime()));
				pStatement.setTimestamp(7, new Timestamp(m.getTimestampDelivered().getTime()));
			} else if (m.getTimestampDelivered() != null && m.getTimestampRead() == null) {
				pStatement.setTimestamp(6, null);
				pStatement.setTimestamp(7, new Timestamp(m.getTimestampDelivered().getTime()));
			} else if (m.getTimestampDelivered() == null && m.getTimestampRead() != null) {
				pStatement.setTimestamp(6, new Timestamp(m.getTimestampRead().getTime()));
				pStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			} else {
				pStatement.setTimestamp(6, null);
				pStatement.setTimestamp(7, null);
			}
			pStatement.setString(8, pathToFile);
			pStatement.executeUpdate();
			con.commit();

		} catch (NullPointerException npe) {
			LOG.error("NullPointerException", npe);
		} catch (SQLException e) {
			LOG.error("Failed to save Message", e);
		}
	}

	/**
	 * Deletes an existing contact from the database.
	 * 
	 * @param uuid
	 *            the conact's UUID
	 * @return true if successfully deleted
	 * 
	 * @see UUID
	 */
	public boolean deleteContact(UUID uuid) {

		try {
			pStatement = con.prepareStatement("DELETE FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);

			pStatement.executeUpdate();
			con.commit();

			return true;
		} catch (SQLException e) {
			LOG.error("Failed to delete contact", e);
			return false;
		}

	}
}
