package jaims_development_studio.jaims.client.gui.login;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * <code>PanelProgramStartup</code> represents the <code>JPanel</code> that is
 * put onto the <code>JFrame</code>'s content pane when starting the client. It
 * gives the user the possibilities of logging in or creating a new account.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 * 
 * @see #initGUI()
 *
 */
public class PanelProgramStartup extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private GUIMain				guiMain;
	private Image				img;
	private boolean				signUpSelected		= false, signInSelected = true, closeSelected = false;
	private int					xSignIn				= 0, ySignIn = 0, xOvalSignIn = 0, xSignUp = 0, ySignUp = 0,
			xOvalSignUp = -55;
	private PanelLogin			panelLogin;
	private PanelSignUp			pRegister;
	private JPanel				panelSignUp, panelSignIn, pPageStart;

	/**
	 * Constructor of this class. Initialises the reference to the
	 * <code>GUIMain</code> class and loads the background image that has to be
	 * displayed. Calls {@link #initGUI()} to build the GUI.
	 * 
	 * @param guiMain
	 *            reference to the GUIMain class
	 * 
	 * @see GUIMain
	 */
	public PanelProgramStartup(GUIMain guiMain) {

		this.guiMain = guiMain;
		img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/LoginBackground.png"))
				.getScaledInstance(900, 600, Image.SCALE_FAST);

		initGUI();
	}

	/**
	 * This method is responsible for calling every method that is needed to build
	 * the GUI. Afterwards it adds all <code>JPanel</code>s built in these methods
	 * to the parent <code>JPanel</code>. It also sets the layout of the parent
	 * panel to <code>null</code> in order to make the animation when switching
	 * between the panels for login in and sing up possible.<br>
	 * The following methods are called:
	 * <ul>
	 * <li>{@link #initPanelPageStart()}</li>
	 * <li>{@link #initPanelSignIn()}</li>
	 * <li>{@link #initPanelSignUp()}</li>
	 * <li>{@link #addMouseListenerpSignUp()}</li>
	 * <li>{@link #addMouseListenerpSignIn()}</li>
	 * <li>{@link #addMouseListeners()}</li>
	 * </ul>
	 */
	private void initGUI() {

		setLayout(null);

		initPanelPageStart();
		initPanelSignIn();
		initPanelSignUp();
		addMouseListenerpSignUp();
		addMouseListenerpSignIn();

		add(pPageStart);
		panelLogin = new PanelLogin(guiMain);
		panelLogin.setBounds(0, 80, 450, 520);
		add(panelLogin, BorderLayout.CENTER);
		pRegister = new PanelSignUp(guiMain);
		pRegister.setBounds(460, 80, 450, 520);
		add(pRegister);

		addMouseListeners();

	}

	/**
	 * This method creates the <code>JPanel</code> that is added to the
	 * <code>JFrame</code>'s equivalent of a </code>BorderLayout</code>'s
	 * </code>Page.START</code> only with absolute coordinates because of the null
	 * layout. It furthermore holds the <code>JPanel</code>s for "Sign in" and "Sign
	 * up" which are added respectively in {@link #initPanelSignIn()} and
	 * {@link #initPanelSignUp()}
	 */
	private void initPanelPageStart() {

		pPageStart = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		pPageStart.setOpaque(false);
		pPageStart.setLayout(new BoxLayout(pPageStart, BoxLayout.LINE_AXIS));
		pPageStart.setBounds(0, 0, 450, 80);
		pPageStart.setBorder(new EmptyBorder(30, 0, 0, 0));
	}

	/**
	 * This method creates the <code>JPanel</code> that is shown at the top of the
	 * <code>JFrame</code> and only holds the text "Sign in".
	 */
	private void initPanelSignIn() {

		pPageStart.add(Box.createHorizontalGlue());
		panelSignIn = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Calibri", Font.PLAIN, 20));
				if (xSignIn == 0)
					xSignIn = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Sign in") / 2;
				if (ySignIn == 0)
					ySignIn = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;
				if (xOvalSignIn == 0)
					xOvalSignIn = xSignIn;
				g2.drawString("Sign in", xSignIn, ySignIn);

				if (signInSelected) {
					g2.setColor(new Color(191, 30, 30, 190));
					g2.setStroke(new BasicStroke(2.5F));
					g2.fillOval(xOvalSignIn, ySignIn + 9, g2.getFontMetrics(g2.getFont()).stringWidth("Sign in"), 3);
				}
			}
		};
		panelSignIn.setPreferredSize(new Dimension(60, 50));
		panelSignIn.setMaximumSize(panelSignIn.getPreferredSize());
		panelSignIn.setOpaque(false);
		panelSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pPageStart.add(panelSignIn);
		pPageStart.add(Box.createRigidArea(new Dimension(15, 0)));
	}

	/**
	 * This method creates the <code>JPanel</code> that is shown at the top of the
	 * <code>JFrame</code> and only holds the text "Sign up".
	 */
	private void initPanelSignUp() {

		panelSignUp = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Calibri", Font.PLAIN, 20));
				if (xSignUp == 0)
					xSignUp = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Sign up") / 2;
				if (ySignUp == 0)
					ySignUp = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;

				g2.drawString("Sign up", xSignUp, ySignUp);

				if (signUpSelected) {
					g2.setColor(new Color(191, 30, 30, 190));
					g2.setStroke(new BasicStroke(2.5F));
					g2.fillOval(xOvalSignUp, ySignUp + 9, g2.getFontMetrics(g2.getFont()).stringWidth("Sign up"), 3);
				}
			}
		};
		panelSignUp.setPreferredSize(new Dimension(60, 50));
		panelSignUp.setMaximumSize(panelSignUp.getPreferredSize());
		panelSignUp.setOpaque(false);
		panelSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * This method adds a <code>MouseListener</code> to the sign up panel which
	 * starts an animation that slides from the sign in panel to the sign up panel
	 * and changes from the <code>PanelLogin</code> to the <code>PanelSignUp</code>
	 * when the sign up panel is clicked.
	 * 
	 * @see #showPanelSignUp()
	 * @see PanelLogin
	 * @see PanelSignUp
	 * @see MouseListener
	 */
	private void addMouseListenerpSignUp() {

		panelSignUp.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if (signUpSelected == false) {

					Timer timer = new Timer(10, e1 -> {

						if (signInSelected == true) {
							if (xOvalSignIn <= panelSignIn.getWidth()) {
								xOvalSignIn += 4;
								panelSignIn.repaint();
							} else {
								signInSelected = false;
								panelSignIn.repaint();
								((Timer) e1.getSource()).stop();

							}
						}

					});
					timer.start();
					Timer timer2 = new Timer(10, e1 -> {

						if (signUpSelected == false)
							signUpSelected = true;

						if (xOvalSignUp < xSignUp) {
							if (xOvalSignUp < xSignUp) {
								xOvalSignUp += 4;
								panelSignUp.repaint();
							} else {
								xOvalSignUp = xSignUp;
								panelSignUp.repaint();
								((Timer) e1.getSource()).stop();
							}
						} else {
							((Timer) e1.getSource()).stop();
						}

					});
					timer2.start();
					Timer timer3 = new Timer(10, e1 -> {

						double x = panelLogin.getLocation().getX() - 40;
						panelLogin.setLocation((int) x, (int) panelLogin.getLocation().getY());
						repaint();
						if (x < -460) {
							((Timer) e1.getSource()).stop();
							showPanelSignUp();
						}

					});
					timer3.start();

				}

			}

		});
	}

	/**
	 * This method adds a <code>MouseListener</code> to the sign in panel which
	 * starts an animation that slides from the sign up panel to the sign in panel
	 * and changes from the <code>PanelSignUp</code> to the <code>PanelLogin</code>
	 * when the sign up panel is clicked.
	 * 
	 * @see #showPanelSignIn()
	 * @see PanelLogin
	 * @see PanelSignUp
	 * @see MouseListener
	 */
	private void addMouseListenerpSignIn() {

		panelSignIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (signInSelected == false) {

					Timer timer3 = new Timer(10, e1 -> {

						if (signUpSelected == true) {
							if (xOvalSignUp >= -55) {
								xOvalSignUp = xOvalSignUp - 4;
								panelSignUp.repaint();
							} else {
								signUpSelected = false;
								panelSignUp.repaint();
								((Timer) e1.getSource()).stop();

							}
						}

					});
					timer3.start();
					Timer timer4 = new Timer(10, e1 -> {

						if (signInSelected == false)
							signInSelected = true;

						if (xOvalSignIn > xSignIn) {
							if (xOvalSignIn > xSignIn) {
								xOvalSignIn -= 4;
								panelSignIn.repaint();
							} else {
								xOvalSignIn = xSignIn;
								panelSignIn.repaint();
								((Timer) e1.getSource()).stop();
							}
						} else {
							((Timer) e1.getSource()).stop();
						}

					});
					timer4.start();

					Timer timer5 = new Timer(10, e1 -> {

						double x = pRegister.getLocation().getX() + 40;
						pRegister.setLocation((int) x, (int) pRegister.getLocation().getY());
						repaint();
						if (x >= 460) {
							((Timer) e1.getSource()).stop();
							showPanelSignIn();
						}

					});
					timer5.start();
				}

			}
		});
		pPageStart.add(panelSignUp);
		pPageStart.add(Box.createHorizontalGlue());
	}

	/**
	 * Adds a <code>MouseListener</code> to the parent panel which listens to the
	 * mouse's movements and when it was clicked. When either of these events return
	 * mouse coordinates in the square the close button was drawn in, the mouse
	 * changes to a <code>Cursor.HAND_CURSOR</code> and upon clicking the painted
	 * button, the client closes.
	 * 
	 * @see MouseListener
	 * @see Cursor
	 */
	private void addMouseListeners() {

		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				if ((e.getX() >= 405 && e.getX() <= 435) && (e.getY() >= 10 && e.getY() <= 40))
					System.exit(0);

			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {

				if ((e.getX() >= 410 && e.getX() <= 430) && (e.getY() >= 15 && e.getY() <= 35)) {
					closeSelected = true;
					setCursor(new Cursor(Cursor.HAND_CURSOR));
					repaint();
				}

				if (closeSelected) {
					if ((e.getX() <= 405 || e.getX() >= 435) && (e.getY() <= 10 || e.getY() >= 40)) {
						closeSelected = false;
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						repaint();
					}
				}

			}
		});
	}

	/**
	 * Animates the login panel to slide to the left and the sign up panel to come
	 * into the displayed area from the right.
	 */
	private void showPanelSignUp() {

		double y = panelLogin.getLocation().getY();

		Timer timer4 = new Timer(10, e -> {

			double x = pRegister.getLocation().getX() - 40;
			pRegister.setLocation((int) x, (int) y);
			repaint();
			if (x <= 0) {
				pRegister.setLocation(0, (int) y);
				repaint();
				((Timer) e.getSource()).stop();
			}

		});
		timer4.start();
	}

	/**
	 * Starts the animation that slides the sign up panel out of the frame and the
	 * sign in panel back into it.
	 */
	private void showPanelSignIn() {

		double y = pRegister.getLocation().getY();

		Timer timer = new Timer(10, e -> {

			double x = panelLogin.getLocation().getX() + 40;
			panelLogin.setLocation((int) x, (int) y);
			repaint();
			if (x > 0) {
				panelLogin.setLocation(0, (int) y);
				repaint();
				((Timer) e.getSource()).stop();
			}

		});
		timer.start();
	}

	/**
	 * Gets the username and the password of a newly and successfully registered
	 * user, fills them into the login panel and starts a animation from the
	 * currently displayed sign up panel back to the sign in panel.
	 * 
	 * @see PanelLogin#setLogin(String, String)
	 * 
	 * @deprecated
	 */
	public void succesfulRegistration(String username, String password) {

		Timer timer3 = new Timer(10, e1 -> {

			if (signUpSelected == true) {
				if (xOvalSignUp >= -55) {
					xOvalSignUp = xOvalSignUp - 4;
					panelSignUp.repaint();
				} else {
					signUpSelected = false;
					panelSignUp.repaint();
					((Timer) e1.getSource()).stop();

				}
			}

		});
		timer3.start();
		Timer timer4 = new Timer(10, e1 -> {

			if (signInSelected == false)
				signInSelected = true;

			if (xOvalSignIn > xSignIn) {
				if (xOvalSignIn > xSignIn) {
					xOvalSignIn -= 4;
					panelSignIn.repaint();
				} else {
					xOvalSignIn = xSignIn;
					panelSignIn.repaint();
					((Timer) e1.getSource()).stop();
				}
			} else {
				((Timer) e1.getSource()).stop();
			}

		});
		timer4.start();

		panelLogin.setLogin(username, password);
		Timer timer5 = new Timer(10, e1 -> {

			double x = pRegister.getLocation().getX() + 40;
			pRegister.setLocation((int) x, (int) pRegister.getLocation().getY());
			repaint();
			if (x >= 460) {
				((Timer) e1.getSource()).stop();
				showPanelSignIn();
			}

		});
		timer5.start();
		signInSelected = true;
		signUpSelected = false;
	}

	/**
	 * 
	 * @return the username used in the sign up panel
	 */
	public String getRegisteredUsername() {

		return pRegister.getUsername();
	}

	/**
	 * 
	 * @return the password used in the sign up panel
	 */
	public String getRegisteredPassword() {

		return pRegister.getPassword();
	}

	/**
	 * @param wrong
	 *            indicates whether the entered username was wrong
	 * 
	 * @see PanelLogin#setWrongUsername(boolean)
	 */
	public void wrongUsername(boolean wrong) {

		panelLogin.setWrongUsername(wrong);
	}

	/**
	 * @param wrong
	 *            indicated whether the entered password was wrong
	 * 
	 * @see PanelLogin#setWrongPassword(boolean)
	 */
	public void wrongPassword(boolean wrong) {

		panelLogin.setWrongPassword(wrong);
	}

	/**
	 * @param enabled
	 *            sets the LoginButton enabled/disabled
	 * 
	 * @see PanelLogin#setLoginEnabled(boolean)
	 */
	public void setLoginEnabled(boolean enabled) {

		panelLogin.setLoginEnabled(enabled);
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

		if (closeSelected) {
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(2.5F));
			g2.drawLine(405, 10, 435, 40);
			g2.drawLine(405, 40, 435, 10);
			g2.setStroke(new BasicStroke(1));
		} else {
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(1));
			g2.drawLine(410, 15, 430, 35);
			g2.drawLine(410, 35, 430, 15);
		}
	}
}
