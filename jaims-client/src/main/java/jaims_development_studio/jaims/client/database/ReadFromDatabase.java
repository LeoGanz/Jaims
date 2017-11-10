package jaims_development_studio.jaims.client.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.chatObjects.Profile;

public class ReadFromDatabase implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReadFromDatabase.class);
	public static List<ChatObjects> chatObjectsList = Collections.synchronizedList(new ArrayList<ChatObjects>());
	static UUID user = UUID.randomUUID();
	
	
	String tablename;
	Connection con;
	ResultSet rs;
	Statement statement;

	public ReadFromDatabase(String tablename, Connection con) {
		this.tablename = tablename;
		this.con = con;
	}

	private void read() {		
		/*try {
			//Builds a connection to the database
			con = DriverManager.getConnection("jdbc:hsqldb:file:c:/database/", "root", "");
		} catch (SQLException e) {
			LOG.error("Failed to connect to database!", e);
		}
	    
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
					Profile pf = new Profile((UUID) rs.getObject(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBytes(5), convertToDate(rs.getTimestamp(6)));
					ChatObjects co = new ChatObjects(pf);
					co.setMessageObjectsArray((ArrayList<Message>) rs.getArray(7));
					chatObjectsList.add(co);
				}
				statement.close();
			} catch (SQLException e) {
				LOG.error("Failed to create statement or resultSet!", e);
			}
	    }*/
		
		try {
			UUID user1 = UUID.randomUUID();
			UUID user2 = UUID.randomUUID();
			UUID user3 = UUID.randomUUID();
			ArrayList<Message> list1 = new ArrayList<>();
			ArrayList<Message> list2 = new ArrayList<>();
			ArrayList<Message> list3 = new ArrayList<>();
			list1.add(new Message(user1, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis()-561423)));
			list1.add(new Message(user, user1, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-235645), new Timestamp(System.currentTimeMillis()-55413)));
			list1.add(new Message(user1, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-586105), new Timestamp(System.currentTimeMillis()-65148)));
			list2.add(new Message(user2, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-32654), new Timestamp(System.currentTimeMillis()-3214489)));
			list2.add(new Message(user, user2, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-665987), new Timestamp(System.currentTimeMillis()-54496987)));
			list2.add(new Message(user2, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-213456), new Timestamp(System.currentTimeMillis()-200)));
			list3.add(new Message(user3, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-2256), new Timestamp(System.currentTimeMillis())));
			list3.add(new Message(user, user3, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-1234879), new Timestamp(System.currentTimeMillis()-99456)));
			list3.add(new Message(user3, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-6549998), new Timestamp(System.currentTimeMillis()-623154)));
			list3.add(new Message(user3, user, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", new Timestamp(System.currentTimeMillis()-105943), new Timestamp(System.currentTimeMillis()-33648)));
			
			
			Profile pf  = new Profile(user1, "Echo-Test", "Test", "Test", IOUtils.toByteArray(new FileInputStream("G:/Bilder/IMG_3731.jpg")), new Date(System.currentTimeMillis()));
			ChatObjects co = new ChatObjects(pf);
			co.setMessageObjectsArray(list1);
			
			Profile pf2 = new Profile(user2, "Leo", "Test", "Test", IOUtils.toByteArray(new FileInputStream("G:/Bilder/Assassin's Creed/wallpaper_9.jpg")), new Date(System.currentTimeMillis()));
			ChatObjects co2 = new ChatObjects(pf2);
			co2.setMessageObjectsArray(list2);
			
			Profile pf3 = new Profile(user3, "Sebi", "Test", "Test", IOUtils.toByteArray(new FileInputStream("G:/Bilder/Handy-07.01.17/WhatsApp Images/IMG-20160805-WA0005.jpg")), new Date(System.currentTimeMillis()));
			ChatObjects co3 = new ChatObjects(pf3);
			co3.setMessageObjectsArray(list3);
			
			chatObjectsList.add(co);
			chatObjectsList.add(co2);
			chatObjectsList.add(co3);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
