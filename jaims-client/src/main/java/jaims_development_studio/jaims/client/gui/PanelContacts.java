package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Graphics;
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

import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;

public class PanelContacts extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(PanelContacts.class);

	PanelContactsAndChats		pcc;

	List<ContactPanel>			panels;

	public PanelContacts(List<ContactPanel> list) {

		panels = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ContactPanel cp : list) {
			panels.add(cp);
		}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void initGUI() {

		Comparator<ContactPanel> comp = new Comparator<ContactPanel>() {

			@Override
			public int compare(ContactPanel o1, ContactPanel o2) {

				if (o1.getChatObject().getProfileContact().getNickname()
						.compareTo(o2.getChatObject().getProfileContact().getNickname()) > 0) {
					return 1;
				} else if (o1.getChatObject().getProfileContact().getNickname()
						.compareTo(o2.getChatObject().getProfileContact().getNickname()) < 0) {
					return -1;
				} else {
					return 0;
				}
			}

		};
		panels.sort(comp);
		for (int i = 0; i < panels.size(); i++) {
			try {
				Panel p;
				add(p = new Panel(
						ImageIO.read(new ByteArrayInputStream(
								getPicture(panels.get(i).getChatObject().getProfileContact()))),
						panels.get(i).getChatObject().getProfileContact().getNickname()));
				int z = i;
				p.addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {

						panels.get(z).setPanel();
					}
				});
				add(new LinePanel());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(Box.createRigidArea(new Dimension(0, 5)));
		}
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

	@Override
	public void run() {

		initGUI();

	}

	@Override
	public void paintComponent(Graphics g) {

	}

}
