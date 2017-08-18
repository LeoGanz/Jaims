package jaims_development_studio.jaims.client.database;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.chatObjects.Profile;

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
	}

	private void read() {		
		try {
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
