package jaims_development_studio.jaims.client.gui;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ContactPanel {

	private static final Logger	LOG	= LoggerFactory.getLogger(ContactPanel.class);
	ChatObject					co;
	PanelChatMessages			pcm;
	PanelChat					pc;
	JaimsFrame					jf;
	ClientMain					cm;
	Panel						contact, chat = null;
	ContactPanel				cp	= this;

	public ContactPanel(ChatObject co, JaimsFrame jf, ClientMain cm) {

		this.co = co;
		this.jf = jf;
		this.cm = cm;
		initGUI(co.getProfileContact(), jf, cm);
	}

	private void initGUI(ClientProfile pf, JaimsFrame jf, ClientMain cm) {

		pcm = new PanelChatMessages(jf, pf, co, this);
		Thread thread = new Thread(pcm);
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pc = new PanelChat(pf, pcm, co, this);
		Thread thread2 = new Thread(pc);
		thread2.start();

		contact = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())),
				co.getProfileContact().getNickname());
		contact.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

				setPanel();
			}
		});

		if (messageListExists(co)) {
			chat = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())),
					co.getProfileContact().getNickname());
			chat.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent arg0) {

					setPanel();

				}
			});
		}
	}

	public void setPanel() {

		cm.setMessagePanel(pc);
	}

	private byte[] getPicture(ClientProfile up) {

		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(up.getProfilePic());
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

		return co.getList() != null;

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

		chat = new Panel(Toolkit.getDefaultToolkit().createImage(getPicture(co.getProfileContact())),
				co.getProfileContact().getNickname());
		chat.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

				setPanel();
			}
		});
	}

	public PanelChatMessages getPanelChatMessages() {

		return pcm;
	}
}
