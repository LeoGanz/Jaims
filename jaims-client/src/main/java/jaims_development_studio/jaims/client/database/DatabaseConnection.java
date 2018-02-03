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

import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class DatabaseConnection {

	private static final Logger	LOG	= LoggerFactory.getLogger(DatabaseConnection.class);

	private static Connection	con;
	private ReadFromDatabase	readFromDatabase;

	public DatabaseConnection(String username) {

		initConnection(username);
	}

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
	}

	public ArrayList<SimpleContact> getSimpleContacts() {

		if (readFromDatabase.hasTables() == false)
			readFromDatabase.createTables();

		return readFromDatabase.getSimpleContacts();
	}

	public ArrayList<SimpleContact> getSimpleChatContacts() {

		return readFromDatabase.getSimpleChatContacts();
	}

	public ArrayList<Message> getContactMessages(UUID uuid) {

		return readFromDatabase.getContactMessages(uuid);
	}

	public SimpleContact getUserContact(UUID uuid, String username, ClientMain cm) {

		if (readFromDatabase.hasTables() == false)
			readFromDatabase.createTables();

		return readFromDatabase.getUserContact(uuid, username, cm);
	}

	public Image getProfileImage(UUID uuid) {

		return readFromDatabase.getProfileImage(uuid);
	}

	public Image getUserProfileImage(UUID uuid) {

		return readFromDatabase.getUserProfileImage(uuid);
	}

	public Image getChatBackground() {

		return readFromDatabase.getChatBackground();
	}

	public String getContactStatus(UUID uuid) {
		return readFromDatabase.getContactStatus(uuid);
	}

	public static Connection getConnection() {

		return con;
	}

}
