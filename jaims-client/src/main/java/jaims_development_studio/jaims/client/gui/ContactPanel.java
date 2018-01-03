package jaims_development_studio.jaims.client.gui;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ContactPanel{
	
	private static final Logger LOG = LoggerFactory.getLogger(ContactPanel.class);
	ChatObject co;
	PanelChatMessages pcm;
	PanelChat pc;
	JaimsFrame jf;
	ClientMain cm;
	Panel contact,chat=null;
	ContactPanel cp = this;
	
	
	public ContactPanel(ChatObject co, JaimsFrame jf, ClientMain cm) {
		this.co = co;
		this.jf = jf;
		this.cm = cm;
		initGUI(co.getProfileContact(), jf, cm);
	}
	
	private void initGUI(Profile pf, JaimsFrame jf, ClientMain cm) {
		
		pcm = new PanelChatMessages(jf, pf, co, this);
		Thread thread = new Thread(pcm);
		thread.start();
		
		pc = new PanelChat(pf, pcm, co, this);
		Thread thread2 = new Thread(pc);
		thread2.start();
		
		contact = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())), co.getProfileContact().getNickname());
		contact.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				setPanel(cp);
			}
		});
		
		if (messageListExists(co)) {
			chat = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())), co.getProfileContact().getNickname());
			chat.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					setPanel(cp);
				}
			});
		}
}
	
	public void setPanel(ContactPanel cp) {
		
		cm.setAcvtiveContactPanel(cp);
		cm.setMessagePanel(pc);
	}
	
	private byte[] getPicture(Profile up) {
		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(up.getProfilePicture());
			ps.setObject(1, up.getUuid());
			rs = ps.executeQuery();
			con.commit();
			
			rs.next();
			
			return rs.getBytes(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private boolean messageListExists(ChatObject co) {
		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(co.getList());
			ps.setObject(1, co.getProfileContact().getUuid());
			rs = ps.executeQuery();
			con.commit();
			
			rs.next();
			
			if (rs.getBytes(1) != null) {
				if (getMessageList(co).size() >= 1)
					return true;
				else
					return false;
			}else
				return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	public ChatObject getChatObject() {
		return co;
	}
	
	public ClientMain getClientMain() {
		return cm;
	}
	
	public Panel getContactPanel() {
		return contact;
	}
	
	public boolean chatExists() {
		if (chat == null)
			return false;
		else
			return true;
	}
	
	public Panel getChatPanel() {
		return chat;
	}
	
	public void createChatPanel() {
		chat = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())), co.getProfileContact().getNickname());
		chat.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				setPanel(cp);
			}
		});
	}
	
	private ArrayList<Message> getMessageList(ChatObject co) {
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
}
