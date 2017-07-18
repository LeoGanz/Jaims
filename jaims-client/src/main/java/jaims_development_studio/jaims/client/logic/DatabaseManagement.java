package jaims_development_studio.jaims.client.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import jaims_development_studio.jaims.client.gui.MainFrame;

public class DatabaseManagement implements Runnable{
	
	String username;
	private Connection con;
	private Statement statement;
	private PreparedStatement pStatement;
	private ResultSet rs = null;
	private Profile profile, userProfile;
	private List<Profile> profileList = Collections.synchronizedList(new ArrayList<Profile>());
	private MainFrame mf;
	private MessageObject messageObject;
	
	public DatabaseManagement(MainFrame mf, Profile userProfile) {
		this.mf = mf;
		this.userProfile = userProfile;
	}
	private boolean tableExist(String tableName) {
		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:c:/database/", "root", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    boolean tExists = false;
	    
	    try {
	    	rs = con.getMetaData().getTables(null, null, tableName.toUpperCase(), null);

	        while (rs.next()) { 
	            String tName = rs.getString(3);
	            if (tName != null && tName.equals(tableName.toUpperCase())) {
	                tExists = true;
	                break;
	            }
	        }
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return tExists;
	}
	
	private void getProfiles() {
		try {
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * from " + userProfile.getNickname().toUpperCase());
			while (rs.next()) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getMessages() {
		try {
			con.setAutoCommit(false);
			for (int i = 0; i < profileList.size(); i++) {
				pStatement = con.prepareStatement("SELECT * FROM " + userProfile.getNickname().toUpperCase() + "_MESSAGES WHERE SENDER=? OR RECIEVER=?");
				pStatement.setObject(1, profileList.get(i).getUUID());
				pStatement.setObject(2, profileList.get(i).getUUID());
				
				
				rs = pStatement.executeQuery();
				while(rs.next()) {
					MessageObject mo = new MessageObject();
					mo.setUUIDSender((UUID) rs.getObject(1));
					mo.setUUIDRecipient((UUID) rs.getObject(2));
					mo.setMessage(rs.getString(3));
					mo.setImage(rs.getBytes(4));
					mo.setTimeDelievered(convertTimestampToDate(rs.getTimestamp(5)));
					//mo.setTImeRead(convertTimestampToDate(rs.getTimestamp(6)));
					profileList.get(i).addMessage(mo);
				}
			}
			pStatement.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void setExample() {
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
			pStatement.setObject(2, userProfile.getUUID());
			pStatement.setString(3, "Das ist nur ein Versuch das JPanel in der richtigen Größe darzustellen");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile.getUUID());
			pStatement.setObject(2, sender);
			pStatement.setString(3, "Beispiel-Nachricht 2");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile.getUUID());
			pStatement.setString(3, "Das hier sollte der Sender verschickt haben; es müsste also auf der linken Seite angezeigt werden...");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile.getUUID());
			pStatement.setString(3, "Theresa denkt, dass Josef auf Kathrin steht... mittlerweile hat sie es auch geschafft Annika von dieser Idee zu überzeugen, die eigentlich auch was von ihm will");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile.getUUID());
			pStatement.setObject(2, sender);
			pStatement.setString(3, "Tut er das denn ? Also steht er auf sie ?");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender);
			pStatement.setObject(2, userProfile.getUUID());
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
			pStatement.setObject(1, userProfile.getUUID());
			pStatement.setObject(2, sender3);
			pStatement.setString(3, "Hi Sebi, ich wollt dich hier auch mal anschreiben ^^");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES(?,?,?,?)");
			pStatement.setObject(1, sender3);
			pStatement.setObject(2, userProfile.getUUID());
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
			pStatement.setObject(2, userProfile.getUUID());
			pStatement.setString(3, "Hey Josef, hier ist Leo Ganz");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
			pStatement = con.prepareStatement("INSERT INTO BU88LE_MESSAGES (SENDER, RECIEVER, MESSAGE, TIME_DELIEVERED) VALUES (?,?,?,?)");
			pStatement.setObject(1, userProfile.getUUID());
			pStatement.setObject(2, sender2);
			pStatement.setString(3, "Hi Leo :)");
			pStatement.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.executeUpdate();
			con.commit();
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void load(String username) {
		if (tableExist(username)) {
			getProfiles();
			getMessages();
		}else {
			createTable(username);
		}
		
	}
	@Override
	public void run() {
		load(userProfile.getNickname());
		
	}
	
	private Date convertTimestampToDate(Timestamp ts) {
		Date d = new Date(ts.getTime());
		return d;
	}
	
	public List<Profile> getProfileList() {
		return profileList;
	}
}
