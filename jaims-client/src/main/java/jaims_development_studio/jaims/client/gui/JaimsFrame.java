package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaimsFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(JaimsFrame.class);
	
	JFrame frame;
	
	public JaimsFrame() {
		super("JAIMS Client");
		
		initLoadingFrame();
	}
	
	private void initLoadingFrame() {
		frame = new JFrame();
		frame.setSize(700, 500);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		
		Image img = null;		
		try {
			img = ImageIO.read(new File("C:/Users/josef/Pictures/JAIMS_Penguin.png"));
			img = img.getScaledInstance(370, 473, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			LOG.error("Failed to create image");
		}
		
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setVisible(true);
		
		
	}
	
	public void initGUI() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		try {
			setIconImage(ImageIO.read(new File("C:/Users/josef/Pictures/JAIMS_Penguin.png")));
		} catch (IOException e) {
			LOG.error("Failed to create IconImage");
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			LOG.error("Failed to set Look and Feel");
		}
		
		setVisible(true);
		
		frame.dispose();
		
	}

}
