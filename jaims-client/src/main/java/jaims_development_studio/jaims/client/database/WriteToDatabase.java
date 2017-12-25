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

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
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

	public void writeMessage(Message m,  UUID uuid, ChatObjects co, ArrayList<Message> list) {

			try {
				m.setTimestampSent(new Date(System.currentTimeMillis()));
				list.add(m);
				
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
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
	@Override
	public void run() {
		
	}
	
	private ArrayList<Message> getMessageList(ChatObjects co) {
		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		ArrayList<Message> listCo = null;
		try {
			ps = con.prepareStatement(co.getList());
			ps.setObject(1, co.getProfileContact().getUuid());
			rs = ps.executeQuery();
			con.commit();
			
			rs.next();
			ByteArrayInputStream bis = new ByteArrayInputStream(new byte[rs.getBytes(1).length]);
			bis.read(rs.getBytes(1));
			System.out.println(bis == null);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)));
			listCo = (ArrayList<Message>) ois.readObject();
			
			return listCo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static Thread writeToDatabase(String tablename, Connection con) {
		Thread thread = new Thread(new WriteToDatabase(tablename, con));
		thread.start();
		return thread;
	}
}
