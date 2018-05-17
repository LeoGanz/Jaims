package jaims_development_studio.jaims.client.gui.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

/**
 * This class represents a <code>JPanel</code> that holds a
 * <code>JProgressBar</code> which indicates the program's progress during the
 * startup.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class ShowGuiBuildingProcess extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private Image				img;
	private JProgressBar		jpb;

	/**
	 * Constructor of this class. Only initialises the background image.
	 */
	public ShowGuiBuildingProcess() {

		img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/LoginBackground.png"))
				.getScaledInstance(900, 600, Image.SCALE_FAST);

	}

	/**
	 * Initialises the <code>JPanel</code> and the <code>JProgressBar</code>.
	 */
	public void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createVerticalGlue());

		jpb = new JProgressBar(0, 100);
		jpb.setBorder(new LineBorder(Color.black, 2));
		jpb.setPreferredSize(new Dimension(500, 40));
		jpb.setMaximumSize(jpb.getPreferredSize());
		jpb.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(jpb);

		add(Box.createVerticalGlue());

	}

	/**
	 * Receives an integer which indicated how much the loading has progressed, sets
	 * the progress bar's value to the given int and repaints it.
	 * 
	 * @param progress
	 *            percentage of how much of the loading process is done
	 */
	public void setProgress(int progress) {

		jpb.setValue(progress);
		jpb.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setClip(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40.5, 40.5));
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.drawImage(img, getWidth() / 2 - img.getWidth(this) / 2, getHeight() / 2 - img.getHeight(this) / 2, this);
	}

}
