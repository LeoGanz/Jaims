package jaims_development_studio.jaims.client.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import jaims_development_studio.jaims.client.chatObjects.Message;

public class WriteToDatabase implements Runnable{
	
	static String tablename;
	static Connection con;
	static Statement s;
	static PreparedStatement ps;
	static ResultSet rs;
	
	public WriteToDatabase(String tablename, Connection con) {
		WriteToDatabase.tablename = tablename;
		WriteToDatabase.con = con;
	}

	public static void writeMessage(Message m,  UUID uuid) {

			try {
				ps = con.prepareStatement("SELECT * FROM " + tablename.toUpperCase() + " WHERE ID=?");
				ps.setObject(1, uuid);
				rs = ps.executeQuery();
				
				rs.next();
				System.out.println(uuid);
				System.out.println((UUID) rs.getObject(1));
				byte[] arr = rs.getBytes(7);
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(arr));
				ArrayList<Message> list = (ArrayList<Message>) ois.readObject();
				m.setTimestampSent(new Date(System.currentTimeMillis()));
				list.add(m);
				ps.close();
				
				ps = con.prepareStatement("UPDATE " + tablename.toUpperCase() + " SET MESSAGE_ARRAY=? WHERE ID=?");
		
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(list);
				ps.setBytes(1, bos.toByteArray());
				ps.setObject(2, uuid);
				bos.close();
				oos.close();
				
				ps.executeUpdate();
				con.commit();
			} catch (SQLException | IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
		
	}

	public static Thread writeToDatabase(String tablename, Connection con) {
		Thread thread = new Thread(new WriteToDatabase(tablename, con));
		thread.start();
		return thread;
	}
}
