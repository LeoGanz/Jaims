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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

/**
 * This class represents a
 * <code>JPanel</cod> that shows a contact's profile picture and username and is added to the top of the <code>PanelChat</code>.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class PanelChatWindowTop extends JPanel {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	private Image							profileImage;
	private GUIMain							guiMain;
	private PanelContactAndChatInformation	pcaci;

	/**
	 * Constructor of this class. Initialises fields and calls
	 * {@link #initGUI(SimpleContact)}
	 * 
	 * @param contactProfile
	 * @param guiMain
	 */
	public PanelChatWindowTop(SimpleContact contactProfile, GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI(contactProfile);

	}

	/**
	 * This method builds the GUI of the parent <code>JPanel</code>.
	 * 
	 * @param contactProfile
	 */
	private void initGUI(SimpleContact contactProfile) {

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setBorder(new LineBorder(Color.black, 1));

		profileImage = scaleMaintainAspectRatio(guiMain.getProfileImage(contactProfile.getContactID()));

		JPanel p = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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

		JLabel lblUsername = new JLabel(contactProfile.getContactNickname(), JLabel.LEFT);
		lblUsername.setFont(new Font("Calibri", Font.BOLD, 15));
		lblUsername.setPreferredSize(new Dimension(
				lblUsername.getFontMetrics(lblUsername.getFont()).stringWidth(contactProfile.getContactNickname()),
				lblUsername.getFontMetrics(lblUsername.getFont()).getHeight()));
		lblUsername.setMaximumSize(lblUsername.getPreferredSize());
		add(lblUsername);
		add(Box.createHorizontalGlue());

		pcaci = new PanelContactAndChatInformation(guiMain, contactProfile,
				guiMain.getChatInformation(contactProfile.getContactID()));

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				guiMain.showParentPanel(pcaci);

			}
		});
	}

	private Image scaleMaintainAspectRatio(Image image) {

		Image returnImg = image.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
		return returnImg;
	}

}
