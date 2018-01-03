package jaims_development_studio.jaims.client.logic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.database.WriteToDatabase;
import jaims_development_studio.jaims.client.gui.AddSign;
import jaims_development_studio.jaims.client.gui.ContactPanel;
import jaims_development_studio.jaims.client.gui.JaimsFrame;
import jaims_development_studio.jaims.client.gui.LoginPanel;
import jaims_development_studio.jaims.client.gui.PanelAccount;
import jaims_development_studio.jaims.client.gui.PanelChat;
import jaims_development_studio.jaims.client.gui.PanelContactsAndChats;
import jaims_development_studio.jaims.client.gui.PanelSettings;
import jaims_development_studio.jaims.client.gui.RecordingFrame;
import jaims_development_studio.jaims.client.gui.SettingDots;
import jaims_development_studio.jaims.client.networking.ServerConnection;

public class ClientMain {

	private static final Logger	LOG						= LoggerFactory.getLogger(ClientMain.class);
	public static boolean		confirmationRecieved	= false;
	Point						p						= new Point(10, 5);
	JPanel						userUILeftSide			= new JPanel();
	Rectangle					r						= new Rectangle(150, 150, 500, 500);
	ContactPanel				activeContactPanel;
	PanelChat					activePanelChat;
	PanelSettings				panelSettings;
	ServerConnection			sc;
	WriteToDatabase				wtd;
	Thread						threadDatabaseManagement, threadPCC;
	DatabaseConnection			dc;
	PanelContactsAndChats		pcc;
	JaimsFrame					jf;
	LoginPanel					lp;

	/**
	 * Static profile which represents the logged-in user.
	 */
	public static Profile		userProfile;

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

		Thread thread = new Thread(sc = new ServerConnection(this));
		thread.start();

		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			LOG.error("Sleep interrupted");
		}

		jf.initGUI();

		lp = new LoginPanel(jf, this);
		jf.getContentPane().add(lp, BorderLayout.CENTER);
		jf.getContentPane().revalidate();

		if (sc.getSocket().isClosed() || sc.getSocket().isConnected() == false)
			lp.addConnectionError(sc);
	}

	/**
	 * Starts creating the UI which is displayed after a successful login. Shows the
	 * own profile, all open chats and all contacts.
	 *
	 * @param username
	 *            the name used by the user for login
	 */
	public void startCreatingChatWindow(String username) {
		// creates a new WriteToDatabase Object with the username and the connection
		// object stored in DatabaseConnection
		wtd = dc.getWTD(username);
		userProfile = new Profile(null, username, "Test", "Test", null, new Date(System.currentTimeMillis()));

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

	}

	/**
	 * Receives a finished PanelChat and adds it to the frame's contentpane. Sets
	 * the given PanelChat as the active PanelChat after it was shown.
	 *
	 * @param panelChat
	 *            a finished PanelChat belonging to a specific contact
	 */
	public void setMessagePanel(PanelChat panelChat) {
		// if panelSettings is shown then it is first removed before adding the
		// panelChat
		if (panelSettings != null) {
			jf.getContentPane().remove(panelSettings);
			panelSettings.getPF().dispose();
			panelSettings = null;
			jf.revalidate();
		}
		// if there is a activePanelChat then it is first removed before adding the
		// panelChat
		if (activePanelChat != null) {
			jf.getContentPane().remove(activePanelChat);
			jf.getContentPane().revalidate();
		}
		jf.getContentPane().add(panelChat, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();

		activePanelChat = panelChat;
	}

	/**
	 * Receives a finished PanelSettings and shows it in the centre of the frame's
	 * content pane.
	 *
	 * @param panelSettings
	 *            a finished PanelSettings which shows all available setting options
	 */
	public void setSettingPanel(PanelSettings panelSettings) {
		// if there is a panelChat currently active then first remove it before showing
		// the panelSettings
		if (activePanelChat != null) {
			jf.getContentPane().remove(activePanelChat);
			jf.revalidate();
		}
		jf.getContentPane().add(panelSettings, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		jf.getContentPane().repaint();

		this.panelSettings = panelSettings;
	}

	/**
	 * Receives a <code>ChatObjects</code> and uses it to build a
	 * <code>RecordingFrame</code>.
	 *
	 * @param co
	 *            a ChatObject which is given to the RecordingFrame's constructor
	 */
	public void showRecordFrame(ChatObject co) {
		RecordingFrame rf = new RecordingFrame(activePanelChat, co);
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

	/**
	 * Receives a ContactPanel and sets it to the activeContactPanel.
	 *
	 * @param cp
	 *            the last clicked ContactPanel
	 */
	public void setAcvtiveContactPanel(ContactPanel cp) {
		activeContactPanel = cp;
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
}