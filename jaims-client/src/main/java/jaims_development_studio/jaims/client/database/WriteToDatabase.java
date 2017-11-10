package jaims_development_studio.jaims.client.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import jaims_development_studio.jaims.client.chatObjects.Message;

public class WriteToDatabase implements Runnable{
	
	String tablename;
	Connection con;
	PreparedStatement ps;
	
	public WriteToDatabase(String tablename, Connection con) {
		this.tablename = tablename;
		this.con = con;
		//CREATE TABLE BU88LE(
		//USER_ID UUID NOT NULL PRIMARY KEY,
		//NICKNAME VARCHAR(256) NOT NULL,
		//DESCRIPTION VARCHAR(4096),
		//STATUS VARCHAR(4096),
		//BYTE_ARRAY BLOB,
		//LAST_UPDATED TIMESTAMP NOT NULL,
		//MESSAGE_OBJECTS ARRAY);

	}

	private void write() {
		//try {
			///con.setAutoCommit(false);
			UUID user = UUID.randomUUID();
			UUID user1 = UUID.randomUUID();
			UUID user2 = UUID.randomUUID();
			UUID user3 = UUID.randomUUID();
			ArrayList<Message> list1 = new ArrayList<>();
			ArrayList<Message> list2 = new ArrayList<>();
			ArrayList<Message> list3 = new ArrayList<>();
			list1.add(new Message(user1, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list1.add(new Message(user, user1, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list1.add(new Message(user1, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list2.add(new Message(user2, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list2.add(new Message(user, user2, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list2.add(new Message(user2, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list3.add(new Message(user3, user, "Nachricht 1 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list3.add(new Message(user, user3, "Nachricht 2 von User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			list3.add(new Message(user3, user, "Nachricht 3 an User", new Timestamp(System.currentTimeMillis()-100000), new Timestamp(System.currentTimeMillis())));
			
//			
//			ps = con.prepareStatement("TRUNCATE TABLE BU88LE");
//			ps.executeUpdate();
//			con.commit();
//			
//			ps = con.prepareStatement("INSERT INTO BU88LE (USER_ID, NICKNAME, DESCRIPTION, STATUS, BYTE_ARRAY, LAST_UPDATED, MESSAGE_OBJECTS) VALUES(?,?,?,?,?,?,?)");
//			ps.setObject(1, UUID.randomUUID());
//			ps.setString(2, "Echo-Test");
//			ps.setString(3, "Das ist die Beschreibung");
//			ps.setString(4, "Das ist der Status");
//			ps.setBytes(5, IOUtils.toByteArray(new FileInputStream("G:/Bilder/IMG_3731.jpg")));
//			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//			ps.setArray(7, null);
//			ps.executeUpdate();
//			con.commit();
//			
//			ps = con.prepareStatement("INSERT INTO BU88LE (USER_ID, NICKNAME, DESCRIPTION, STATUS, BYTE_ARRAY, LAST_UPDATED) VALUES(?,?,?,?,?,?)");
//			ps.setObject(1, UUID.randomUUID());
//			ps.setString(2, "Leo");
//			ps.setString(3, "Das ist die Beschreibung");
//			ps.setString(4, "Das ist der Status");
//			ps.setBytes(5, IOUtils.toByteArray(new FileInputStream("G:/Bilder/Assassin's Creed/wallpaper_9.jpg")));
//			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//			//String[] arr = list2.toArray(new String[0]);
//			//Array array = con.createArrayOf("VARCHAR", arr);
//			//ps.setArray(7, array);
//			ps.executeUpdate();
//			con.commit();
//			
//			ps = con.prepareStatement("INSERT INTO BU88LE (USER_ID, NICKNAME, DESCRIPTION, STATUS, BYTE_ARRAY, LAST_UPDATED, MESSAGE_OBJECTS) VALUES(?,?,?,?,?,?,?)");
//			ps.setObject(1, UUID.randomUUID());
//			ps.setString(2, "Sebi");
//			ps.setString(3, "Das ist die Beschreibung");
//			ps.setString(4, "Das ist der Status");
//			ps.setBytes(5, IOUtils.toByteArray(new FileInputStream("G:/Bilder/Handy-07.01.17/WhatsApp Images/IMG-20160805-WA0005.jpg")));
//			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//			ps.setArray(7, null);
//			ps.executeUpdate();
//			con.commit();
//			
//			System.out.println("Written to database");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	public void run() {
		write();
		
	}

	public static Thread writeToDatabase(String tablename, Connection con) {
		Thread thread = new Thread(new WriteToDatabase(tablename, con));
		thread.start();
		return thread;
	}
}
