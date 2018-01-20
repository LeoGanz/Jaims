package jaims_development_studio.jaims.client.logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.audio.SelectAudioDevices;
import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.ClientProfile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.database.WriteToDatabase;
import jaims_development_studio.jaims.client.gui.AddSign;
import jaims_development_studio.jaims.client.gui.ContactPanel;
import jaims_development_studio.jaims.client.gui.ContainerPanel;
import jaims_development_studio.jaims.client.gui.JaimsFrame;
import jaims_development_studio.jaims.client.gui.LoginPanel;
import jaims_development_studio.jaims.client.gui.PanelAccount;
import jaims_development_studio.jaims.client.gui.PanelAddUser;
import jaims_development_studio.jaims.client.gui.PanelChat;
import jaims_development_studio.jaims.client.gui.PanelChatMessages;
import jaims_development_studio.jaims.client.gui.PanelContactsAndChats;
import jaims_development_studio.jaims.client.gui.PanelEditUser;
import jaims_development_studio.jaims.client.gui.PanelSettings;
import jaims_development_studio.jaims.client.gui.PanelUserProfileInformation;
import jaims_development_studio.jaims.client.gui.RecordingFrame;
import jaims_development_studio.jaims.client.gui.SettingDots;
import jaims_development_studio.jaims.client.networking.ServerConnection;
import jaims_development_studio.jaims.client.settings.Settings;

public class ClientMain {

	private static final Logger			LOG						= LoggerFactory.getLogger(ClientMain.class);
	public static boolean				confirmationRecieved	= false;
	private JPanel						userUILeftSide			= new JPanel();
	private ContactPanel				activeContactPanel;
	private PanelChat					activePanelChat;
	private PanelSettings				panelSettings;
	private ServerConnection			sc;
	private WriteToDatabase				wtd;
	private Thread						threadDatabaseManagement, threadPCC;
	private DatabaseConnection			dc;
	private PanelContactsAndChats		pcc;
	private JaimsFrame					jf;
	private LoginPanel					lp;
	private PanelEditUser				panelEditUser;
	private PanelUserProfileInformation	panelUserProfileInformation;
	private ContainerPanel				container				= new ContainerPanel();
	private Settings					settings;
	private String						username;
	private SelectAudioDevices			selectAudioDevices;

	/**
	 * Static profile which represents the logged-in user.
	 */
	public static ClientProfile			userProfile;
	public static UUID					serverUUID;

	/**
	 * Main method of program which takes args as given start arguments and creates
	 * a new instance of <code>ClientMain</code>.
	 *
	 * @param args
	 *            given start arguments.
	 */
	public static void main(String[] args) {

		try {
			new ClientMain();
		} catch (Exception e) {
			LOG.error("Fatal error!", e);
		}
	}

	/*
	 * Constructor of class <code>ClientMain</code>. Calls a method which
	 * initialises the program.
	 */
	public ClientMain() {

		UIManager.put("ScrollBarUI", "jaims_development_studio.jaims.client.gui.MyScrollBarUI");
		initProgram();
	}

	/**
	 * Begins to initialise the program by doing the following:
	 * <ul>
	 * <li>initialising a new <code>JaimsFrame</code></li>
	 * <li>opening up a new <code>DatabaseConnection</code></li>
	 * <li>starting a new Thread which tries to connect to the server</li>
	 * <li>initialising a new <code>ServerConnection</code></li>
	 * <li>adding a new <code>LoginPanel</code> to the JFrame</li>
	 * </ul>
	 */
	private void initProgram() {

		jf = new JaimsFrame();

		dc = new DatabaseConnection();
		dc.initConnection();

		userProfile = new ClientProfile(null, "Bu88le", "Test", "Test", null, new Date(System.currentTimeMillis()));

		Thread thread = new Thread(sc = new ServerConnection(this));
		thread.start();

		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			LOG.error("Sleep interrupted");
		}

		jf.initGUI();

		lp = new LoginPanel(jf, this);
		if (!sc.checkIfServerIsAvailable())
			lp.addConnectionError();

