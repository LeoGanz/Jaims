package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelChatWithUsers extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6014240532566432438L;
	private static final Logger	LOG					= LoggerFactory.getLogger(PanelChatWithUsers.class);
	ProfileImage				lbl;
	PanelContactsAndChats		jtp;

	List<ContactPanel>			panels;

	public PanelChatWithUsers(List<ContactPanel> panels) {

		this.panels = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ContactPanel cp : panels)
			this.panels.add(cp);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void initGUI() {

		try {
			Comparator<ContactPanel> comp = new Comparator<ContactPanel>() {

				@Override
				public int compare(ContactPanel o1, ContactPanel o2) {

					if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
							.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
									.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) > 0) {
						return 1;
					} else if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
							.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
									.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) < 0) {
						return -1;
					} else {
						return 0;
					}
				}

			};
			panels.sort(comp);
		} catch (NullPointerException e) {
			LOG.info("ContactPanel had no chat history");
		}
		for (ContactPanel panel : panels) {
			if (messageListExists(panel.getChatObject())) {
				Panel p = null;
				try {
					p = new Panel(
							ImageIO.read(
									new ByteArrayInputStream(getPicture(panel.getChatObject().getProfileContact()))),
							panel.getChatObject().getProfileContact().getNickname());
					p.addMouseListener(new MouseAdapter() {

						@Override
						public void mousePressed(MouseEvent e) {

							panel.setPanel();
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				add(p);
				add(new LinePanel());
				add(Box.createRigidArea(new Dimension(0, 5)));
			}
		}
	}

	@Override
	public void run() {

		initGUI();

	}

	public List<ContactPanel> getList() {

		return panels;
	}

	private ArrayList<Message> getMessageList(ChatObject co) {

		return co.getList();
	}

	private byte[] getPicture(ClientProfile pu) {

		ResultSet rs;
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(pu.getProfilePic());
			ps.setObject(1, pu.getUuid());
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
			ps = con.prepareStatement(
					"SELECT MESSAGE_ARRAY FROM " + ClientMain.userProfile.getNickname().toUpperCase() + " WHERE ID=?;");
			ps.setObject(1, co.getProfileContact().getUuid());
			rs = ps.executeQuery();
			con.commit();

			rs.next();

			if (rs.getBytes(1) != null)
				return true;
			else
				return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
