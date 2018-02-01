package jaims_development_studio.jaims.client.database;

import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class ReadFromDatabase implements Runnable {

	private static final Logger		LOG				= LoggerFactory.getLogger(ReadFromDatabase.class);
	public static List<ChatObject>	chatObjectsList	= Collections.synchronizedList(new ArrayList<ChatObject>());

	String							tablename;
	Connection						con;
	ResultSet						rs;
	Statement						statement;
	private PreparedStatement		pStatement;
	private boolean					messagesExist	= false, contactsExist = false, userExists = false,
			imageExists = false;

	public ReadFromDatabase(Connection con) {

		this.con = con;

		new WriteToDatabase(tablename, con);
	}

	public boolean hasTables() {

		try {
			// Gets all the tables available in the db
			rs = con.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while (rs.next()) {
				String tName = rs.getString("TABLE_NAME");
				if (tName != null && tName.equals("MESSAGES")) {
					messagesExist = true;
				} else if (tName != null && tName.equals("CONTACTS")) {
					contactsExist = true;
				} else if (tName != null && tName.equals("USER"))
					userExists = true;
				else if (tName != null && tName.equals("IMAGE"))
					imageExists = true;
			}
			if (messagesExist && contactsExist)
				return true;
			else
				return false;
		} catch (SQLException e) {
			LOG.error("Failed to create resultSet!", e);
			return false;
		}
	}

	public void createTables() {

		try {
			Statement s = con.createStatement();
			if (messagesExist == false) {
				s.execute(
						"CREATE TABLE MESSAGES (MESSAGE_ID UUID PRIMARY KEY NOT NULL, SENDER_ID UUID NOT NULL, RECIPIENT_ID UUID NOT NULL, MESSAGE_TYPE VARCHAR(128) NOT NULL, TIMESTAMP_DELIEVERED TIMESTAMP NOT NULL, TIMESTAMP_READ TIMESTAMP,TIMESTAMP_RECIEVED TIMESTAMP NOT NULL, MESSAGE_STRING VARCHAR(8192))");
				con.commit();
			}
			if (contactsExist == false) {
				s.execute(
						"CREATE TABLE CONTACTS (CONTACT_ID UUID PRIMARY KEY NOT NULL, NICKNAME VARCHAR(256), DESCRIPTION VARCHAR(4096), STATUS VARCHAR(4096), PROFILE_PICTURE BLOB, LAST_UPDATED TIMESTAMP NOT NULL, HAS_CHAT BOOLEAN NOT NULL)");
				con.commit();
			}
			if (userExists == false) {
				s.execute(
						"CREATE TABLE USER (USER_ID UUID PRIMARY KEY NOT NULL, NICKNAME VARCHAR(256), DESCRIPTION VARCHAR(4096), STATUS VARCHAR(4096), PROFILE_PICTURE BLOB, LAST_UPDATED TIMESTAMP NOT NULL)");
				con.commit();
			}
			if (imageExists == false) {
				s.execute("CREATE TABLE IMAGE (BACKGROUND_IMAGE BLOB)");
				con.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SimpleContact getUserContact(UUID uuid, String username, ClientMain cm) {

		try {
			pStatement = con.prepareStatement("SELECT * from USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			if (rs.next() && rs.getObject(1) != null) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2));
				return simpleContact;
			} else {
				Thread thread = new Thread() {
					@Override
					public void run() {

						cm.requestUserProfile(uuid);
					}
				};
				thread.start();
				return new SimpleContact(uuid, username);
			}

		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
			Thread thread = new Thread() {
				@Override
				public void run() {

					cm.requestUserProfile(uuid);
				}
			};
			thread.start();
			return new SimpleContact(uuid, username);
		}
	}

	public Date getUserLastUpdatedDate(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT LAST_UPDATED FROM USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			if (rs.getBytes(1) != null) {
				return convertToDate(rs.getTimestamp(1));
			} else {
				return convertToDate(new Timestamp(0L));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return convertToDate(new Timestamp(0L));
		}
	}

	public ArrayList<SimpleContact> getSimpleContacts() {

		ArrayList<SimpleContact> simpleContactList = new ArrayList<>();

		try {
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT * from CONTACTS");
			while (rs.next()) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2),
						rs.getBoolean(7));
				simpleContactList.add(simpleContact);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}
		return simpleContactList;
	}

	public ArrayList<SimpleContact> getSimpleChatContacts() {

		ArrayList<SimpleContact> getSimpleChatContact = new ArrayList<>();

		try {
			pStatement = con.prepareStatement("SELECT * from CONTACTS WHERE HAS_CHAT=?");
			pStatement.setBoolean(1, true);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				SimpleContact simpleContact = new SimpleContact((UUID) rs.getObject(1), rs.getString(2),
						rs.getBoolean(7));
				getSimpleChatContact.add(simpleContact);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		return getSimpleChatContact;
	}

	public ArrayList<Message> getContactMessages(UUID uuid) {

		ArrayList<Message> mList = new ArrayList<>();

		try {
			pStatement = con.prepareStatement("SELECT * from MESSAGES WHERE SENDER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				Message message = new Message((UUID) rs.getObject(1), (UUID) rs.getObject(2), (UUID) rs.getObject(3),
						rs.getString(4), convertToDate(rs.getTimestamp(5)), convertToDate(rs.getTimestamp(6)),
						convertToDate(rs.getTimestamp(7)), rs.getString(8));
				mList.add(message);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		try {
			pStatement = con.prepareStatement("SELECT * from MESSAGES WHERE RECIPIENT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				Message message = new Message((UUID) rs.getObject(1), (UUID) rs.getObject(2), (UUID) rs.getObject(3),
						rs.getString(4), convertToDate(rs.getTimestamp(5)), convertToDate(rs.getTimestamp(6)),
						convertToDate(rs.getTimestamp(7)), rs.getString(8));
				mList.add(message);
			}
			statement.close();
		} catch (SQLException e) {
			LOG.error("Failed to create statement or resultSet!", e);
		}

		mList.sort(new Comparator<Message>() {

			@Override
			public int compare(Message m1, Message m2) {

				if (m1.getRecieved().compareTo(m2.getRecieved()) > 0)
					return -1;
				else if (m1.getRecieved().compareTo(m2.getRecieved()) < 0)
					return 1;
				else
					return 0;
			}
		});

		return mList;
	}

	public Image getProfileImage(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT PROFILE_PICTURE FROM CONTACTS WHERE CONTACT_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();

			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
		}
	}

	public Image getUserProfileImage(UUID uuid) {

		try {
			pStatement = con.prepareStatement("SELECT PROFILE_PICTURE FROM USER WHERE USER_ID=?");
			pStatement.setObject(1, uuid);
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();
			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/Jaims_USER.png"));
		}
	}

	public Image getChatBackground() {

		try {
			pStatement = con.prepareStatement("SELECT BACKGROUND_IMAGE FROM IMAGE");
			rs = pStatement.executeQuery();
			con.commit();

			rs.next();
			if (rs.getBytes(1) != null) {
				return Toolkit.getDefaultToolkit().createImage(rs.getBytes(1));
			} else {
				return Toolkit.getDefaultToolkit()
						.createImage(getClass().getClassLoader().getResource("images/LoginBackground.png"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return Toolkit.getDefaultToolkit()
					.createImage(getClass().getClassLoader().getResource("images/LoginBackground.png"));
		}
	}

	private void read() {

		try {
			//
			Statement s = con.createStatement();
			s.execute("TRUNCATE TABLE BU88LE");
			con.commit();
			//
			// UUID user = ClientMain.userProfile.getUuid();
			// UUID user1 = UUID.randomUUID();
			// UUID user2 = UUID.randomUUID();
			// UUID user3 = UUID.randomUUID();
			// UUID user4 = UUID.randomUUID();
			// ArrayList<Message> list1 = new ArrayList<>();
			// ArrayList<Message> list2 = new ArrayList<>();
			// ArrayList<Message> list3 = new ArrayList<>();
			// list1.add(
			// new Message(user1, user, "Nachricht 1 an User", new
			// Timestamp(System.currentTimeMillis() - 100000),
			// new Timestamp(System.currentTimeMillis() - 561423), false));
			// list1.add(
			// new Message(user, user1, "Nachricht 2 von User", new
			// Timestamp(System.currentTimeMillis() - 235645),
			// new Timestamp(System.currentTimeMillis() - 55413), false));
			// list1.add(
			// new Message(user1, user, "Nachricht 3 an User", new
			// Timestamp(System.currentTimeMillis() - 586105),
			// new Timestamp(System.currentTimeMillis() - 65148), false));
			// list2.add(new Message(user2, user, "Nachricht 1 an User", new
			// Timestamp(System.currentTimeMillis() - 32654),
			// new Timestamp(System.currentTimeMillis() - 3214489), false));
			// list2.add(
			// new Message(user, user2, "Nachricht 2 von User", new
			// Timestamp(System.currentTimeMillis() - 665987),
			// new Timestamp(System.currentTimeMillis() - 54496987), false));
			// list2.add(
			// new Message(user2, user, "Nachricht 3 an User", new
			// Timestamp(System.currentTimeMillis() - 213456),
			// new Timestamp(System.currentTimeMillis() - 200), false));
			// list3.add(new Message(user3, user, "Nachricht 1 an User", new
			// Timestamp(System.currentTimeMillis() - 2256),
			// new Timestamp(System.currentTimeMillis()), false));
			// list3.add(new Message(user, user3, "Nachricht 2 von User",
			// new Timestamp(System.currentTimeMillis() - 1234879),
			// new Timestamp(System.currentTimeMillis() - 99456), false));
			// list3.add(
			// new Message(user3, user, "Nachricht 3 an User", new
			// Timestamp(System.currentTimeMillis() - 6549998),
			// new Timestamp(System.currentTimeMillis() - 623154), false));
			// /*
			// * list3.add(new Message(user3, user,
			// * "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam
			// numy
			// eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam
			// voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
			// ita
			// kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
			// rem
			// ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
			// tempor invidunt ut labore et dolore magna aliquyam erat, sed diam
			// luptua.
			// At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd
			// gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
			// * , new Timestamp(System.currentTimeMillis() - 105943), new
			// * Timestamp(System.currentTimeMillis() - 33648), false));
			// */
			//
			// try {
			// PreparedStatement ps = con.prepareStatement("INSERT INTO " +
			// tablename.toUpperCase()
			// +
			// ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY)
			// VALUES (?,?,?,?,?,?,?)");
			// ps.setObject(1, user1);
			// ps.setString(2, "Echo-Test");
			// ps.setString(3, "Test");
			// ps.setString(4, "Test");
			// ps.setBytes(5,
			//
			// IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
			// ps.setTimestamp(6, new Timestamp(new
			// Date(System.currentTimeMillis()).getTime()));
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(bos);
			// oos.writeObject(new ArrayList<Message>());
			// ps.setBytes(7, bos.toByteArray());
			// bos.close();
			// oos.close();
			//
			// ps.executeUpdate();
			// con.commit();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// try {
			// PreparedStatement ps = con.prepareStatement("INSERT INTO " +
			// tablename.toUpperCase()
			// +
			// ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY)
			// VALUES (?,?,?,?,?,?,?)");
			// ps.setObject(1, user2);
			// ps.setString(2, "Sebi");
			// ps.setString(3, "Test");
			// ps.setString(4, "Test");
			// ps.setBytes(5,
			//
			// Utils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
			// ps.setTimestamp(6, new Timestamp(new
			// Date(System.currentTimeMillis()).getTime()));
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(bos);
			// oos.writeObject(list2);
			// ps.setBytes(7, bos.toByteArray());
			// bos.close();
			// oos.close();
			//
			// ps.executeUpdate();
			// con.commit();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// try {
			// PreparedStatement ps = con.prepareStatement("INSERT INTO " +
			// tablename.toUpperCase()
			// +
			// ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY)
			// VALUES (?,?,?,?,?,?,?)");
			// ps.setObject(1, user3);
			// ps.setString(2, "Leo");
			// ps.setString(3, "Test");
			// ps.setString(4, "Test");
			// ps.setBytes(5,
			//
			// Utils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
			// ps.setTimestamp(6, new Timestamp(new
			// Date(System.currentTimeMillis()).getTime()));
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(bos);
			// oos.writeObject(list3);
			// ps.setBytes(7, bos.toByteArray());
			// bos.close();
			// oos.close();
			//
			// ps.executeUpdate();
			// con.commit();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// try {
			// PreparedStatement ps = con.prepareStatement("INSERT INTO " +
			// tablename.toUpperCase()
			// +
			// "(ID,NICKNAME,DESCRIPTION,STATUS,PROFILE_PICTURE,TIMESTAMP,MESSAGE_ARRAY)
			// VALUES (?,?,?,?,?,?,?)");
			// ps.setObject(1, user4);
			// ps.setString(2, "Theresa");
			// ps.setString(3, "Test");
			// ps.setString(4, "Test");
			// ps.setBytes(5,
			//
			// IOUtils.toByteArray(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")));
			// ps.setTimestamp(6, new Timestamp(new
			// Date(System.currentTimeMillis()).getTime()));
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(bos);
			// oos.writeObject(new ArrayList<Message>());
			// ps.setBytes(7, bos.toByteArray());
			//
			// ps.executeUpdate();
			// con.commit();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }catch(
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {

		read();

	}

	private Date convertToDate(Timestamp ts) {

		long millis = ts.getTime();
		return new Date(millis);
	}

}
