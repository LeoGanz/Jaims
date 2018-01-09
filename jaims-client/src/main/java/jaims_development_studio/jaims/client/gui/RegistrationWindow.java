package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class RegistrationWindow extends JWindow {

	private JPanel				panelCenter, panelUsername, panelPassword, panelEmail, panelButtons;
	private JLabel				lblUsername, lblPassword, lblEmail;
	private JTextField			tfUsername, tfEmail;
	private JPasswordField		pfPassword;
	private JButton				btRegister, btStop;
	private String				username, pw, email;
	private GridBagConstraints	c	= new GridBagConstraints();
	private ClientMain			cm;

	public RegistrationWindow(JFrame caller, ClientMain cm) {

		super(caller);
		setSize(300, 380);
		setLocationRelativeTo(caller);
		this.cm = cm;

		initGUI();
	}

	private void initGUI() {

		panelCenter = new JPanel();
		panelCenter.setBorder(new CompoundBorder(new EmptyBorder(10, 5, 5, 5),
				new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Registration")));
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
		{

			panelUsername = new JPanel();
			panelUsername.setLayout(new BoxLayout(panelUsername, BoxLayout.PAGE_AXIS));
			panelUsername.setBorder(new EmptyBorder(25, 12, 0, 12));
			{
				lblUsername = new JLabel("<html><b><u><font size=3>Username:</html>", SwingConstants.LEFT);
				lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelUsername.add(lblUsername);
				panelUsername.add(Box.createRigidArea(new Dimension(0, 10)));

				tfUsername = new JTextField();
				tfUsername.setPreferredSize(new Dimension(200, 25));
				tfUsername.setMinimumSize(new Dimension(200, 25));
				tfUsername.setMaximumSize(new Dimension(200, 25));
				tfUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);
				tfUsername.requestFocus();
				tfUsername.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent arg0) {

						if (verifyUsername(tfUsername.getText())) {
							tfUsername.setBorder(new LineBorder(Color.black, 1));
							username = tfUsername.getText();
						} else
							tfUsername.setBorder(new LineBorder(Color.RED, 2));

						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							if ((tfUsername.getText() != null) && (verifyUsername(tfUsername.getText()) == true)) {
								username = tfUsername.getText();
								btRegister.doClick();
							} else
								tfUsername.setBorder(new LineBorder(Color.RED));
					}
				});
				panelUsername.add(tfUsername);

			}
			panelCenter.add(panelUsername);
			panelCenter.add(Box.createRigidArea(new Dimension(0, 30)));

			panelPassword = new JPanel();
			panelPassword.setLayout(new BoxLayout(panelPassword, BoxLayout.PAGE_AXIS));
			panelPassword.setBorder(new EmptyBorder(0, 12, 0, 12));
			{
				lblPassword = new JLabel("<html><b><u><font size=3>Password:</html>", SwingConstants.LEFT);
				lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelPassword.add(lblPassword);
				panelPassword.add(Box.createRigidArea(new Dimension(0, 10)));

				pfPassword = new JPasswordField();
				pfPassword.setMinimumSize(new Dimension(200, 25));
				pfPassword.setMaximumSize(new Dimension(200, 25));
				pfPassword.setPreferredSize(new Dimension(200, 25));
				pfPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);
				pfPassword.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent arg0) {

						pw = "";
						for (int i = 0; i < pfPassword.getPassword().length; i++)
							pw += pfPassword.getPassword()[i];

						if (verifyPassword(pw))
							pfPassword.setBorder(new LineBorder(Color.black, 1));
						else
							pfPassword.setBorder(new LineBorder(Color.RED, 2));

						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							if ((pw != null) && (verifyPassword(pw) == true))
								btRegister.doClick();
							else {
								pw = null;
								pfPassword.setBorder(new LineBorder(Color.RED, 2));
							}
					}
				});
				panelPassword.add(pfPassword);
			}
			panelCenter.add(panelPassword);
			panelCenter.add(Box.createRigidArea(new Dimension(0, 30)));

			panelEmail = new JPanel();
			panelEmail.setLayout(new BoxLayout(panelEmail, BoxLayout.PAGE_AXIS));
			panelEmail.setBorder(new EmptyBorder(0, 12, 0, 12));
			{
				lblEmail = new JLabel("<html><b><u><font size=3>E-Mail:</html>", SwingConstants.LEFT);
				lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelEmail.add(lblEmail);
				panelEmail.add(Box.createRigidArea(new Dimension(0, 10)));

				tfEmail = new JTextField();
				tfEmail.setMinimumSize(new Dimension(200, 25));
				tfEmail.setPreferredSize(new Dimension(200, 25));
				tfEmail.setMaximumSize(new Dimension(200, 25));
				tfEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
				tfEmail.addKeyListener(new KeyAdapter() {

					@Override
					public void keyReleased(KeyEvent arg0) {

						if (verifyEmail(tfEmail.getText())) {
							tfEmail.setBorder(new LineBorder(Color.black, 1));
							email = tfEmail.getText();
						} else
							tfEmail.setBorder(new LineBorder(Color.RED, 2));

						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							if ((tfEmail.getText() != null) && (verifyEmail(tfEmail.getText()) == true)) {
								email = tfEmail.getText();
								btRegister.doClick();
							} else
								tfEmail.setBorder(new LineBorder(Color.RED));
					}
				});
				panelEmail.add(tfEmail);
			}
			panelCenter.add(panelEmail);
			panelCenter.add(Box.createVerticalGlue());

			panelButtons = new JPanel();
			panelButtons.setLayout(new GridBagLayout());

			{
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				c.insets = new Insets(0, 8, 15, 8);
				panelButtons.add(Box.createVerticalGlue(), c);

				c.weightx = 0;
				c.weighty = 0;
				c.gridy = 1;

				btRegister = new JButton("Register");
				btRegister.setAlignmentX(Component.RIGHT_ALIGNMENT);
				btRegister.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				btRegister.setBackground(Color.GRAY);
				btRegister.setForeground(Color.BLUE);
				btRegister.setBorderPainted(false);
				btRegister.setMinimumSize(new Dimension(120, 40));
				btRegister.setPreferredSize(new Dimension(120, 40));
				btRegister.setMaximumSize(new Dimension(120, 40));
				btRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btRegister.addActionListener(e -> {
					pw = "";
					for (int i = 0; i < pfPassword.getPassword().length; i++)
						pw += pfPassword.getPassword()[i];

					if (verifyPassword(pw) && verifyUsername(tfUsername.getText()) && verifyEmail(tfEmail.getText()))
						sendRegistration(pw, username, email);

				});
				panelButtons.add(btRegister, c);

				c.gridx = 1;

				btStop = new JButton("Cancel");
				btStop.setAlignmentX(Component.LEFT_ALIGNMENT);
				btStop.setBackground(Color.GRAY);
				btStop.setForeground(Color.BLUE);
				btStop.setBorderPainted(false);
				btStop.setMinimumSize(new Dimension(120, 40));
				btStop.setPreferredSize(new Dimension(120, 40));
				btStop.setMaximumSize(new Dimension(120, 40));
				btStop.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btStop.addActionListener(e -> {
					cancel();

				});
				panelButtons.add(btStop, c);
			}
			panelCenter.add(panelButtons);

		}

		add(panelCenter);

		setVisible(true);
	}

	private Boolean verifyPassword(String password) {

		return password.matches("[A-Za-z1-9!\\?@\\(\\)\\{\\}\\[\\]\\\\/|<>=~$€%&#\\*-\\+.:,;'\"_]*");
	}

	private Boolean verifyEmail(String email) {

		return email
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}

	private Boolean verifyUsername(String username) {

		return username.matches("[A-Za-z1-9_.]*");
	}

	private void cancel() {

		dispose();
		LoginPanel.setBooleanWindow(false);
	}

	private void sendRegistration(String pw, String username, String email) {

		SendableRegistration sr = new SendableRegistration(username, pw, email);
		cm.getServerConnection().sendSendable(sr);
	}

}
