package jaims_development_studio.jaims.clientfx.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {

	private static final Logger	LOG	= LoggerFactory.getLogger(DatabaseConnection.class);

	private static Connection	con;

	public static void initDatabaseConnection(String username) {

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
	}

	public static boolean closeConnection() {

		try {
			con.close();
		} catch (SQLException e) {
			closeConnection();
		}

		LOG.info("Closed database-connection");

		return true;

	}

}
