package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.util.UUID;

import javax.swing.JPanel;
import javax.swing.Timer;

import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelContactShowing extends JPanel {

	private GUIMain			guiMain;
	private SimpleContact	simpleContact;
	private Profile			userProfile;
	private Image			img, tickmark;
	private boolean			paintAnimation	= false, allowAnimationPainting = true, inDatabase = false, onAddUser;
	private int				xStart, yStart, width, height;

	public PanelContactShowing(GUIMain guiMain, SimpleContact simpleContact) {

		this.simpleContact = simpleContact;

		img = guiMain.getProfileImage(simpleContact.getContactID()).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		setMinimumSize(new Dimension(200, 60));
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(200, 60));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (simpleContact.chatExists() == false)
					guiMain.addChatUser(simpleContact);

				guiMain.showParentPanel(
						guiMain.getMessagePanelManager().getChatPanelForUser(simpleContact.getContactID()),
						PanelContactShowing.this);

			}
		});
	}

	public PanelContactShowing(GUIMain guiMain, Profile userProfile, boolean inDatabase) {

		this.userProfile = userProfile;
		this.inDatabase = inDatabase;

		allowAnimationPainting = false;

		try {
			img = Toolkit.getDefaultToolkit().createImage(userProfile.getProfilePicture()).getScaledInstance(50, 50,
					Image.SCALE_SMOOTH);
		} catch (NullPointerException npe) {
			img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_User.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		}

		tickmark = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/tickmark_green.png"))
				.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		setMinimumSize(new Dimension(200, 60));
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(200, 60));

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (onAddUser) {
					if (guiMain.hasEntry(userProfile.getUuid()) == false) {
						if (guiMain.saveProfile(userProfile)) {
							PanelContactShowing.this.inDatabase = true;
							revalidate();
							repaint();
						}
					} else {
						if (guiMain.deleteProfile(userProfile.getUuid())) {
							PanelContactShowing.this.inDatabase = false;
							revalidate();
							repaint();
						}
					}
				}

			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if (e.getX() >= 155 && e.getY() >= 10 && e.getX() <= 1955 && e.getY() <= 50) {
					onAddUser = true;
					setCursor(new Cursor(Cursor.HAND_CURSOR));
					repaint();
				} else {
					onAddUser = false;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

			}

		});
	}

	public void paintAnimation() {

		if (paintAnimation == false && allowAnimationPainting) {
			paintAnimation = true;
			xStart = getWidth() / 2;
			yStart = getHeight() / 2;
			Timer t = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (xStart >= 0) {
						xStart -= 20;
						width += 40;
					}

					if (yStart >= 0) {
						yStart -= 6;
						height += 12;
					}

					repaint();

					if (xStart == 0)
						((Timer) e.getSource()).stop();

				}
			});
			t.start();
		}
	}

	public void removeAnimation() {

		if (paintAnimation && allowAnimationPainting) {
			paintAnimation = false;
			Timer t = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (xStart <= ((getWidth() / 2) - 20)) {
						xStart += 20;
						width -= 40;
					}

					if (yStart <= ((getHeight() / 2) - 6)) {
						yStart += 6;
						height -= 12;
					}

					repaint();

					if (xStart == (getWidth() / 2))
						((Timer) e.getSource()).stop();

				}
			});
			t.start();
		}
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
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
		if (paintAnimation) {
			g2.setColor(Color.GRAY);
			g2.fillRoundRect(xStart, yStart, width, height, 60, 60);
		}

		g2.setFont(new Font("Sans Serif", Font.BOLD, 15));
		int y = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 4;
		g2.setColor(Color.BLACK);
		if (allowAnimationPainting)
			g2.drawString(simpleContact.getContactNickname(), 75, y);
		else
			g2.drawString(userProfile.getNickname(), 75, y);

		g2.setClip(new java.awt.geom.RoundRectangle2D.Double(18, 5, 50, 50, 15, 15));
		g2.drawImage(img, 18, 5, this);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2F));
		g2.setClip(0, 0, getWidth(), getHeight());
		g2.drawRoundRect(17, 4, 51, 51, 15, 15);

		if (allowAnimationPainting == false)
			g2.drawOval(getWidth() - 45, 10, 40, 40);

		if (inDatabase) {
			g2.setClip(new RoundRectangle2D.Double(getWidth() - 44, 14, 38, 38, 32, 32));
			g2.drawImage(tickmark, getWidth() - 44, 14, this);
		}

	}

}
