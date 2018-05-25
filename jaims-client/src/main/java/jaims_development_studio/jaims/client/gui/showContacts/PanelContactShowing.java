package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelContactShowing extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private SimpleContact		simpleContact;
	private Image				img;
	private BufferedImage		output				= new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

	public PanelContactShowing(GUIMain guiMain, SimpleContact simpleContact) {

		this.simpleContact = simpleContact;

		img = guiMain.getProfileImage(simpleContact.getContactID()).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		setMinimumSize(new Dimension(200, 60));
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(200, 60));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(70, 0)));
		JLabel lbl = new JLabel(simpleContact.getContactNickname());
		lbl.setFont(new Font("Sans Serif", Font.BOLD, 16));
		add(lbl);
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				guiMain.addChatUser(simpleContact);

				guiMain.showParentPanel(
						guiMain.getMessagePanelManager().getChatPanelForUser(simpleContact.getContactID()),
						PanelContactShowing.this);

			}
		});
	}

	public UUID getPanelID() {

		return simpleContact.getContactID();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(130, 130, 130));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

		Graphics2D g2d = output.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fill(new RoundRectangle2D.Float(0, 0, 50, 50, 15, 15));
		g2d.setComposite(AlphaComposite.SrcAtop);
		g2d.drawImage(img, 0, 0, this);

		g2.drawImage(output, 13, 5, this);
	}

}
