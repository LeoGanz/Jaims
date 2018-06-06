package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelSelectNewContact extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private BufferedImage		output				= new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
	private Profile				userProfile;
	private Image				img, tickmark;
	private JPanel				pCheckbox;
	private boolean				inDatabase, requestPending;
	private GUIMain				guiMain;

	public PanelSelectNewContact(GUIMain guiMain, Profile userProfile, boolean inDatabase, boolean requestPending) {

		this.guiMain = guiMain;
		this.userProfile = userProfile;
		this.inDatabase = inDatabase;
		this.requestPending = requestPending;
		try {
			img = Toolkit.getDefaultToolkit().createImage(userProfile.getProfilePicture()).getScaledInstance(50, 50,
					Image.SCALE_SMOOTH);
		} catch (NullPointerException npe) {
			img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Jaims_User.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		}
		tickmark = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/tickmark_green.png"))
				.getScaledInstance(23, 23, Image.SCALE_SMOOTH);

		createCheckbox();
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new EmptyBorder(0, 0, 0, 15));
		setMinimumSize(new Dimension(200, 60));
		setPreferredSize(new Dimension(200, 60));
		setMaximumSize(new Dimension(200, 60));
		add(Box.createHorizontalGlue());
		add(pCheckbox);
	}

	private void createCheckbox() {

		pCheckbox = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(200, 200, 200));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

				if (inDatabase)
					g2.drawImage(tickmark, 1, 1, this);
				else if (requestPending) {
				}

			}
		};
		pCheckbox.setMinimumSize(new Dimension(25, 25));
		pCheckbox.setPreferredSize(pCheckbox.getMinimumSize());
		pCheckbox.setMaximumSize(pCheckbox.getMinimumSize());
		pCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pCheckbox.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (guiMain.hasEntry(userProfile.getUuid()) == false) {
					if (guiMain.saveProfile(userProfile)) {
						PanelSelectNewContact.this.inDatabase = true;
						repaint();
					}
				} else {
					if (guiMain.deleteProfile(userProfile.getUuid())) {
						PanelSelectNewContact.this.inDatabase = false;
						repaint();
					}
				}

			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(170, 170, 170));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

		Graphics2D g2d = output.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fill(new RoundRectangle2D.Float(0, 0, 50, 50, 15, 15));

		g2d.setComposite(AlphaComposite.SrcAtop);
		g2d.drawImage(img, 0, 0, this);
		g2.drawImage(output, 13, 5, this);
		g2.setFont(new Font("Sans Serif", Font.BOLD, 16));
		g2.setColor(Color.BLACK);
		int y = getHeight() / 2 + g2.getFontMetrics(g2.getFont()).getHeight() / 3;
		g2.drawString(userProfile.getNickname(), 70, y);

	}

}
