package jaims_development_studio.jaims.client.gui;import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;
import jaims_development_studio.jaims.client.logic.Profile;

public class PanelProgram extends JPanel{
	
	MainFrame mf;
	Thread threadBuildChatWithUsers, threadDatabaseManagement;
	PanelProfile panelProfile;
	Profile userProfile;
	PanelChatWithUsers panelChatWithUsers;
	DatabaseManagement databaseManagement;
	
	PanelChatWindow centerpanel = null;
	
	public PanelProgram (MainFrame mf, Profile userProfile) {
		this.mf = mf;
		this.userProfile = userProfile;
		setLayout(new BorderLayout());
		databaseManagement = new DatabaseManagement(mf, userProfile);
		threadDatabaseManagement = new Thread(databaseManagement);
		threadDatabaseManagement.start();
		buildProgramPanel(mf, userProfile);
	}
	
	private void buildProgramPanel(MainFrame mf, Profile userProfile) {		
		
		JPanel leftNorth = new JPanel();
		leftNorth.setLayout(new BorderLayout(0,10));
		
		threadBuildChatWithUsers = new Thread(panelChatWithUsers  = new PanelChatWithUsers(mf, databaseManagement, this, threadDatabaseManagement, userProfile));
		threadBuildChatWithUsers.start();
		
		
		panelProfile = new PanelProfile(userProfile, mf, this);
		
		
		while(threadBuildChatWithUsers.isAlive()) {
			
		}
	        
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Chats", new JScrollPane(panelChatWithUsers));
		tabbedPane.addTab("Contacts", new PanelContacts(databaseManagement, mf));
		tabbedPane.setFont(new Font("Calibri", Font.ITALIC, 14));
		tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		leftNorth.add(tabbedPane, BorderLayout.CENTER);
		leftNorth.add(panelProfile, BorderLayout.NORTH);
		
		add(leftNorth, BorderLayout.LINE_START);
		
	}
	
	public void setChatWindow(PanelChatWindow chatWindow) {
		if (centerpanel != null) {
			remove(centerpanel);
		}
		
		centerpanel = chatWindow;
		add(centerpanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	public MainFrame getMF() {
		return mf;
	}

	public PanelChatWithUsers getPanelChatWithUsers() {
		return panelChatWithUsers;
	}
	
	public void doResizing() {
		panelProfile.doResizing();
		
		
		panelChatWithUsers.doResizing();
		revalidate();

	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(null, 0, 0, null);
		doResizing();
		
	}
}
