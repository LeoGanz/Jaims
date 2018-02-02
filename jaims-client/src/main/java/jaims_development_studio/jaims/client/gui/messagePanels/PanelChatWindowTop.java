package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelChatWindowTop extends JPanel {

	private Image	profileImage;
	private GUIMain	guiMain;

	public PanelChatWindowTop(SimpleContact contactProfile, GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI(contactProfile);

	}

	private void initGUI(SimpleContact contactProfile) {

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new LineBorder(Color.black, 1));

		profileImage = scaleMaintainAspectRatio(guiMain.getProfileImage(contactProfile.getContactID()));

		JPanel p = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setClip(new RoundRectangle2D.Double(4, 4, 45, 45, 15, 15));
				g2d.drawImage(profileImage, 4, 4, 45, 45, this);
				g2d.setColor(Color.BLACK);
				g2d.setClip(0, 0, getWidth(), getHeight());
				g2d.setStroke(new BasicStroke(1.5F));
				g2d.drawRoundRect(3, 3, 45, 45, 15, 15);

				g2d.dispose();
			}

		};
		p.setPreferredSize(new Dimension(57, 52));
		add(p);
		// add(Box.createRigidArea(new Dimension(0, 15)));

		JLabel lblUsername = new JLabel(contactProfile.getContactNickname(), JLabel.LEFT);
		lblUsername.setFont(new Font("Calibri", Font.BOLD, 15));
		lblUsername.setPreferredSize(new Dimension(
				lblUsername.getFontMetrics(lblUsername.getFont()).stringWidth(contactProfile.getContactNickname()),
				lblUsername.getFontMetrics(lblUsername.getFont()).getHeight()));
		lblUsername.setMaximumSize(lblUsername.getPreferredSize());
		add(lblUsername);
		add(Box.createHorizontalGlue());
	}

	private Image scaleMaintainAspectRatio(Image image) {

		image.getHeight(this);
		image.getWidth(this);
		Image returnImg = image.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
		return returnImg;
	}

}
