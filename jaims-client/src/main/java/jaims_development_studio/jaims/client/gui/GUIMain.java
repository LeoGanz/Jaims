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

import jaims_development_studio.jaims.client.chatObjects.ChatInformation;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.gui.customGUIComponents.CenterPanel;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.login.PanelProgramStartup;
import jaims_development_studio.jaims.client.gui.login.ShowGuiBuildingProcess;
import jaims_development_studio.jaims.client.gui.messagePanels.ManageMessagePanels;
import jaims_development_studio.jaims.client.gui.showContacts.PanelTabbedPane;
import jaims_development_studio.jaims.client.gui.showContacts.PanelUserShowing;
import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;
import jaims_development_studio.jaims.client.settings.Settings;

public class GUIMain implements Runnable {

	private static final Logger				LOG	= LoggerFactory.getLogger(GUIMain.class);

	private JaimsFrame						jaimsFrame;
	private ClientMain						cm;
	private PanelProgramStartup				panelProgramStartup;
	private ShowGuiBuildingProcess			showGuiBuildingProcess;
	private PanelTabbedPane					panelTabbedPane;
	private ManageMessagePanels				manageMessagePanels;
	private CenterPanel						centerPanel;
	private PanelUserShowing				panelUserShowing;
	private JPanel							infoPanel;
	private HashMap<UUID, SimpleContact>	allContacts;
	private ParentPanel						parentPanel;
	private PanelAddUser					panelAddUser;
	private boolean							showSplashScreen;

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

		panelProgramStartup.initGUI();
		jaimsFrame.repaint();
	}

	public void showLoginPanel() {

	}

	public void showStandardPanel() {

	}

	public void showParentPanel(ParentPanel pp) {

		if (parentPanel != null)
			jaimsFrame.getContentPane().remove(parentPanel);

		jaimsFrame.getContentPane().add(parentPanel = pp, BorderLayout.CENTER);
		jaimsFrame.getContentPane().repaint();
	}

	public void showSettings() {

	}

	public void loginSuccessful() {

		jaimsFrame.getContentPane().remove(panelProgramStartup);
		jaimsFrame.getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		showGuiBuildingProcess = new ShowGuiBuildingProcess(this);
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
		showGuiBuildingProcess.setProgress(80);

		panelTabbedPane.buildPanelChatContacts(cm.getSimpleContacts());
		showGuiBuildingProcess.setProgress(98);

		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		buildChatStart();
	}

	private void buildChatStart() {

		JPanel panelLeftSide = new JPanel() {
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
		panelLeftSide.setBorder(new EmptyBorder(7, 0, 0, 8));
		{
			panelUserShowing = new PanelUserShowing(this, cm.getUserContact());
			panelLeftSide.add(panelUserShowing);

			infoPanel = new JPanel();
			infoPanel.setBackground(new Color(0, 0, 0, 0));
			infoPanel.setOpaque(false);
			infoPanel.setMinimumSize(new Dimension(250, 50));
			infoPanel.setPreferredSize(infoPanel.getMinimumSize());
			infoPanel.setMaximumSize(new Dimension(350, 50));
			panelLeftSide.add(Box.createRigidArea(new Dimension(0, 6)));
			panelLeftSide.add(infoPanel);
			panelLeftSide.add(Box.createRigidArea(new Dimension(0, 6)));

			panelLeftSide.add(panelTabbedPane);
		}

		centerPanel = new CenterPanel();
		centerPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
		centerPanel.add(panelLeftSide, BorderLayout.LINE_START);
		jaimsFrame.getContentPane().removeAll();
		jaimsFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		jaimsFrame.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		jaimsFrame.initChatGUI();
		jaimsFrame.getContentPane().repaint();
	}

	public void showPanelAddUser() {

		if (panelAddUser == null)
			panelAddUser = new PanelAddUser(this);

		jaimsFrame.getContentPane().remove(centerPanel);
		jaimsFrame.getContentPane().add(panelAddUser, BorderLayout.CENTER);
		jaimsFrame.revalidate();
		jaimsFrame.repaint();
	}

	public void removePanelAddUser() {

		jaimsFrame.remove(panelAddUser);
		jaimsFrame.add(centerPanel, BorderLayout.CENTER);
		jaimsFrame.revalidate();
		jaimsFrame.repaint();
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

	public ArrayList<Message> getMessageList(UUID uuid) {

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

	public int getPanelWidth() {

		return 0;
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

	public void succesfulRegistration() {

		String username = panelProgramStartup.getRegisteredUsername();
		String password = panelProgramStartup.getRegisteredPassword();
		panelProgramStartup.succesfulRegistration(username, password);
	}

	public String getRegisteredUsername() {

		return panelProgramStartup.getRegisteredUsername();
	}

	public void doLogout() {

		cm.doLogout();
	}

	public boolean closeGUI() {

		jaimsFrame.dispose();

		LOG.info("Disposed gui");

		return true;
	}

	@Override
	public void run() {

		initFrame();

	}

}
