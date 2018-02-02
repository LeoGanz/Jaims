package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelContactShowing extends JPanel {

	private GUIMain			guiMain;
	private SimpleContact	simpleContact;
	private Image			img;

	public PanelContactShowing(GUIMain guiMain, SimpleContact simpleContact) {

		this.simpleContact = simpleContact;

		img = guiMain.getProfileImage(simpleContact.getContactID()).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(250, 60));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);

		g2.setFont(new Font("Sans Serif", Font.BOLD, 15));
		int y = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 4;
		g2.setColor(Color.BLACK);
		g2.drawString(simpleContact.getContactNickname(), 95, y);

		g2.setClip(new java.awt.geom.RoundRectangle2D.Double(30, 5, 50, 50, 15, 15));
		g2.drawImage(img, 30, 5, this);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2F));
		g2.setClip(0, 0, getWidth(), getHeight());
		g2.drawRoundRect(29, 4, 51, 51, 15, 15);

	}

}