package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		try {
			iconImages = new ArrayList<>(4);
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 16x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 32x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 64x.png")));
			iconImages.add(ImageIO.read(getClass().getResourceAsStream("/images/logo_blue_simple - 128x.png")));
		} catch (@SuppressWarnings("unused") IOException e) {
			LOG.warn("Couldn't read IconImages");
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error("Failed to set Look and Feel");
		}
		setIconImages(iconImages);
		getContentPane().setLayout(new BorderLayout(10, 0));
		initLoadingFrame();
	}

	private void initLoadingFrame() {

		frame = new JFrame();
		frame.setSize(705, 505);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
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

	public void initLogin() {

		setSize(new Dimension(450, 600));
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setLocationRelativeTo(null);
		frame.dispose();
	}

	public void initGUI() {

		setSize(900, 600);
		setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void initChatGUI() {

		dispose();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(900, 600);
		setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
		setBackground(Color.DARK_GRAY);
		setUndecorated(false);
		setVisible(true);
	}
}
