package jaims_development_studio.jaims.client.database;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ReadFromDatabase implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReadFromDatabase.class);
	public static List<ChatObjects> chatObjectsList = Collections.synchronizedList(new ArrayList<ChatObjects>());
	
	
	String tablename;
	Connection con;
	ResultSet rs;
	Statement statement;

	public ReadFromDatabase(String tablename, Connection con) {
		this.tablename = tablename;
		this.con = con;
		
		new WriteToDatabase(tablename, con);
	}

	private void read() {			    
		boolean tableExists = false;
		
	    try {
	    	//Gets all the tables available in the db
	    	rs = con.getMetaData().getTables(null, null, tablename.toUpperCase(), null);

	        while (rs.next()) { 
	            String tName = rs.getString(3);
	            if (tName != null && tName.equals(tablename.toUpperCase())) {
	            	tableExists = true;
	                break;
	            }
	        }
	    } catch (SQLException e) {
			LOG.error("Failed to create resultSet!", e);
		}
	    
	    if (tableExists) {
	    	try {
				//gets all profile in the Database USERNAME
				statement = con.createStatement();
				rs = statement.executeQuery("SELECT * from " + tablename.toUpperCase());
				while (rs.next()) {
					//creates profiles with data from db
					Profile pf = new Profile();
					pf.setUUID((UUID) rs.getObject(1));
					pf.setNickname(rs.getString(2));
					pf.setDescription(rs.getString(3));
					pf.setStatus(rs.getString(4));
					pf.setLastUpdated(convertToDate(rs.getTimestamp(6)));
					pf.setProfilePicture("SELECT PROFILE_PICTURE FROM " + tablename.toUpperCase() + " WHERE ID=?;");
					ChatObjects co = new ChatObjects(pf);
					co.setMessageObjectsArray("SELECT MESSAGE_ARRAY FROM " + tablename.toUpperCase() + " WHERE ID=?;");
					chatObjectsList.add(co);
				}
				statement.close();
			} catch (SQLException e) {
				LOG.error("Failed to create statement or resultSet!", e);
			}
	    }else {
	    	try {
	    		statement = con.createStatement();
				rs = statement.executeQuery("CREATE TABLE " + tablename.toUpperCase() +  "(ID UUID PRIMARY KEY NOT NULL,NICKNAME VARCHAR(256) NOT NULL,DESCRIPTION VARCHAR(4096),STATUS VARCHAR(2048),PROFILE_PICTURE BLOB,TIMESTAMP TIMESTAMP NOT NULL,MESSAGE_ARRAY BLOB)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
		
//		try {
//			
//			Statement s = con.createStatement();
//			s.execute("TRUNCATE TABLE BU88LE");
//			con.commit();
//			
//			UUID user = ClientMain.userProfile.getUuid();
//			UUID user1 = UUID.randomUUID();
//			UUID user2 = UUID.randomUUID();
//			UUID user3 = UUID.randomUUID();
//			UUID user4 = UUID.randomUUID();
//			ArrayList<Message> list1 = new ArrayList<>();
//			ArrayList<Message> list2 = new ArrayList<>();
//			ArrayList<Message> list3 = new ArrayList<>();
//			list1.add(new Message(user1, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis()-561423), false));
//			list1.add(new Message(user, user1, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-235645), new Timestamp(System.currentTimeMillis()-55413), false));
//			list1.add(new Message(user1, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-586105), new Timestamp(System.currentTimeMillis()-65148), false));
//			list2.add(new Message(user2, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-32654), new Timestamp(System.currentTimeMillis()-3214489), false));
//			list2.add(new Message(user, user2, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-665987), new Timestamp(System.currentTimeMillis()-54496987), false));
//			list2.add(new Message(user2, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-213456), new Timestamp(System.currentTimeMillis()-200), false));
//			list3.add(new Message(user3, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-2256), new Timestamp(System.currentTimeMillis()), false));
//			list3.add(new Message(user, user3, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-1234879), new Timestamp(System.currentTimeMillis()-99456), false));
//			list3.add(new Message(user3, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-6549998), new Timestamp(System.currentTimeMillis()-623154), false));
//			list3.add(new Message(user3, user, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", new Timestamp(System.currentTimeMillis()-105943), new Timestamp(System.currentTimeMillis()-33648), false));
//		
//			
//			try {
//				PreparedStatement ps = con.prepareStatement("INSERT INTO " + tablename.toUpperCase() + "(ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY) VALUES (?,?,?,?,?,?,?)");
//				ps.setObject(1, user1);
//				ps.setString(2, "Echo-Test");
//				ps.setString(3,  "Test");
//				ps.setString(4, "Test");
//				ps.setBytes(5, IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
//				ps.setTimestamp(6, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(bos);
//				oos.writeObject(list1);
//				ps.setBytes(7, null);
//				bos.close();
//				oos.close();
//				
//				ps.executeUpdate();
//				con.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				PreparedStatement ps = con.prepareStatement("INSERT INTO " + tablename.toUpperCase() + "(ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY) VALUES (?,?,?,?,?,?,?)");
//				ps.setObject(1, user2);
//				ps.setString(2, "Sebi");
//				ps.setString(3,  "Test");
//				ps.setString(4, "Test");
//				ps.setBytes(5, IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
//				ps.setTimestamp(6, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(bos);
//				oos.writeObject(list2);
//				ps.setBytes(7, bos.toByteArray());
//				bos.close();
//				oos.close();
//				
//				ps.executeUpdate();
//				con.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				PreparedStatement ps = con.prepareStatement("INSERT INTO " + tablename.toUpperCase() + "(ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY) VALUES (?,?,?,?,?,?,?)");
//				ps.setObject(1, user3);
//				ps.setString(2, "Leo");
//				ps.setString(3,  "Test");
//				ps.setString(4, "Test");
//				ps.setBytes(5, IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
//				ps.setTimestamp(6, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(bos);
//				oos.writeObject(list3);
//				ps.setBytes(7, bos.toByteArray());
//				bos.close();
//				oos.close();
//				
//				ps.executeUpdate();
//				con.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				PreparedStatement ps = con.prepareStatement("INSERT INTO " + tablename.toUpperCase() + "(ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY) VALUES (?,?,?,?,?,?,?)");
//				ps.setObject(1, user4);
//				ps.setString(2, "Theresa");
//				ps.setString(3,  "Test");
//				ps.setString(4, "Test");
//				ps.setBytes(5, IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
//				ps.setTimestamp(6, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
//				ps.setBytes(7, null);
//				
//				ps.executeUpdate();
//				con.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}

	@Override
	public void run() {
		read();

	}

	public static Thread readDatabase(String tablename, Connection con) {
		Thread thread = new Thread(new ReadFromDatabase(tablename, con));
		thread.start();

		return thread;
	}
	
	private Date convertToDate(Timestamp ts) {
		long millis = ts.getTime();
		return new Date(millis);
	}

}
