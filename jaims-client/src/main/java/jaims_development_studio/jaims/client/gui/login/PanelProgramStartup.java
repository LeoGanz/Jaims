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
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelProgramStartup extends JPanel {

	private GUIMain		guiMain;
	private Image		img, scaledImage;
	private boolean		signUpSelected	= false, signInSelected = true, closeSelected = false;
	private int			xSignIn			= 0, ySignIn = 0, xOvalSignIn = 0, xSignUp = 0, ySignUp = 0, xOvalSignUp = -55;
	private PanelLogin	panelLogin;
	private PanelSignUp	pRegister;
	JPanel				pa, panelSignUp, panelSignIn;

	public PanelProgramStartup(GUIMain guiMain) {

		this.guiMain = guiMain;
		img = Toolkit.getDefaultToolkit()
				.getImage(getClass().getClassLoader().getResource("images/LoginBackground.jpeg"))
				.getScaledInstance(900, 600, Image.SCALE_FAST);
		scaledImage = img;
	}

	public void initGUI() {

		setLayout(null);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JPanel pPageStart = new JPanel() {
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
		{
			pPageStart.add(Box.createHorizontalGlue());
			panelSignIn = new JPanel() {
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
						g2.fillOval(xOvalSignIn, ySignIn + 9, g2.getFontMetrics(g2.getFont()).stringWidth("Sign in"),
								3);
					}
				}
			};
			panelSignIn.setPreferredSize(new Dimension(60, 50));
			panelSignIn.setMaximumSize(panelSignIn.getPreferredSize());
			panelSignIn.setOpaque(false);
			panelSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			pPageStart.add(panelSignIn);
			pPageStart.add(Box.createRigidArea(new Dimension(15, 0)));

			panelSignUp = new JPanel() {
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
						g2.fillOval(xOvalSignUp, ySignUp + 9, g2.getFontMetrics(g2.getFont()).stringWidth("Sign up"),
								3);
					}
				}
			};
			panelSignUp.setPreferredSize(new Dimension(60, 50));
			panelSignUp.setMaximumSize(panelSignUp.getPreferredSize());
			panelSignUp.setOpaque(false);
			panelSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
		add(pPageStart);

		panelLogin = new PanelLogin(guiMain);
		panelLogin.setBounds(0, 80, 450, 520);
		add(panelLogin, BorderLayout.CENTER);

		pRegister = new PanelSignUp(guiMain);
		pRegister.setBounds(460, 80, 450, 520);
		add(pRegister);

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

	public String getRegisteredUsername() {

		return pRegister.getUsername();
	}

	public String getRegisteredPassword() {

		return pRegister.getPassword();
	}

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

	public void wrongUsername(boolean wrong) {

		panelLogin.setWrongUsername(wrong);
	}

	public void wrongPassword(boolean wrong) {

		panelLogin.setWrongPassword(wrong);
	}

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
