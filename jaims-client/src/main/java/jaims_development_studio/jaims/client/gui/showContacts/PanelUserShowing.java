package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelUserShowing extends JPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private SimpleContact		simpleContact;
	private Image				img;
	private BufferedImage		output				= new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

	public PanelUserShowing(GUIMain guiMain, SimpleContact simpleContact) {

		this.simpleContact = simpleContact;

		img = guiMain.getUserProfileImage(simpleContact.getContactID()).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		setPreferredSize(new Dimension(250, 60));
		setMaximumSize(new Dimension(350, 60));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(70, 0)));
		JLabel lbl = new JLabel(simpleContact.getContactNickname(), JLabel.LEFT);
		lbl.setFont(new Font("Sans Serif", Font.BOLD, 16));
		add(lbl);
		add(Box.createHorizontalGlue());
		add(new AddSign(guiMain, this));
		add(Box.createRigidArea(new Dimension(2, 0)));
		add(new SettingDots(guiMain));
		add(Box.createRigidArea(new Dimension(8, 0)));
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				// TODO Auto-generated method stub

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

	}
}
