package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
	private Rectangle			maxBounds;
	private boolean				scaleAlongX			= false, scaleAlongY = false;
	private Point				startPoint;

	private JFrame				frame;

	public JaimsFrame(boolean showSplashScreen) {

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
		if (showSplashScreen)
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

	public void initLogin() {

		setSize(new Dimension(450, 600));
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setLocationRelativeTo(null);
		if (frame != null)
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
		setMinimumSize(new Dimension(700, 400));
		setLocationRelativeTo(null);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setDefaultLookAndFeelDecorated(true);
		setVisible(true);

		getContentPane().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if ((e.getX() >= 0 && e.getX() <= 5) || (e.getX() >= (getWidth() - 5) && e.getX() <= getWidth())) {
					scaleAlongX = true;
					getContentPane().setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				} else {
					if (scaleAlongX) {
						scaleAlongX = false;
						getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}

				if ((e.getY() >= 0 && e.getY() <= 5) || (e.getY() >= (getHeight() - 5) && e.getY() <= getHeight())) {
					scaleAlongY = true;
					getContentPane().setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				} else {
					if (scaleAlongY) {
						scaleAlongY = false;
						getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}

				if (scaleAlongY && scaleAlongX) {
					if ((e.getX() >= 0 && e.getX() <= 5) && (e.getY() >= 0 && e.getY() <= 5))
						getContentPane().setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					else if ((e.getX() >= 0 && e.getX() <= 5)
							&& (e.getY() >= (getHeight() - 5) && e.getY() <= getHeight()))
						getContentPane().setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
					else if ((e.getX() >= (getWidth() - 5) && e.getX() <= getWidth())
							&& (e.getY() >= 0 && e.getY() <= 5))
						getContentPane().setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
					else if ((e.getX() >= (getWidth() - 5) && e.getX() <= getWidth())
							&& (e.getY() >= (getHeight() - 5) && e.getY() <= getHeight()))
						getContentPane().setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				}

			}

			@Override
			public void mouseDragged(MouseEvent e) {

				Point frameLocation = getLocation();
				if ((startPoint.getX() >= 0) && (startPoint.getX() <= 5)) {
					int width = getWidth();
					int addingWidth = (int) Math.abs(e.getX() - startPoint.getX());
					width += addingWidth;
					int height = getHeight();
					int addingHeight = (int) Math.abs(e.getY() - startPoint.getY());
					height += addingHeight;

					setSize(width, height);
					setLocation(frameLocation.x - addingWidth, frameLocation.y - addingHeight);
				}
			}
		});
		getContentPane().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				startPoint = e.getPoint();

			}
		});
	}

	@Override
	public synchronized void setExtendedState(int state) {

		if (maxBounds == null && (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
			Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
			Rectangle screenSize = getGraphicsConfiguration().getBounds();
			Rectangle maxBounds = new Rectangle(screenInsets.left + screenSize.x, screenInsets.top + screenSize.y,
					screenSize.x + screenSize.width - screenInsets.right - screenInsets.left,
					screenSize.y + screenSize.height - screenInsets.bottom - screenInsets.top);
			super.setMaximizedBounds(maxBounds);
		}

		super.setExtendedState(state);
	}
}