		jf.getContentPane().add(lp, BorderLayout.CENTER);
		jf.getContentPane().revalidate();

	}

	/**
	 * Starts creating the UI which is displayed after a successful login. Shows the
	 * own profile, all open chats and all contacts.
	 *
	 * @param username
	 *            the name used by the user for login
	 */
	public void startCreatingChatWindow(String username) {

		this.username = username;
		deserializeSettings(username);

		// creates a new WriteToDatabase Object with the username and the connection
		// object stored in DatabaseConnection
		wtd = dc.getWTD(username);

		// local variable thread which starts loading the content from the database into
		// the program
		Thread thread = dc.readFromDatabase(username);
		try {
			thread.join();
		} catch (InterruptedException e) {
			LOG.error("Failed to join thread", e);
		}

		// local variable pa which represents a panel which shows the username and the
		// profile picture of the logged-in user.
		PanelAccount pa = null;
		try {
			pa = new PanelAccount(ImageIO.read(getClass().getResourceAsStream("/images/JAIMS_Penguin.png")), username);
			pa.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {

					addPanelEditUser();

				}
			});
		} catch (IOException e1) {
			LOG.error("Failed to read image from file", e1);
		}

		// local variable JPanel controlPanel, which is used to hold the PanelAccount,
		// the
		// SettingDots and the AddUser sign
		//
		// Has a gray Border and uses a horizontally laid out BoxLayout
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new LineBorder(Color.GRAY));
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
		controlPanel.add(pa); // adds the PanelAccount to the JPanel
		controlPanel.add(Box.createHorizontalGlue()); // pushes all components, added from now on, to the right side of
														// the JPanel
		controlPanel.add(new AddSign(this)); // adds a new JPanel which shows an 'add sign'
		controlPanel.add(Box.createRigidArea(new Dimension(3, 0))); // creates a blank space between the two components
		controlPanel.add(new SettingDots(this)); // adds a new JPanel which shows dots

		// creates and starts a thread which creates a new PanelContactsAndChats.
		// PanelContactsAndChats is a JTabbedPane which shows all open chats and all
		// contacts.
		threadPCC = new Thread(pcc = new PanelContactsAndChats(jf, this));
		threadPCC.start();

		try {
			// waits for the thread to finish
			threadPCC.join();
		} catch (InterruptedException e) {
			LOG.error("Failed to join the thread", e);
		}

		jf.getContentPane().removeAll(); // removes everything from the frame's content pane

		// creates a new JPanel with a white background and a BorderLayout.
		// This panel is used to display the left side of the UI which is shown to the
		// user after the login, which means the controlPanel and the JTabbedPane
		// PanelContactsAndChats
		userUILeftSide = new JPanel();
		userUILeftSide.setBackground(Color.WHITE);
		userUILeftSide.setLayout(new BorderLayout());
		userUILeftSide.add(controlPanel, BorderLayout.PAGE_START); // Adds the controlPanel
		userUILeftSide.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER); // adds a blank space of 20
																							// px height
		// between the controlPanel and the
		// PanelContactsAndChats
		userUILeftSide.add(pcc, BorderLayout.PAGE_END); // adds the PanelContactsAndChats

		jf.getContentPane().add(userUILeftSide, BorderLayout.LINE_START); // add the userUILeftSide panel to the fraim's
																			// content pane
		jf.getContentPane().setBackground(Color.WHITE);

		// revalidates and repaints the userUILeftSide and the frame's content pane to
		// have it shown properly
		userUILeftSide.revalidate();
		userUILeftSide.repaint();
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();

		jf.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // sets the Cursor from Waiting Cursor back to
																			// Default cursor

		selectAudioDevices = new SelectAudioDevices(this);
	}

	/**
	 * Receives a finished PanelChat and adds it to the frame's contentpane. Sets
	 * the given PanelChat as the active PanelChat after it was shown.
	 *
	 * @param panelChat
	 *            a finished PanelChat belonging to a specific contact
	 */
	public void setMessagePanel(PanelChat panelChat) {

		jf.getContentPane().remove(container);
		jf.revalidate();

		jf.getContentPane().add(panelChat, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();

		panelChat.getSP().getVerticalScrollBar().setValue(1000);
		panelChat.getSP().repaint();
		container = panelChat;
		activePanelChat = panelChat;
	}

	public void setSettingPanel() {

		jf.getContentPane().removeAll();
		if (panelSettings == null)
			panelSettings = new PanelSettings(this, selectAudioDevices);

		jf.getContentPane().add(panelSettings, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
	}

	public void removeSettingPanel() {

		jf.getContentPane().remove(panelSettings);
		jf.getContentPane().revalidate();

		jf.getContentPane().add(userUILeftSide, BorderLayout.LINE_START);
		jf.getContentPane().add(container, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
	}

	/**
	 * Receives a <code>ChatObjects</code> and uses it to build a
	 * <code>RecordingFrame</code>.
	 *
	 * @param co
	 *            a ChatObject which is given to the RecordingFrame's constructor
	 */
	public void showRecordFrame(ChatObject co) {

		RecordingFrame rf = new RecordingFrame(activePanelChat, co, this);
		rf.setLocationRelativeTo(activePanelChat);
		jf.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentMoved(ComponentEvent e) {

				rf.setLocationRelativeTo(jf);
			}
		});
		rf.setVisible(true);

	}

	/**
	 * Repaints the panel userUILeftSide and the frame's contentpane.
	 */
	public void repaintPanelLeft() {

		userUILeftSide.repaint();
		jf.getContentPane().repaint();
	}

	public void addPanelEditUser() {

		jf.getContentPane().remove(container);
		jf.getContentPane().revalidate();

		container = new PanelEditUser(this);
		jf.getContentPane().add(container, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
	}

	public void addPanelAddUser() {

		jf.getContentPane().removeAll();
		jf.getContentPane().revalidate();

		PanelAddUser pa = new PanelAddUser(this);
		jf.getContentPane().add(pa, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
	}

	public void removePanelAddUser() {

		jf.getContentPane().removeAll();
		jf.getContentPane().revalidate();

		jf.getContentPane().add(userUILeftSide, BorderLayout.LINE_START);
		jf.getContentPane().add(container, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();
	}

	public void addPanelSelectProfileImage() {

	}

	public void repaintPanelSelectProfileImage() {

	}

	public void addPanelUserProfileInformation(PanelChatMessages pcm, ClientProfile userProfile) {

		jf.getContentPane().remove(container);
		jf.getContentPane().revalidate();

		jf.getContentPane().add(panelUserProfileInformation = new PanelUserProfileInformation(this, userProfile, pcm),
				BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();

		container = panelUserProfileInformation;
	}

	/**
	 * Receives a ContactPanel and sets it to the activeContactPanel.
	 *
	 * @param cp
	 *            the last clicked ContactPanel
	 */
	public void setAcvtiveContactPanel(ContactPanel cp) {

		// activeContactPanel = cp;
	}

	/**
	 *
	 * @return a JaimsFrame object
	 */
	public JaimsFrame getJaimsFrame() {

		return jf;
	}

	/**
	 *
	 * @return a WriteToDatabase object
	 */
	public WriteToDatabase getWTD() {

		return wtd;
	}

	/**
	 *
	 * @return a PanelChatsAndContacts object
	 */
	public PanelContactsAndChats getPCC() {

		return pcc;
	}

	/**
	 *
	 * @return a LoginPanel object
	 */
	public LoginPanel getLoginPanel() {

		return lp;
	}

	public ServerConnection getServerConnection() {

		return sc;
	}

	public PanelChat getActivePanelChat() {

		return activePanelChat;
	}

	public Settings getSetting() {

		return settings;
	}

	private void deserializeSettings(String username) {

		String userHome = System.getProperty("user.home").replace("\\", "/");
		String filename = userHome + "/Jaims/" + username + "/settings/settings.set";
		String path = userHome + "/Jaims/" + username;

		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = new FileInputStream(filename);
			ois = new ObjectInputStream(fin);
			settings = (Settings) ois.readObject();

		} catch (FileNotFoundException fnfe) {
			settings = new Settings();
			createDirectory(path);
			File settingFile = new File(filename);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingFile))) {

				oos.writeObject(settings);
				System.out.println("Done");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void createDirectory(String path) {

		File f = new File(path);
		if (!f.exists())
			f.mkdirs();

		File settings = new File(path + "/settings");
		if (!settings.exists())
			settings.mkdirs();
	}

	public String getUsername() {

		return username;
	}

	public SelectAudioDevices getSAD() {

		return selectAudioDevices;
	}
}