package jaims_development_studio.jaims.client.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.networking.ServerConnection;

public class DatabaseManagement implements Runnable{
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseManagement.class);
	public static boolean tExists = false;
	
	
	String username;
	private Connection con;
	private Statement statement;
	private PreparedStatement pStatement;
	private ResultSet rs = null;
	private Profile profile;
	private List<Profile> profileList = Collections.synchronizedList(new ArrayList<Profile>());

	public DatabaseManagement(String username, String password) {
		this.username = username;
		
	}
	private boolean tableExists(String tableName) {
		try {
			//Builds a connection to the database
			con = DriverManager.getConnection("jdbc:hsqldb:file:c:/database/", "root", "");
		} catch (SQLException e) {
			LOG.error("Failed to connect to database!", e);
		}
		
	    tExists = false;
	    
	    try {
	    	//Gets all the tables available in the db
	    	rs = con.getMetaData().getTables(null, null, tableName.toUpperCase(), null);

	        while (rs.next()) { 
	            String tName = rs.getString(3);
	            if (tName != null && tName.equals(tableName.toUpperCase())) {
	                tExists = true;
	                break;
	            }
	        }
	    } catch (SQLException e) {
			LOG.error("Failed to create resultSet!", e);
		}
	    return tExists;
	}
	
	private void getProfiles(String username) {
		try {
			//gets all profile in the Database USERNAME
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * from " + username.toUpperCase());
			while (rs.next()) {
				//creates profiles with data from db
				profile = new Profile();
				profile.setUUID((UUID)rs.getObject(1));
				profile.setNickname(rs.getString(2));
				profile.setDescription(rs.getString(3));
				profile.setStatus(rs.getString(4));
				profile.setLastUpdated(convertTimestampToDate(rs.getTimestamp(5)));
				profile.setByteImage(rs.getBytes(6));
				profileList.add(profile);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}
		
	}
	
	private void getMessages(String username) {
		try {
			con.setAutoCommit(false);
			//Loops through profileList to get all messages
			for (int i = 0; i < profileList.size(); i++) {
				//Gets messages from message table
				pStatement = con.prepareStatement("SELECT * FROM " + username.toUpperCase() + "_MESSAGES WHERE SENDER=? OR RECIEVER=?");
				pStatement.setObject(1, profileList.get(i).getUUID());
				pStatement.setObject(2, profileList.get(i).getUUID());
				
				
				rs = pStatement.executeQuery();
				while(rs.next()) {
					//Creates a MessageObject with data from db
					MessageObject mo = new MessageObject();
					mo.setUUIDSender((UUID) rs.getObject(1));
					mo.setUUIDRecipient((UUID) rs.getObject(2));
					mo.setMessage(rs.getString(3));
					mo.setImage(rs.getBytes(4));
					mo.setTimeDelievered(convertTimestampToDate(rs.getTimestamp(5)));
					//mo.setTImeRead(convertTimestampToDate(rs.getTimestamp(6)));
					//add messageObject to profiles' messageObjectList
					profileList.get(i).addMessage(mo);
				}
			}
			pStatement.close();
			rs.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}		
	}
	
	
	
	private void createTable(String username) {
		try {
			statement = con.createStatement();
			statement.executeQuery("CREATE TABLE " + username + " (USER_ID UUID NOT NULL PRIMARY KEY, NICKNAME VARCHAR(256) NOT NULL, DESCRIPTION VARCHAR(4096), STATUS VARCHAR(4096), TIME TIMESTAMP NOT NULL, BYTE_IMAGE BLOB);");
			statement.execute("CREATE TABLE "+ username.toUpperCase() + "_MESSAGES (SENDER UUID NOT NULL PRIMARY KEY, RECIEVER UUID NOT NULL, MESSAGE VARCHAR(8192),BYTE_IMAGE BLOB, TIME_DELIEVERED TIMESTAMP NOT NULL, TIME_READ TIMESTAMP);");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Date convertTimestampToDate(Timestamp ts) {
		Date d = new Date(ts.getTime());
		return d;
	}
	
	public List<Profile> getProfileList() {
		return profileList;
	}
	
	
	@Override
	public void run() {
		if (tableExists(username) == true) {
			//createTable(username);
			//setExample();
			getProfiles(username);
			getMessages(username);
		}		
	}
	
	
	public void setExample() {
		UUID userProfile =  UUID.randomUUID();
		UUID sender = UUID.randomUUID();
		UUID sender2 = UUID.randomUUID();
		UUID sender3 = UUID.randomUUID();
		
		
		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:c:/database/", "root", "");
			con.setAutoCommit(false);
			
			statement = con.createStatement();
			statement.executeQuery("TRUNCATE TABLE BU88LE");
			
			statement = con.createStatement();
			statement.executeQuery("TRUNCATE TABLE BU88LE_MESSAGES");	
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE VALUES (?,?,?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setString(2, "Echo-Test");
			pStatement.setString(3, "Nur ein Test");
			pStatement.setString(4, "Das ist ein Status :O");
			pStatement.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			byte[] img = IOUtils.toByteArray(this.getClass().getClassLoader().getResource("images/JAIMS_PENGUIN.png"));
			pStatement.setBytes(6, img);
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Das ist nur ein Versuch das JPanel in der richtigen Größe darzustellen");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile);
			pStatement.setObject(2, sender);
			pStatement.setString(3, "Beispiel-Nachricht 2");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Das hier sollte der Sender verschickt haben; es müsste also auf der linken Seite angezeigt werden...");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Theresa denkt, dass Josef auf Kathrin steht... mittlerweile hat sie es auch geschafft Annika von dieser Idee zu überzeugen, die eigentlich auch was von ihm will");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile);
			pStatement.setObject(2, sender);
			pStatement.setString(3, "Tut er das denn ? Also steht er auf sie ?");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Wenn man das nur wüsste ^^");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE VALUES (?,?,?,?,?,?)");
			pStatement.setObject(1, sender3);
			pStatement.setString(2, "Sebi");
			pStatement.setString(3, "Nur ein Test");
			pStatement.setString(4, "Das ist ein Status :O");
			pStatement.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			img = IOUtils.toByteArray(this.getClass().getClassLoader().getResource("images/JAIMS_PENGUIN.png"));
			pStatement.setBytes(6, img);
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile);
			pStatement.setObject(2, sender3);
			pStatement.setString(3, "Hi Sebi, ich wollt dich hier auch mal anschreiben ^^");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender3);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Oh nein, nicht der schon wieder :P");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE VALUES (?,?,?,?,?,?)");
			pStatement.setObject(1, sender2);
			pStatement.setString(2, "Leo");
			pStatement.setString(3, "Nur ein Test");
			pStatement.setString(4, "Das ist ein Status :O");
			pStatement.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			img = IOUtils.toByteArray(this.getClass().getClassLoader().getResource("images/JAIMS_PENGUIN.png"));
			pStatement.setBytes(6, img);
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender2);
			pStatement.setObject(2, userProfile);
			pStatement.setString(3, "Hey Josef, hier ist Leo Ganz");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile);
			pStatement.setObject(2, sender2);
			pStatement.setString(3, "Hi Leo :)");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
		}catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
