package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.networking.ListenForInput;
import jaims_development_studio.jaims.client.networking.ServerConnection;

public class LoginPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LoginPanel.class);
	
	private Image image;
	private boolean tField, pwField;
	private static boolean registrationExists;
	private String username, pw, oldUsername = "";
	
	private JLabel lblImg, lblUsername, lblPassword, lblRegistrationLink;
	private JTextField tfUsername;
	private JPasswordField jpPassword;
	private JPanel panelCenter, panelSouth;
	private JPanel panel;
	private JButton btLogin;
	private GridBagConstraints c = new GridBagConstraints();
	


	
	public LoginPanel(JFrame caller, ClientMain cm) {
		setLayout(new BorderLayout());
		initGUI(caller, cm);
	}
	
	private void initGUI(JFrame caller, ClientMain cm) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/images/speech-md.png"));
			image = image.getScaledInstance(200, 120, Image.SCALE_SMOOTH);
			lblImg = new JLabel(new ImageIcon(image));
			lblImg.setPreferredSize(new Dimension(202, 130));
			add(lblImg, BorderLayout.NORTH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		panelCenter = new JPanel();
		panelCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelCenter.setLayout(new BorderLayout());
		{
			JPanel panelCenterCenter = new JPanel();
			panelCenterCenter.setLayout(new BorderLayout());
			{
				//panelCenterCenter.add(Box.createVerticalGlue(), BorderLayout.CENTER);
				lblUsername = new JLabel("<html><b><font size = 4><font color = #000000>Username</html>", SwingConstants.CENTER);
				lblPassword = new JLabel("<html><b><font size =4><font color = #000000>Password</html>", SwingConstants.CENTER);
				
				tfUsername = new JTextField();
				tfUsername.setPreferredSize(new Dimension(200, 25));
				tfUsername.addKeyListener(new KeyAdapter() {
					
					@Override
					public void keyReleased(KeyEvent arg0) {
						if (verifyUsername(tfUsername.getText())) {
							tfUsername.setBorder(new LineBorder(Color.black, 1));
							tField = true;
							username = tfUsername.getText();
						} else {
							tfUsername.setBorder(new LineBorder(Color.RED, 2));
							tField = false;
						}
						
						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							if ((tfUsername.getText() != null) && (verifyUsername(tfUsername.getText()) == true)) {
								username = tfUsername.getText();
								tField = true;
								btLogin.doClick();
							} else
								tfUsername.setBorder(new LineBorder(Color.RED));
					}
				});
				
				jpPassword = new JPasswordField();
				jpPassword.setPreferredSize(new Dimension(200, 25));
				jpPassword.addKeyListener(new KeyAdapter() {
					
					@Override
					public void keyReleased(KeyEvent arg0) {
						pw = "";
						for (int i = 0; i < jpPassword.getPassword().length; i++)
							pw += jpPassword.getPassword()[i];
						
						if (verifyPassword(pw)) {
							jpPassword.setBorder(new LineBorder(Color.black, 1));
							pwField = true;
						} else {
							jpPassword.setBorder(new LineBorder(Color.RED, 2));
							pwField = false;
						}
						if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
							if ((pw != null) && (verifyPassword(pw) == true)) {
								pwField = true;
								btLogin.doClick();
							} else {
								pw = null;
								jpPassword.setBorder(new LineBorder(Color.RED, 2));
							}
					}
				});
				
				panel = new JPanel();
				panel.setLayout(new GridBagLayout());
				{
					c.insets = new Insets(5, 8, 10, 8);
					c.fill = GridBagConstraints.NONE;
					c.gridx = 0;
					c.gridy = 0;
					panel.add(lblUsername, c);
					
					c.gridx = 1;
					panel.add(tfUsername, c);
					
					c.gridx = 0;
					c.gridy = 1;
					panel.add(lblPassword, c);
					
					c.gridx = 1;
					panel.add(jpPassword, c);
				}
				panelCenterCenter.add(panel, BorderLayout.CENTER);
				
			}
			panelCenter.add(panelCenterCenter, BorderLayout.CENTER);
			
			JPanel panelCenterSouth = new JPanel();
			panelCenterSouth.setLayout(new BoxLayout(panelCenterSouth, BoxLayout.X_AXIS));
			{
				btLogin = new JButton("Login");
				btLogin.setBorderPainted(false);
				btLogin.setFont(new Font("Cailbri", Font.BOLD, 12));
				btLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btLogin.setPreferredSize(new Dimension(120, 40));
				btLogin.addActionListener(arg0 -> {
					if ((pwField == true) && (tField == true)) {
						
						
						
					}
					
				});
				btLogin.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						sendLogin(tfUsername.getText(), pw);
						caller.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						cm.startCreatingChatWindow(tfUsername.getText());
					}
				});
			}
			
			
			panelCenterSouth.add(Box.createHorizontalGlue());
			panelCenterSouth.add(btLogin);
			panelCenterSouth.add(Box.createHorizontalGlue());
			panelCenter.add(panelCenterSouth, BorderLayout.SOUTH);
		}
		add(panelCenter, BorderLayout.CENTER);
		
		panelSouth = new JPanel();
		panelSouth.setBorder(new EmptyBorder(10, 0, 5, 0));
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.LINE_AXIS));
		{
			lblRegistrationLink = new JLabel("", SwingConstants.CENTER);
			lblRegistrationLink.setText("<html><u><font color = #0000FF> Noch kein Konto? Hier gehts zur Anmeldung</html>");
			lblRegistrationLink.setMaximumSize(new Dimension(lblRegistrationLink.getFontMetrics(lblRegistrationLink.getFont()).stringWidth(lblRegistrationLink.getText()), 20));
			lblRegistrationLink.setPreferredSize(lblRegistrationLink.getMaximumSize());
			lblRegistrationLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lblRegistrationLink.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if (registrationExists == false) {
						registrationExists = true;
						new RegistrationWindow(caller);
					} else {
						
					}
					
				}
				
			});
			panelSouth.add(Box.createHorizontalGlue());
			panelSouth.add(lblRegistrationLink);
			panelSouth.add(Box.createHorizontalGlue());
		}
		add(panelSouth, BorderLayout.SOUTH);
	
	}
	
	public void addConnectionError(ServerConnection sc) {
		btLogin.setEnabled(false);
		lblRegistrationLink.setEnabled(false);
		
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;		
		ConnectionErrorPanel cep = new ConnectionErrorPanel();
		panel.add(cep, c);
		panel.revalidate();
		panel.repaint();
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				Socket s = new Socket();
				while (s.isConnected() == false) {
					try {
						s = new Socket();
						s.connect(new InetSocketAddress("localhost", 6000), 0);
						Thread.sleep(600);
					} catch (InterruptedException | IOException e) {

					}
				}
				LOG.info("Connected");
				btLogin.setEnabled(true);
				lblRegistrationLink.setEnabled(true);
				panel.remove(cep);
				panel.repaint();
				sc.setSocket(s);
				
				Thread t  = new Thread(new ListenForInput(s));
				t.start();
			}
		};
		thread.start();
		
	}
	
	private Boolean verifyUsername(String username) {
		return username.matches("[A-Za-z1-9_.]*");
	}
	
	private Boolean verifyPassword(String password) {
		return password.matches("[A-Za-z1-9!\\?@\\(\\)\\{\\}\\[\\]\\\\/|<>=~$€%&#\\*-\\+.:,;'\"_]*");
	}
	
	public static void setBooleanWindow(Boolean b) {
		registrationExists = b;
	}
	
	private void sendLogin(String username, String pw) {
		SendableLogin sl = new SendableLogin(username, pw);
		ServerConnection.sendSendable(sl);
	}
	

}
