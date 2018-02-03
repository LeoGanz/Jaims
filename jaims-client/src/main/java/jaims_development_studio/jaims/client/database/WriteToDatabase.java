package jaims_development_studio.jaims.client.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public void saveProfile(Profile pf, boolean contact) {
		String sql;
		if (contact)
			sql = "INSERT INTO CONTACTS VALUES (?,?,?,?,?,?)";
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
			pStatement.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateProfile(Profile pf, boolean contact) {
		String sql;
		if (contact)
			sql = "UPDATE CONTACTS SET NICKNAME=?, DESCRIPTION=?,STATUS=?,PROFILE_PICTURE=?,LAST_UPDATED=? WHERE CONTACT_ID=?";
		else
			sql = "UPDATE USER SET NICKNAME=?, DESCRIPTION=?,STATUS=?,PROFILE_PICTURE=?,LAST_UPDATED=? WHERE CONTACT_ID=?";

		try {
			pStatement = con.prepareStatement(sql);
			pStatement.setString(1, pf.getNickname());
			pStatement.setString(2, pf.getDescription());
			pStatement.setString(3, pf.getStatus());
			pStatement.setBytes(4, pf.getProfilePicture());
			pStatement.setTimestamp(5, new Timestamp(pf.getLastUpdated().getTime()));
			pStatement.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
