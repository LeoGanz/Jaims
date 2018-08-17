package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.audio.ListAudioDevices;
import jaims_development_studio.jaims.client.chatObjects.ChatInformation;
import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.gui.customGUIComponents.CenterPanel;
import jaims_development_studio.jaims.client.gui.customGUIComponents.EventManager;
import jaims_development_studio.jaims.client.gui.customGUIComponents.FrameTop;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.dfe.DFEObject;
import jaims_development_studio.jaims.client.gui.login.PanelProgramStartup;
import jaims_development_studio.jaims.client.gui.login.ShowGuiBuildingProcess;
import jaims_development_studio.jaims.client.gui.messagePanels.ManageMessagePanels;
import jaims_development_studio.jaims.client.gui.messagePanels.PanelChat;
import jaims_development_studio.jaims.client.gui.settings.PanelSelectSettings;
import jaims_development_studio.jaims.client.gui.showContacts.PanelContactShowing;
import jaims_development_studio.jaims.client.gui.showContacts.PanelTabbedPane;
import jaims_development_studio.jaims.client.gui.showContacts.PanelUserShowing;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class GUIMain implements Runnable {

	private static final Logger				LOG			= LoggerFactory.getLogger(GUIMain.class);

	private JaimsFrame						jaimsFrame;
	private ClientMain						cm;
	private PanelProgramStartup				panelProgramStartup;
	private ShowGuiBuildingProcess			showGuiBuildingProcess;
	private PanelTabbedPane					panelTabbedPane;
	private ManageMessagePanels				manageMessagePanels;
	private CenterPanel						centerPanel;
	private PanelUserShowing				panelUserShowing;
	private JPanel							infoPanel;
	private HashMap<UUID, SimpleContact>	allContacts	= new HashMap<>();
	private ParentPanel						parentPanel;
	private PanelAddUser					panelAddUser;
	private PanelSelectSettings				panelSelectSettings;
	private ListAudioDevices				listAudioDevices;
	private boolean							showSplashScreen;
	private EventManager					eventManager;

	public GUIMain(ClientMain cm, boolean showSplashScreen) {

		this.cm = cm;
		this.showSplashScreen = showSplashScreen;
	}

	private void initFrame() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("ScrollBarUI",
					"jaims_development_studio.jaims.client.gui.customGUIComponents.CustomScrollBarUI");
			UIManager.put("ScrollBar.width", 8);
			ToolTipManager.sharedInstance().setInitialDelay(250);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		jaimsFrame = new JaimsFrame(showSplashScreen);
		if (showSplashScreen) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOG.error("Interrupted Sleep");
			}
		}
		jaimsFrame.initLogin();

		panelProgramStartup = new PanelProgramStartup(this);
		jaimsFrame.getContentPane().add(panelProgramStartup, BorderLayout.CENTER);
		jaimsFrame.setVisible(true);

	}

	public void showLoginPanel() {

	}

	public void showStandardPanel() {

	}

	public void showParentPanel(ParentPanel pp) {

		if (parentPanel != null) {
			if (parentPanel.getPanelUUID().equals(pp.getPanelUUID()) == false) {
				manageMessagePanels.updateChatPanel(parentPanel);
				centerPanel.remove(parentPanel);

				centerPanel.add(parentPanel = pp, BorderLayout.CENTER);
				centerPanel.revalidate();
				jaimsFrame.getContentPane().revalidate();
				jaimsFrame.getContentPane().repaint();
			}
		} else {
			centerPanel.add(parentPanel = pp, BorderLayout.CENTER);
			centerPanel.revalidate();
			jaimsFrame.getContentPane().revalidate();
			jaimsFrame.getContentPane().repaint();
		}

	}
	
	public ParentPanel getCurrentParentPanel() {
		return parentPanel;
	}

	public void showParentPanel(ParentPanel pp, PanelContactShowing caller) {

		if (parentPanel != null) {
			if (parentPanel.getPanelUUID().equals(pp.getPanelUUID()) == false && parentPanel instanceof PanelChat) {
				manageMessagePanels.updateChatPanel(parentPanel);
				centerPanel.remove(parentPanel);

				centerPanel.add(parentPanel = pp, BorderLayout.CENTER);
				centerPanel.revalidate();
				jaimsFrame.getContentPane().revalidate();
				jaimsFrame.getContentPane().repaint();
			} else {

				centerPanel.remove(parentPanel);

				centerPanel.add(parentPanel = pp, BorderLayout.CENTER);
				centerPanel.revalidate();
				jaimsFrame.getContentPane().revalidate();
				jaimsFrame.getContentPane().repaint();
				caller.revalidate();
				caller.repaint();
			}
		} else {

			centerPanel.add(parentPanel = pp, BorderLayout.CENTER);
			centerPanel.revalidate();
			jaimsFrame.getContentPane().revalidate();
			jaimsFrame.getContentPane().repaint();
		}

	}

	public void repaintParentPanel() {

		try {
			parentPanel.repaint();
		} catch (NullPointerException npe) {
			LOG.error("Parent panel doesn't exist!", npe);
		}

	}

	public void showSettings() {

		jaimsFrame.getContentPane().remove(centerPanel);
		if (panelSelectSettings == null)
			panelSelectSettings = new PanelSelectSettings(this);

		jaimsFrame.getContentPane().add(panelSelectSettings, BorderLayout.CENTER);
		jaimsFrame.getContentPane().revalidate();
		jaimsFrame.repaint();
	}

	public void removeSettings() {

		jaimsFrame.getContentPane().remove(panelSelectSettings);
		jaimsFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		jaimsFrame.getContentPane().revalidate();
		jaimsFrame.getContentPane().repaint();
	}

	public void loginSuccessful() {

		jaimsFrame.getContentPane().remove(panelProgramStartup);
		jaimsFrame.getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		showGuiBuildingProcess = new ShowGuiBuildingProcess();
		jaimsFrame.getContentPane().add(showGuiBuildingProcess);
		jaimsFrame.initGUI();
		jaimsFrame.repaint();

		showGuiBuildingProcess.initGUI();
		showGuiBuildingProcess.revalidate();
		jaimsFrame.repaint();

		showGuiBuildingProcess.setProgress(10);

		panelTabbedPane = new PanelTabbedPane(this);

		ArrayList<SimpleContact> list = cm.getSimpleContacts();
		for (SimpleContact sc : list)
			allContacts.put(sc.getContactID(), sc);
		panelTabbedPane.buildPanelContacts(list);
		showGuiBuildingProcess.setProgress(15);

		manageMessagePanels = new ManageMessagePanels(this);
		manageMessagePanels.createMessageLists();
		showGuiBuildingProcess.setProgress(80);

		panelTabbedPane.buildPanelChatContacts(cm.getSimpleChatContacts());
		showGuiBuildingProcess.setProgress(98);

		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listAudioDevices = new ListAudioDevices(this);

		buildChatStart();

	}

	private void buildChatStart() {

		JPanel panelLeftSide = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.BLACK);
				g2.fillOval(getWidth() - 3, 5, 3, getHeight() - 10);
			}
		};
		panelLeftSide.setLayout(new BoxLayout(panelLeftSide, BoxLayout.PAGE_AXIS));
		panelLeftSide.setBorder(new EmptyBorder(0, 8, 8, 16));
		{
			panelUserShowing = new PanelUserShowing(this, cm.getUserContact());
			panelLeftSide.add(panelUserShowing);

			eventManager = new EventManager(cm.getAllPendingEvents().size());
			eventManager.setBackground(new Color(0, 0, 0, 0));
			eventManager.setOpaque(false);
			eventManager.setMinimumSize(new Dimension(250, 50));
			eventManager.setPreferredSize(eventManager.getMinimumSize());
			eventManager.setMaximumSize(new Dimension(250, 50));
			panelLeftSide.add(Box.createVerticalGlue());
			panelLeftSide.add(Box.createRigidArea(new Dimension(0, 6)));
			panelLeftSide.add(eventManager);
			panelLeftSide.add(Box.createRigidArea(new Dimension(0, 6)));
			panelLeftSide.add(Box.createVerticalGlue());

			panelLeftSide.add(panelTabbedPane);
		}

		centerPanel = new CenterPanel();
		centerPanel.add(panelLeftSide, BorderLayout.LINE_START);
		jaimsFrame.getContentPane().removeAll();
		jaimsFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		jaimsFrame.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		FrameTop ft = new FrameTop(this);
		jaimsFrame.getContentPane().add(ft, BorderLayout.PAGE_START);

		jaimsFrame.initChatGUI();
		jaimsFrame.getContentPane().repaint();
	}

	public void showPanelAddUser() {

		panelAddUser = new PanelAddUser(this);

		jaimsFrame.getContentPane().remove(centerPanel);
		jaimsFrame.getContentPane().add(panelAddUser, BorderLayout.CENTER);
		jaimsFrame.revalidate();
		jaimsFrame.repaint();
	}

	public void removePanelAddUser() {

		jaimsFrame.remove(panelAddUser);

		setAddingNewUser(false);

		jaimsFrame.add(centerPanel, BorderLayout.CENTER);
		jaimsFrame.revalidate();
		jaimsFrame.repaint();

		panelAddUser = null;
	}

	public void addChatUser(SimpleContact simpleContact) {

		panelTabbedPane.addChatUser(simpleContact);
		panelTabbedPane.revalidate();
		panelTabbedPane.repaint();
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	public void setUserOnTop(UUID uuid) {

		panelTabbedPane.setUserOnTop(uuid);
		centerPanel.repaint();
	}

	public void updateSettings() {

		cm.saveSettings();
	}

	public void setWrongUsername(boolean wrong) {

		panelProgramStartup.wrongUsername(wrong);
	}

	public void setWrongPassword(boolean wrong) {

		panelProgramStartup.wrongPassword(wrong);
	}

	public void sendLogin(String username, String password) {

		cm.sendLogin(username, password);
	}

	public String[] getLogin() {

		return cm.getLogin();
	}

	public void setRememberMe(boolean remember, String username, String password) {

		cm.setRememberMe(remember, username, password);
	}

	public SimpleContact getLoggedInUser() {

		return cm.getUserContact();
	}

	public SimpleContact getSimpleContact(UUID uuid) {

		return allContacts.get(uuid);
	}

	public ChatInformation getChatInformation(UUID uuid) {

		return manageMessagePanels.getContactChatInformation(uuid);
	}

	public ArrayList<SimpleContact> getSimpleChatContacts() {

		return cm.getSimpleChatContacts();
	}

	public ArrayList<SimpleContact> getSimpleContacts() {

		return cm.getSimpleContacts();
	}

	public ArrayList<ClientInternMessage> getMessageList(UUID uuid) {

		return cm.getMessageList(uuid);
	}

	public void updateChatPanels() {

	}

	public UUID getUserUUID() {

		return cm.getUserUUID();
	}

	public Settings getSettings() {

		return cm.getSettings();
	}

	public JaimsFrame getJaimsFrame() {

		return jaimsFrame;
	}

	public ManageMessagePanels getMessagePanelManager() {

		return manageMessagePanels;
	}

	public Image getProfileImage(UUID uuid) {

		return cm.getProfileImage(uuid);
	}

	public Image getUserProfileImage(UUID uuid) {

		return cm.getUserProfileImage(uuid);
	}

	public Image getChatBackground() {

		return cm.getChatBackground();
	}

	public String getContactStatus(UUID uuid) {

		return cm.getContactStatus(uuid);
	}

	public boolean isServerConnected() {

		return cm.isServerConnected();
	}

	public void setLoginEnabled(boolean enabled) {

		if (panelProgramStartup != null)
			panelProgramStartup.setLoginEnabled(enabled);
	}

	public void registerNewUser(String username, String password, String email) {

		cm.registerNewUser(username, password, email);
	}

	@SuppressWarnings("deprecation")
	public void succesfulRegistration() {

		String username = panelProgramStartup.getRegisteredUsername();
		String password = panelProgramStartup.getRegisteredPassword();
		panelProgramStartup.succesfulRegistration(username, password);
	}

	public String getRegisteredUsername() {

		return panelProgramStartup.getRegisteredUsername();
	}

	public ListAudioDevices getListAudioDevices() {

		return listAudioDevices;
	}

	public ArrayList<DFEObject> getDFEObjectsForUser(UUID user) {

		return cm.getDFEObjectsForUser(user);
	}

	public void saveTextMessage(TextMessage m) {

		cm.saveTextMessage(m);
	}

	public void saveVoiceMessage(jaims_development_studio.jaims.api.message.VoiceMessage v, String pathToFile) {

		cm.saveVoiceMessage(v, pathToFile);
	}

	public void addMessageToChat(jaims_development_studio.jaims.api.message.Message m, EMessageType messageType) {

		if (manageMessagePanels != null) {
			manageMessagePanels.addMessageToChat(m, messageType);
			if (parentPanel != null) {
				parentPanel.repaint();
			}

		}
	}

	public void updateHasChat(boolean hasChat, UUID contactUUID) {

		cm.updateHasChat(hasChat, contactUUID);
	}

	public void setAddingNewUser(boolean b) {

		cm.setAddingNewContact(b);
	}

	public void showAvailableUsersForAdding(Profile... users) {

		panelAddUser.showAvailableUsersForAdding(users);
	}

	public boolean hasEntry(UUID uuid) {

		return cm.hasEntry(uuid);
	}

	public boolean saveProfile(Profile p) {

		boolean b = cm.saveProfile(p);
		if (b) {
			allContacts.put(p.getUuid(), new SimpleContact(p.getUuid(), p.getNickname(), false));
			panelTabbedPane.addContact();
		}
		return b;
	}

	public boolean deleteProfile(UUID uuid) {

		boolean b = cm.deleteProfile(uuid);
		if (b)
			panelTabbedPane.removeContact(uuid);
		return b;
	}

	public void doLogout() {

		cm.doLogout();
	}

	public boolean closeGUI() {

		jaimsFrame.dispose();

		LOG.info("Disposed gui");

		return true;
	}

	public void sendSendable(Sendable s) {

		cm.sendSendable(s);
	}

	@Override
	public void run() {

		initFrame();

	}

	public SimpleContact getUserContact() {

		return cm.getUserContact();
	}

	public String getUsername() {

		return cm.getUsername();
	}

	public void repaintPanelShowMessages() {

		if (parentPanel != null)
			parentPanel.repaint();
	}

	public String getUserPath() {

		return cm.getUserPath();
	}

	public void askForNewPassword() {

	}

	public void addNewEvent() {

		System.out.println("Adding");
		eventManager.addNewEvent();
	}

	public ClientMain getClientMain() {
		return cm;
	}
}
