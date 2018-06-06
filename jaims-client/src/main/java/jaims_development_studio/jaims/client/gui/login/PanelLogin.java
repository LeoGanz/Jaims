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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.api.util.CheckRegistrationDetails;
import jaims_development_studio.jaims.client.gui.GUIMain;

/**
 * This class represents a login panel which is shown on the startup frame.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class PanelLogin extends JPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private JTextField			tfUsername;
	private JPasswordField		pfPassword;
	private boolean				wrongUsername		= false, wrongPassword = false;
	private LoginButton			btLogin;
	private JPanel				pTextField, pPasswordField, loginBtPanel;
	private JLabel				lblForgotPassword;
	private JCheckBox			jcb;

	private char				defaultCharEcho;

	public PanelLogin(GUIMain guiMain) {

		this.guiMain = guiMain;

		initGUI();
		checkLogin();
	}

	private void checkLogin() {

		String[] arr = guiMain.getLogin();
		if (arr.length > 0) {
			guiMain.sendLogin(arr[0], arr[1]);
			tfUsername.setText(arr[0]);
			pfPassword.setText(arr[1]);
		}

	}

	/**
	 * <code>initGUI()</code> is responsible for calling all methods that are needed
	 * in order to build the panel:
	 * 
	 * <ul>
	 * <li>{@link #initPanelTextField()}</li>
	 * <li>{@link #initTFUsername()}</li>
	 * <li>{@link #initPanelPasswordField()}</li>
	 * <li>{@link #initPFPassword()}</li>
	 * <li>{@link #initCheckBox()}</li>
	 * <li>{@link #initLoginPanelWithButton()}</li>
	 * <li>{@link #initLabelForgotPassword()}</li>
	 * </ul>
	 */
	private void initGUI() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		initPanelTextField();
		initTFUsername();
		initPanelPasswordField();
		initPFPassword();
		initCheckBox();
		initLoginPanelWithButton();
		initLabelForgotPassword();

		add(Box.createVerticalGlue());
		add(pTextField);
		add(Box.createRigidArea(new Dimension(0, 35)));
		add(pPasswordField);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(jcb);
		add(Box.createVerticalGlue());
		add(loginBtPanel);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(lblForgotPassword);
		add(Box.createRigidArea(new Dimension(0, 5)));
	}

	/**
	 * Creates a rounded <code>JPanel</code> which holds a text field that allows
	 * the user to enter his username for the login.
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
	 * Creates a <code>JTextField</code> in which the user can type his username
	 * which is needed for the login. It also checks if the entered username
	 * complies with the username criteria specified by the API.
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

	/**
	 * Creates a new <code>JPanel</code> which later on holds the
	 * <code>JPasswordField</code> that the user needs to enter the password for his
	 * login.
	 * 
	 * @see JPanel
	 * @see JPasswordField
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
					g2.setStroke(new BasicStroke(2.5F));
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
	 * Creates the <code>JPasswordField</code> which is added to the JPanel created
	 * in {@link #initPanelPasswordField()}. This method also adds a
	 * <code>KeyListener</code> to the <code>JPasswordField</code> so that after
	 * every letter the user typed the password can be checked on whether it meets
	 * the criteria defined by the API.
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

				if (new String(pfPassword.getPassword()).equals("")) {
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

	/**
	 * Creates a small <code>JCheckBox</code> that allows the user to stay logged
	 * in.
	 */
	private void initCheckBox() {

		jcb = new JCheckBox("Remember me");
		jcb.setSelected(false);
		jcb.setForeground(Color.WHITE);
		jcb.setPreferredSize(new Dimension(185, 25));
		jcb.setMaximumSize(jcb.getPreferredSize());
		jcb.setOpaque(false);
		jcb.setBackground(new Color(0, 0, 0, 0));
		jcb.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		jcb.setAlignmentX(Component.CENTER_ALIGNMENT);
		jcb.setFont(new Font("Sans Serif", Font.PLAIN, 13));
		jcb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (jcb.isSelected()) {
					char[] c = pfPassword.getPassword();
					String password = String.valueOf(c);
					guiMain.setRememberMe(true, tfUsername.getText(), password);
				} else {
					guiMain.setRememberMe(false, "", "");
				}

			}
		});
	}

	/**
	 * Creates a <code>JPanel</code> that holds the <code>LoginButton</code>.
	 * 
	 * @see LoginButton
	 */
	private void initLoginPanelWithButton() {

		loginBtPanel = new JPanel();
		loginBtPanel.setMinimumSize(new Dimension(190, 60));
		loginBtPanel.setPreferredSize(loginBtPanel.getMinimumSize());
		loginBtPanel.setMaximumSize(loginBtPanel.getMinimumSize());
		loginBtPanel.setBackground(new Color(0, 0, 0, 0));
		loginBtPanel.setOpaque(false);
		loginBtPanel.setLayout(new BoxLayout(loginBtPanel, BoxLayout.PAGE_AXIS));

		btLogin = new LoginButton(guiMain, "Login", loginBtPanel);
		btLogin.addActionListener(e -> {

			char[] c = pfPassword.getPassword();
			String password = String.valueOf(c);
			if (wrongPassword == false && wrongUsername == false && tfUsername.getText().equals("Username") == false
					&& password.equals("Password") == false) {
				guiMain.sendLogin(tfUsername.getText(), password);
			}

			if (jcb.isSelected())
				guiMain.setRememberMe(true, tfUsername.getText(), password);

		});
		loginBtPanel.add(Box.createVerticalGlue());
		loginBtPanel.add(btLogin);
		loginBtPanel.add(Box.createVerticalGlue());
	}

	/**
	 * Creates a <code>JLabel</code> on which the user can click if he forgot is
	 * password.
	 * 
	 * @see GUIMain#askForNewPassword()
	 */
	private void initLabelForgotPassword() {

		lblForgotPassword = new JLabel("<html><u>Forgot password ?</u></html>", JLabel.CENTER);
		lblForgotPassword.setMaximumSize(new Dimension(100, 25));
		lblForgotPassword.setPreferredSize(lblForgotPassword.getMaximumSize());
		lblForgotPassword.setOpaque(false);
		lblForgotPassword.setForeground(Color.WHITE);
		lblForgotPassword.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Receives a boolean and decides based on its value whether to set the
	 * <code>LoginButton</code> enabled or disabled.
	 * 
	 * @param enabled
	 *            indicates whether to disable/enable login button
	 * 
	 * @see LoginButton
	 */
	public void setLoginEnabled(boolean enabled) {

		btLogin.setEnabled(enabled);
		if (enabled)
			btLogin.setToolTipText("");
		else
			btLogin.setToolTipText("Could not connect to the server!");
		btLogin.repaint();
		repaint();
	}

	/**
	 * Receives two strings that have to be inserted into the text field for the
	 * username and the password field.
	 * 
	 * @param username
	 *            the username to be inserted
	 * @param password
	 *            the password to be inserted
	 */
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

	/**
	 * @see CheckRegistrationDetails
	 * 
	 * @param username
	 *            the username to be checked
	 * @return whether the username fits the criteria
	 */
	private Boolean verifyUsername(String username) {

		return CheckRegistrationDetails.verifyUsername(username);
	}

	/**
	 * @see CheckRegistrationDetails
	 * 
	 * @param password
	 *            the password to be checked
	 * @return whether the password fits the criteria
	 */
	private Boolean verifyPassword(String password) {

		return CheckRegistrationDetails.verifyPassword(password);
	}

	/**
	 * Notifies the user that the entered username was wrong by drawing a red border
	 * around the <code>pTextField</code>.
	 * 
	 * @param username
	 *            boolean indicating a wrong username
	 * 
	 * @see #initPanelTextField()
	 */
	public void setWrongUsername(boolean username) {

		wrongUsername = username;
		repaint();
	}

	/**
	 * Notifies the user that the entered password was wrong by drawing a red border
	 * around the <code>pPasswordField</code>.
	 * 
	 * @param wrongPassword
	 *            boolean indicating a wrong password
	 * 
	 * @see #initPanelPasswordField()
	 */
	public void setWrongPassword(boolean wrongPassword) {

		this.wrongPassword = wrongPassword;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}