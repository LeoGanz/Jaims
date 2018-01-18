package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaimsFrame extends JFrame {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(JaimsFrame.class);
	private List<Image>			iconImages;
	private Image				img					= null;

	private JFrame				frame;

	public JaimsFrame() {

		super("JAIMS Client");

		try {
			iconImages = new ArrayList<>(4);
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 16x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 32x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 64x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 128x.png")));
		} catch (@SuppressWarnings("unused") IOException e) {
			LOG.warn("Couldn't read IconImages");
		}

		initLoadingFrame();
	}

	private void initLoadingFrame() {

		frame = new JFrame();
		frame.setSize(705, 505);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setIconImages(iconImages);

		try {
			img = ImageIO.read(getClass().getResourceAsStream("/images/JAIMS_Logo.png"));
			img = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			LOG.error("Failed to create image");
		}
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
		frame.setVisible(true);

	}

	public void initGUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(810, 510);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(10, 0));
		setIconImages(iconImages);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error("Failed to set Look and Feel");
		}
		frame.dispose();

		setVisible(true);

	}

	// @Override
	// public void setVisible(final boolean visible) {
	//
	// JFrame f = this;
	// super.setVisible(visible);
	//
	// Timer uiChanger = new Timer(8, new ActionListener() {
	//
	// private int increment = 110;
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// if (f.getSize().getWidth() <= 810) {
	// f.setSize(f.getWidth() + increment, f.getHeight());
	// f.setLocation(f.getX() - increment / 2, f.getY());
	// }
	//
	// if (f.getHeight() <= 510) {
	// f.setSize(f.getWidth(), f.getHeight() + increment);
	// f.setLocation(f.getX(), f.getY() - increment / 2);
	// }
	//
	// if (f.getHeight() >= 810)
	// ((Timer) e.getSource()).stop();
	//
	// f.setMinimumSize(new Dimension(f.getWidth(), f.getHeight()));
	//
	// }
	// });
	// uiChanger.start();
	// }

}
