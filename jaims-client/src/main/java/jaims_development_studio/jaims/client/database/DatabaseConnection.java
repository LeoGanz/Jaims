package jaims_development_studio.jaims.client.database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnection.class);

	String username;
	private static Connection con;
	private WriteToDatabase wtd;
	
	public void initConnection() {
		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:c:/database/jaims/", "JAIMS_Client", "LBOgHFk0HOOcDVDL1oOW");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("Failed to connect to database");
			JOptionPane.showMessageDialog(null, "Couldn't connect to Database", "Database connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public Thread readFromDatabase(String username) {
		return ReadFromDatabase.readDatabase(username, con);
	}
	
	public Thread writeToDatabase() {
		return WriteToDatabase.writeToDatabase(username, con);
	}
	
	public WriteToDatabase getWTD(String username) {
		return wtd = new WriteToDatabase(username, con);
	}
	public static Connection getConnection() {
		return con;
	}
}
