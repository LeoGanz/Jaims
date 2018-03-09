package jaims_development_studio.jaims.client.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.profile.Profile;

public class WriteToDatabase {

	private static final Logger	LOG	= LoggerFactory.getLogger(WriteToDatabase.class);

	private String				tablename;
	private Connection			con;
	private Statement			s;
	private PreparedStatement	pStatement;
	private ResultSet			rs;

	public WriteToDatabase(Connection con) {

		this.con = con;
	}

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
			System.out.println("Saved");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

	}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateHasChat(boolean hasChat, UUID contactID) {

		try {
			pStatement = con.prepareStatement("UPDATE CONTACTS SET HAS_CHAT=? WHERE CONTACT_ID=?");
			pStatement.setBoolean(1, hasChat);
			pStatement.setObject(2, contactID);
			pStatement.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveTextMessage(Message m) {

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
				pStatement.setTimestamp(7, null);
			} else {
				pStatement.setTimestamp(6, null);
				pStatement.setTimestamp(7, null);
			}
			pStatement.setString(8, ((TextMessage) m).getMessage());
			pStatement.executeUpdate();
			con.commit();

		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deleteContact(UUID uuid) {

		try {
			pStatement = con.prepareStatement("DELETE FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);

			pStatement.executeUpdate();
			con.commit();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
