package jaims_development_studio.jaims.client.gui.customGUIComponents;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.showContacts.SettingDots;

/**
 * This class created a custom <code>PopUpMenu</code> in form of a
 * <code>JWindow</code> which holds buttons that allow the user to do various
 * thing, e.g. logging out or entering the settings menu.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 * @see #initGUI()
 */
public class PopUpMenu extends JWindow {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private SettingDots			sd;
	private Image				img, imgLogout;
	private JPanel				pBackground;
	private JLabel				panelSettings, panelLogout;

	/**
	 * Constructor of the class. Calls to super, initialises fields and starts
	 * building the GUI.
	 * 
	 * @param guiMain
	 *            reference to the GUIMain class
	 * @param sd
	 *            reference to the SettingDots class
	 * 
	 * @see GUIMain
	 * @see SettingDots
	 * @see JWindow
	 * @see #initGUI()
	 */
	public PopUpMenu(GUIMain guiMain, SettingDots sd) {

		super();

		this.guiMain = guiMain;
		this.sd = sd;

		initGUI();
	}

	/**
	 * This method is responsible for calling all other methods which are needed to
	 * build the <code>PopUpMenu</code>. It also initialises the
	 * <code>JWindow</code> with its attributes and handles a mouse listener to keep
	 * track of the windows position.
	 * 
	 * <ul>
	 * <li>{@link #loadAndResizeImages()}</li>
	 * <li>{@link #initPanelBackground()}</li>
	 * <li>{@link #initPanelSettings()}</li>
	 * <li>{@link #initPanelLogout()}</li>
	 * </ul>
	 * 
	 * @see JWindow
	 */
	private void initGUI() {

		loadAndResizeImages();

		setFocusable(true);
		setSize(42, 87);
		setBackground(new Color(0, 0, 0, 0));
		setLocation((int) sd.getLocationOnScreen().getX() - 2, (int) sd.getLocationOnScreen().getY() + 40);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		initPanelBackground();
		initPanelSettings();
		initPanelLogout();

		pBackground.add(panelSettings);

		pBackground.add(Box.createRigidArea(new Dimension(0, 5)));
		pBackground.add(panelLogout);

		add(pBackground);

		guiMain.getJaimsFrame().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {

				if (isShowing()) {
					setLocation((int) sd.getLocationOnScreen().getX() - 2, (int) sd.getLocationOnScreen().getY() + 40);
				}

			}

		});
		guiMain.getJaimsFrame().getContentPane().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (!contains(e.getPoint())) {
					dispose();
					sd.setDrawAnimation(false);
				}

			}
		});

		setVisible(true);

	}

	/**
	 * This methods loads the images for the different menu options and resizes them
	 * to the size of width = height = 40px;
	 */
	private void loadAndResizeImages() {

		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/calendar.png"))
				.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		imgLogout = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/logout_button.png"))
				.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	}

	/**
	 * Initialises the <code>JPanel</code> which serves as the background to the
	 * menu option panels.
	 */
	private void initPanelBackground() {

		pBackground = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(180, 180, 180, 150));
				g2.fillRoundRect(0, getHeight() - 40, getWidth(), (getHeight() - (getHeight() - 40)), 20, 20);
				g2.fillRect(0, 0, getWidth(), getHeight() - 20);

			}
		};
		pBackground.setLayout(new BoxLayout(pBackground, BoxLayout.PAGE_AXIS));
	}

	/**
	 * Initialises the <code>JPanel</code> which shows the menu option 'settings'
	 * with all necessary and important attributes such as the
	 * <code>IconImage</code> or a <code>ToolTipText</code> and adds a mouse
	 * listener.
	 */
	private void initPanelSettings() {

		panelSettings = new JLabel(new ImageIcon(img));
		panelSettings.setMinimumSize(new Dimension(42, 42));
		panelSettings.setPreferredSize(panelSettings.getMinimumSize());
		panelSettings.setMaximumSize(panelSettings.getMinimumSize());
		panelSettings.setToolTipText("Settings");
		panelSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				dispose();
				sd.setDrawAnimation(false);
				guiMain.showSettings();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	/**
	 * Initialises the <code>JPanel</code> which shows the menu option 'logout' with
	 * all necessary and important attributes such as the <code>IconImage</code> or
	 * a <code>TollTipText</code> and adds a mouse listener.
	 */
	private void initPanelLogout() {

		panelLogout = new JLabel(new ImageIcon(imgLogout));
		panelLogout.setMinimumSize(new Dimension(42, 42));
		panelLogout.setPreferredSize(panelLogout.getMinimumSize());
		panelLogout.setMaximumSize(panelLogout.getMinimumSize());
		panelLogout.setBackground(new Color(0, 0, 0, 0));
		panelLogout.setOpaque(false);
		panelLogout.setFocusable(true);
		panelLogout.setToolTipText("Logout");
		panelLogout.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				dispose();
				guiMain.doLogout();

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

}
