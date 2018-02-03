package jaims_development_studio.jaims.client.gui.login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class PanelLogin extends JPanel {

	private GUIMain			guiMain;
	private JTextField		tfUsername;
	private JPasswordField	pfPassword;
	private boolean			wrongUsername	= false, wrongPassword = false, failedLogin = false;
	private LoginButton		btLogin;

	private char			defaultCharEcho;

	public PanelLogin(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel pTextField = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

				g2.setColor(Color.GRAY);
				g2.setStroke(new BasicStroke(0.8F));
				g2.drawLine(30, 6, 30, getHeight() - 6);

				if (wrongUsername) {
					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
					int[] x = {getWidth() - 24, getWidth() - 16, getWidth() - 8};
					int[] y = {5, 23, 5};
					g2.setColor(Color.RED);
					g2.fill(new RoundPolygon(new Polygon(x, y, 3), 5));

					g2.setColor(Color.RED);
					g2.fillOval(getWidth() - 20, 26, 8, 8);
				}
			}
		};
		pTextField.setPreferredSize(new Dimension(250, 36));
		pTextField.setMaximumSize(pTextField.getPreferredSize());
		pTextField.setOpaque(true);
		pTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pTextField.setLayout(new BoxLayout(pTextField, BoxLayout.LINE_AXIS));
		{
			tfUsername = new JTextField("Username");
			tfUsername.setForeground(Color.LIGHT_GRAY);
			tfUsername.setFont(new Font("Calibri", Font.BOLD, 13));
			tfUsername.setPreferredSize(new Dimension(185, 30));
			tfUsername.setMaximumSize(new Dimension(185, 30));
			tfUsername.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
			tfUsername.setAlignmentY(Component.CENTER_ALIGNMENT);
			tfUsername.requestFocus(false);
			tfUsername.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {

					if (tfUsername.getText().equals("")) {
						tfUsername.setForeground(Color.LIGHT_GRAY);
						tfUsername.setText("Username");
						tfUsername.repaint();
					}

				}

				@Override
				public void focusGained(FocusEvent e) {

					if (tfUsername.getText().equals("Username")) {
						tfUsername.setText("");
						tfUsername.setForeground(Color.BLACK);
						tfUsername.repaint();
					}

				}
			});
			tfUsername.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						btLogin.doClick();

					if (tfUsername.getText().length() < 120) {
						wrongUsername = verifyUsername(tfUsername.getText());
						pTextField.repaint();
						if (wrongUsername || wrongPassword || guiMain.isServerConnected() == false) {
							btLogin.setEnabled(false);
							btLogin.repaint();
						} else {
							btLogin.setEnabled(true);
							btLogin.repaint();
						}
					} else {
						wrongUsername = true;
						pTextField.repaint();
						btLogin.setEnabled(false);
						btLogin.repaint();
					}

				}
			});
			pTextField.add(Box.createRigidArea(new Dimension(40, 36)));
			pTextField.add(tfUsername);
		}

		add(Box.createVerticalGlue());
		add(pTextField);

		JPanel pPasswordField = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

				g2.setColor(Color.GRAY);
				g2.setStroke(new BasicStroke(0.8F));
				g2.drawLine(30, 6, 30, getHeight() - 6);

				if (wrongPassword) {
					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
					int[] x = {getWidth() - 24, getWidth() - 16, getWidth() - 8};
					int[] y = {5, 23, 5};
					g2.setColor(Color.RED);
					g2.fill(new RoundPolygon(new Polygon(x, y, 3), 5));

					g2.setColor(Color.RED);
					g2.fillOval(getWidth() - 20, 26, 8, 8);
				}
			}
		};
		pPasswordField.setPreferredSize(new Dimension(250, 36));
		pPasswordField.setMaximumSize(pPasswordField.getPreferredSize());
		pPasswordField.setOpaque(true);
		pPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pPasswordField.setLayout(new BoxLayout(pPasswordField, BoxLayout.LINE_AXIS));
		{

			pfPassword = new JPasswordField();
			pfPassword.setFont(new Font("Calibri", Font.BOLD, 13));
			pfPassword.setPreferredSize(new Dimension(185, 30));
			pfPassword.setMaximumSize(pfPassword.getPreferredSize());
			pfPassword.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
			pfPassword.setAlignmentY(Component.CENTER_ALIGNMENT);
			pfPassword.setText("Password");
			pfPassword.setForeground(Color.LIGHT_GRAY);
			defaultCharEcho = pfPassword.getEchoChar();
			pfPassword.setEchoChar((char) 0);
			pfPassword.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {

					if (pfPassword.getText().equals("")) {
						pfPassword.setText("Password");
						pfPassword.setEchoChar((char) 0);
						pfPassword.setForeground(Color.LIGHT_GRAY);
						pfPassword.repaint();
					}

				}

				@Override
				public void focusGained(FocusEvent e) {

					pfPassword.setText("");
					pfPassword.setEchoChar(defaultCharEcho);
					pfPassword.setForeground(Color.BLACK);
					pfPassword.repaint();

				}
			});
			pfPassword.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						btLogin.doClick();

					char[] c = pfPassword.getPassword();
					String password = String.valueOf(c);

					if (password.length() < 120) {
						wrongPassword = verifyPassword(password);
						pPasswordField.repaint();
						if (wrongPassword || wrongUsername || guiMain.isServerConnected() == false) {
							btLogin.setEnabled(false);
							btLogin.repaint();
						} else {
							btLogin.setEnabled(true);
							btLogin.repaint();
						}
					} else {
						wrongPassword = true;
						pPasswordField.repaint();
						btLogin.setEnabled(false);
						btLogin.repaint();
					}

				}
			});

			pPasswordField.add(Box.createRigidArea(new Dimension(40, 36)));
			pPasswordField.add(pfPassword);

		}
		add(Box.createRigidArea(new Dimension(0, 35)));
		add(pPasswordField);

		JCheckBox jcb = new JCheckBox("Remember me");
		jcb.setSelected(false);
		jcb.setForeground(Color.WHITE);
		jcb.setPreferredSize(new Dimension(185, 25));
		jcb.setMaximumSize(jcb.getPreferredSize());
		jcb.setOpaque(false);
		jcb.setBackground(new Color(0, 0, 0, 0));
		jcb.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		jcb.setAlignmentX(Component.CENTER_ALIGNMENT);
		jcb.setFont(new Font("Sans Serif", Font.PLAIN, 13));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(jcb);
		add(Box.createVerticalGlue());

		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(190, 60));
		panel.setPreferredSize(panel.getMinimumSize());
		panel.setMaximumSize(panel.getMinimumSize());
		panel.setBackground(new Color(0, 0, 0, 0));
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		btLogin = new LoginButton(guiMain, "Login", panel);
		btLogin.addActionListener(e -> {

			char[] c = pfPassword.getPassword();
			String password = String.valueOf(c);
			if (wrongPassword == false && wrongUsername == false && tfUsername.getText().equals("Username") == false
					&& password.equals("Password") == false) {
				guiMain.sendLogin(tfUsername.getText(), password);
			}

		});
		panel.add(Box.createVerticalGlue());
		panel.add(btLogin);
		panel.add(Box.createVerticalGlue());
		add(panel);
		add(Box.createRigidArea(new Dimension(0, 30)));

		JLabel lblForgotPassword = new JLabel("<html><u>Forgot password ?</u></html>", JLabel.CENTER);
		lblForgotPassword.setMaximumSize(new Dimension(100, 25));
		lblForgotPassword.setPreferredSize(lblForgotPassword.getMaximumSize());
		lblForgotPassword.setOpaque(false);
		// lblForgotPassword.setBackground(new Color(0, 0, 0, 0));
		lblForgotPassword.setForeground(Color.WHITE);
		lblForgotPassword.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

		add(lblForgotPassword);
		add(Box.createRigidArea(new Dimension(0, 5)));
	}

	public void setLoginEnabled(boolean enabled) {

		btLogin.setEnabled(enabled);
		if (enabled)
			btLogin.setToolTipText("");
		else
			btLogin.setToolTipText("Could not connect to the server!");
		btLogin.repaint();
		repaint();
	}

	public void setLogin(String username, String password) {
		tfUsername.setText(username);
		tfUsername.setForeground(Color.BLACK);
		tfUsername.repaint();
		pfPassword.setText(password);
		pfPassword.setEchoChar(defaultCharEcho);
		pfPassword.setForeground(Color.BLACK);
		pfPassword.repaint();
		revalidate();
	}

	private Boolean verifyUsername(String username) {

		return !username.matches("[A-Za-z1-9_.]*");
	}

	private Boolean verifyPassword(String password) {

		return !password.matches("[A-Za-z1-9!\\?@\\(\\)\\{\\}\\[\\]\\\\/|<>=~$€%&#\\*-\\+.:,;'\"_]*");
	}

	public void setWrongUsername(boolean username) {

		wrongUsername = username;
		repaint();
	}

	public void setWrongPassword(boolean wrongPassword) {

		this.wrongPassword = wrongPassword;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private class RoundPolygon implements Shape {
		GeneralPath path;

		public RoundPolygon(Polygon p, int arcWidth) {

			path = new GeneralPath();
			transform(p, arcWidth, path);
		}

		@Override
		public Rectangle getBounds() {

			return path.getBounds();
		}

		@Override
		public Rectangle2D getBounds2D() {

			return path.getBounds2D();
		}

		@Override
		public boolean contains(double x, double y) {

			return path.contains(x, y);
		}

		@Override
		public boolean contains(Point2D p) {

			return path.contains(p);
		}

		@Override
		public boolean intersects(double x, double y, double w, double h) {

			return path.intersects(x, y, w, h);
		}

		@Override
		public boolean intersects(Rectangle2D r) {

			return path.intersects(r);
		}

		@Override
		public boolean contains(double x, double y, double w, double h) {

			return path.contains(x, y, w, h);
		}

		@Override
		public boolean contains(Rectangle2D r) {

			return path.contains(r);
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at) {

			return path.getPathIterator(at);
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at, double flatness) {

			return path.getPathIterator(at, flatness);
		}

		protected void transform(Polygon shape, int arcWidth, GeneralPath path) {

			PathIterator pIter = shape.getPathIterator(new AffineTransform());

			Point2D.Float pointFirst = new Point2D.Float(0, 0);
			Point2D.Float pointSecond = null;

			Point2D.Float pointLast = new Point2D.Float(0, 0);
			Point2D.Float pointCorner = null;
			Point2D.Float pointNext = null;

			float[] coor = new float[6];
			while (!pIter.isDone()) {
				int type = pIter.currentSegment(coor);
				float x1 = coor[0];
				float y1 = coor[1];
				float x2 = coor[2];
				float y2 = coor[3];
				float x3 = coor[4];
				float y3 = coor[5];

				switch (type) {
				case PathIterator.SEG_CLOSE:
					// path.closePath();
					break;
				case PathIterator.SEG_CUBICTO:
					path.curveTo(x1, y1, x2, y2, x3, y3);
					break;
				case PathIterator.SEG_LINETO:
					if (pointCorner == null) {
						pointCorner = new Point2D.Float(x1, y1);
						if (pointNext == null) {
							// first move
							pointSecond = new Point2D.Float(x1, y1);
							Point2D.Float arcStartPoint = getArcPoint(pointSecond, pointFirst, arcWidth);

							path.moveTo(arcStartPoint.x, arcStartPoint.y);
						}
					} else {
						pointNext = new Point2D.Float(x1, y1);
						add(path, pointLast, pointCorner, pointNext, arcWidth);
						pointLast = pointCorner;
						pointCorner = pointNext;
					}
					// res.lineTo(x1, y1);
					break;
				case PathIterator.SEG_MOVETO:
					pointLast.x = x1;
					pointLast.y = y1;
					pointFirst.x = x1;
					pointFirst.y = y1;
					// path.moveTo(x1, y1);
					break;
				case PathIterator.SEG_QUADTO:
					path.quadTo(x1, y1, x2, y2);
					break;
				}
				pIter.next();
			}

			add(path, pointLast, pointCorner, pointFirst, arcWidth);
			add(path, pointCorner, pointFirst, pointSecond, arcWidth);
			// path.lineTo(pointStart.x, pointStart.y);
			path.closePath();

		}

		protected void add(GeneralPath path, Point2D.Float last, Point2D.Float corner, Point2D.Float next, float w) {

			Point2D.Float arcStartPoint = getArcPoint(last, corner, w);
			Point2D.Float arcEndPoint = getArcPoint(next, corner, w);

			path.lineTo(arcStartPoint.x, arcStartPoint.y);
			path.quadTo(corner.x, corner.y, arcEndPoint.x, arcEndPoint.y);
		}

		protected Point2D.Float getArcPoint(Point2D.Float p1, Point2D.Float p2, float w) {

			Point2D.Float res = new Point2D.Float();
			float d = Math.round(Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));

			if (p1.x < p2.x) {
				res.x = p2.x - w * Math.abs(p1.x - p2.x) / d;
			} else {
				res.x = p2.x + w * Math.abs(p1.x - p2.x) / d;
			}

			if (p1.y < p2.y) {
				res.y = p2.y - w * Math.abs(p1.y - p2.y) / d;
			} else {
				res.y = p2.y + w * Math.abs(p1.y - p2.y) / d;
			}

			return res;
		}

		protected void transformPrint(Polygon shape) {

			PathIterator pIter = shape.getPathIterator(new AffineTransform());
			GeneralPath res = new GeneralPath();

			float[] coor = new float[6];
			while (!pIter.isDone()) {
				int type = pIter.currentSegment(coor);
				float x1 = coor[0];
				float y1 = coor[1];
				float x2 = coor[2];
				float y2 = coor[3];
				float x3 = coor[4];
				float y3 = coor[5];

				switch (type) {
				case PathIterator.SEG_CLOSE:
					res.closePath();
					System.out.println("SEG_CLOSE");
					break;
				case PathIterator.SEG_CUBICTO:
					res.curveTo(x1, y1, x2, y2, x3, y3);
					System.out.println("SEG_CUBICTO");
					break;
				case PathIterator.SEG_LINETO:
					res.lineTo(x1, y1);
					System.out.println("SEG_LINETO");
					break;
				case PathIterator.SEG_MOVETO:
					res.moveTo(x1, y1);
					System.out.println("SEG_MOVETO");
					break;
				case PathIterator.SEG_QUADTO:
					res.quadTo(x1, y1, x2, y2);
					System.out.println("SEG_QUADTO");
					break;
				}
				pIter.next();
			}

		}
	}

}
