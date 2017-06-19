package jaims_development_studio.jaims.client.gui;import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.Box;
import javax.swing.JPanel;

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
		
		leftNorth.add(panelChatWithUsers, BorderLayout.CENTER);
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
	}
	
	public MainFrame getMF() {
		return mf;
	}

	public PanelChatWithUsers getPanelChatWithUsers() {
		return panelChatWithUsers;
	}
	
	public void doResizing() {
		Thread threadPanelProfile = new Thread(panelProfile);
		threadPanelProfile.start();
		
		if(centerpanel != null) {
			Thread threadPanelWindow = new Thread(centerpanel);
			threadPanelWindow.start();
		}
		
		panelChatWithUsers.doResizing();
		
	}
}
