package jaims_development_studio.jaims.client.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.Message;

public class WriteToDatabase {

	private static final Logger	LOG	= LoggerFactory.getLogger(WriteToDatabase.class);

	private String				tablename;
	private Connection			con;
	private Statement			s;
	private PreparedStatement	ps;
	private ResultSet			rs;

	public WriteToDatabase(String tablename, Connection con) {

		this.tablename = tablename;
		this.con = con;
	}

	public void writeMessage(Message m, UUID uuid, ArrayList<Message> list) {

		try {
			// m.setTimestampSent(new Date(System.currentTimeMillis()));
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
			LOG.error("Caught SQL exception", e);
		}
	}
}
