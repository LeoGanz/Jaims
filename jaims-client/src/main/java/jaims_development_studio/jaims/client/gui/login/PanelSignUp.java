package jaims_development_studio.jaims.client.gui.login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.api.util.CheckRegistrationDetails;
import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * This class gives the user the possibility to sign himself up. It is shown on
 * the startup frame by clicking the appropriate button in
 * </code>PanelProgramStartup</code>.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class PanelSignUp extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private JTextField			tfUsername, tfEmail;
	private JPasswordField		pfPassword;
	private boolean				wrongUsername		= true, wrongPassword = true, wrongEmail = true;
	private char				defaultCharEcho;
	private LoginButton			btRegister;
	private JPanel				pTextField, pPasswordField, pEmail, panel;

	/**
	 * Constructor of this class. Initialises the GUIMain instance and calls
	 * {@link #initGUI()}.
	 * 
	 * @param guiMain
	 *            instance of the GUIMain class
	 * 
	 * @see GUIMain
	 */
	public PanelSignUp(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	/**
	 * This method calls all methods that are needed to build the GUI for the sign
	 * up panel. <br>
	 * <ul>
	 * <li>{@link #initPanelTextField()}</li>
	 * <li>{@link #initTFUsername()}</li>
	 * <li>{@link #initPanelPasswordField()}</li>
	 * <li>{@link #initPFPassword()}</li>
	 * <li>{@link #initPanelEmail()}</li>
	 * <li>{@link #initTFEmail()}</li>
	 * <li>{@link #initButtonPanel()}</li>
	 * <li>{@link #initButtonRegister()}</li>
	 * </ul>
	 */
	private void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		initPanelTextField();
		initTFUsername();
		initPanelPasswordField();
		initPFPassword();
		initPanelEmail();
		initTFEmail();
		initButtonPanel();
		initButtonRegister();

		add(Box.createVerticalGlue());
		add(pTextField);
		add(Box.createRigidArea(new Dimension(0, 35)));
		add(pPasswordField);
		add(Box.createRigidArea(new Dimension(0, 35)));
		add(pEmail);
		add(Box.createVerticalGlue());
		add(panel);
		add(Box.createRigidArea(new Dimension(0, 60)));
	}

	/**
	 * This method creates a custom painted <code>JPanel</code> that is later on
	 * used to hold a <code>JTextField</code> in which the user can enter his
	 * desired username.
	 * 
	 * @see #initTFUsername()
	 */
	private void initPanelTextField() {

		pTextField = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
				}
			}
		};
		pTextField.setPreferredSize(new Dimension(250, 36));
		pTextField.setMaximumSize(pTextField.getPreferredSize());
		pTextField.setOpaque(true);
		pTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pTextField.setLayout(new BoxLayout(pTextField, BoxLayout.LINE_AXIS));
	}

	/**
	 * This method creates a <code>JTextField</code> in which the user can enter his
	 * desired username. It also adds a key listener to the text field which checks
	 * after every <code>KeyReleased</code> event whether the current username in
	 * the field fits the criteria for a username, specified in the API.
	 * 
	 * @see JTextField
	 * @see KeyListener
	 * @see CheckRegistrationDetails#verifyUsername(String)
	 */
	private void initTFUsername() {

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

				if (tfUsername.getText().length() < 120) {
					wrongUsername = CheckRegistrationDetails.verifyUsername(tfUsername.getText());
					pTextField.repaint();
					if (wrongUsername || wrongPassword || wrongEmail) {
						btRegister.setEnabled(false);
						btRegister.repaint();
					} else {
						btRegister.setEnabled(true);
						btRegister.repaint();
					}
				} else {
					wrongUsername = true;
					pTextField.repaint();
					btRegister.setEnabled(false);
					btRegister.repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btRegister.doClick();

			}
		});
		pTextField.add(Box.createRigidArea(new Dimension(40, 36)));
		pTextField.add(tfUsername);
	}

	/**
	 * This method creates a <code>JPanel</code> that is later on used to hold the
	 * <code>JPasswordFiel</code> which the user uses to enter his password.
	 * 
	 * @see #initPFPassword()
	 */
	private void initPanelPasswordField() {

		pPasswordField = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
				}
			}
		};
		pPasswordField.setPreferredSize(new Dimension(250, 36));
		pPasswordField.setMaximumSize(pPasswordField.getPreferredSize());
		pPasswordField.setOpaque(true);
		pPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		pPasswordField.setLayout(new BoxLayout(pPasswordField, BoxLayout.LINE_AXIS));
	}

	/**
	 * This method creates a <code>JPasswordField</code> in which the user can enter
	 * his desired password. It also adds a key listener to the pw field which
	 * checks after every <code>KeyReleased</code> event whether the current
	 * password in the field fits the criteria for a password, specified in the API.
	 * 
	 * @see JPasswordField
	 * @see KeyListener
	 * @see CheckRegistrationDetails#verifyPassword(String)
	 * 
	 */
	private void initPFPassword() {

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

				char[] c = pfPassword.getPassword();
				String password = String.valueOf(c);

				if (password.equals("")) {
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

				char[] c = pfPassword.getPassword();
				String password = String.valueOf(c);

				if (password.length() < 120) {
					wrongPassword = CheckRegistrationDetails.verifyPassword(password);
					pPasswordField.repaint();
					if (wrongPassword || wrongUsername || wrongEmail) {
						btRegister.setEnabled(false);
						btRegister.repaint();
					} else {
						btRegister.setEnabled(true);
						btRegister.repaint();
					}
				} else {
					wrongPassword = true;
					pPasswordField.repaint();
					btRegister.setEnabled(false);
					btRegister.repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btRegister.doClick();
			}
		});

		pPasswordField.add(Box.createRigidArea(new Dimension(40, 36)));
		pPasswordField.add(pfPassword);
	}

	/**
	 * This method created a <code>JPanel</code> which later on is used to hold a
	 * <code>JTextField</code> that allows the user to enter his email address.
	 */
	private void initPanelEmail() {

		pEmail = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

				g2.setColor(Color.GRAY);
				g2.setStroke(new BasicStroke(0.8F));
				g2.drawLine(30, 6, 30, getHeight() - 6);

				if (wrongUsername) {
					g2.setColor(Color.RED);
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
				}
			}
		};
		pEmail.setPreferredSize(new Dimension(250, 36));
		pEmail.setMaximumSize(pEmail.getPreferredSize());
		pEmail.setOpaque(true);
		pEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
		pEmail.setLayout(new BoxLayout(pEmail, BoxLayout.LINE_AXIS));
	}

	/**
	 * This method creates a <code>JTextField</code> in which the user can enter his
	 * email. It also adds a key listener to the text field which checks after every
	 * <code>KeyReleased</code> event whether the current email in the field fits
	 * the criteria for a email, specified in the API.
	 * 
	 * @see JTextField
	 * @see KeyListener
	 * @see CheckRegistrationDetails#verifyEmail(String)
	 */
	private void initTFEmail() {

		tfEmail = new JTextField("E-Mail");
		tfEmail.setForeground(Color.LIGHT_GRAY);
		tfEmail.setFont(new Font("Calibri", Font.BOLD, 13));
		tfEmail.setPreferredSize(new Dimension(185, 30));
		tfEmail.setMaximumSize(new Dimension(185, 30));
		tfEmail.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		tfEmail.setAlignmentY(Component.CENTER_ALIGNMENT);
		tfEmail.requestFocus(false);
		tfEmail.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

				if (tfEmail.getText().equals("")) {
					tfEmail.setForeground(Color.LIGHT_GRAY);
					tfEmail.setText("E-Mail");
					tfEmail.repaint();
				}

			}

			@Override
			public void focusGained(FocusEvent e) {

				if (tfEmail.getText().equals("E-Mail")) {
					tfEmail.setText("");
					tfEmail.setForeground(Color.BLACK);
					tfEmail.repaint();
				}

			}
		});
		tfEmail.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (tfEmail.getText().length() < 120) {
					wrongEmail = CheckRegistrationDetails.verifyEmail(tfEmail.getText());
					pEmail.repaint();
					if (wrongEmail || wrongUsername || wrongPassword) {
						btRegister.setEnabled(false);
						btRegister.repaint();
					} else {
						btRegister.setEnabled(true);
						btRegister.repaint();
					}
				} else {
					wrongUsername = true;
					pEmail.repaint();
					btRegister.setEnabled(false);
					btRegister.repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btRegister.doClick();

			}
		});
		pEmail.add(Box.createRigidArea(new Dimension(40, 36)));
		pEmail.add(tfEmail);
	}

	/*
	 * Creates a <code>JPanel</code> which later on is used to hold the
	 * "register button".
	 */
	private void initButtonPanel() {

		panel = new JPanel();
		panel.setMinimumSize(new Dimension(190, 60));
		panel.setPreferredSize(panel.getMinimumSize());
		panel.setMaximumSize(panel.getMinimumSize());
		panel.setBackground(new Color(0, 0, 0, 0));
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	}

	/**
	 * This method creates a <code>LoginButton</code> with the text "Sign up" which
	 * signs the user up when clicked.
	 */
	private void initButtonRegister() {

		btRegister = new LoginButton(guiMain, "Sign up", panel);
		btRegister.setPreferredSize(new Dimension(125, 36));
		btRegister.setMaximumSize(btRegister.getPreferredSize());
		btRegister.setOpaque(true);
		btRegister.setBorderPainted(false);
		btRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
		btRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btRegister.addActionListener(e -> {
			if (wrongEmail == false && wrongPassword == false && wrongUsername == false && btRegister.isEnabled()) {
				char[] c = pfPassword.getPassword();
				String password = String.valueOf(c);
				guiMain.registerNewUser(tfUsername.getText(), password, tfEmail.getText());
			}

		});

		panel.add(Box.createVerticalGlue());
		panel.add(btRegister);
		panel.add(Box.createVerticalGlue());
	}

	public String getUsername() {

		return tfUsername.getText();
	}

	public String getPassword() {

		char[] c = pfPassword.getPassword();
		return String.valueOf(c);
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
