package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import jaims_development_studio.jaims.client.logic.DatabaseManagement;
import jaims_development_studio.jaims.client.logic.Profile;

public class PanelChatWithUsers extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -753575424119617461L;
	
	MainFrame mf;
	DatabaseManagement databaseManagement;
	Thread threadDatabaseManagement;
	PanelProgram panelProgram;
	Profile userProfile;
	PanelChatWindow chatWindow;
	
	PanelChatWindow lastClicked;

	public PanelChatWithUsers(MainFrame mf, DatabaseManagement databaseManagement, PanelProgram panelProgram, Thread threadDatabaseManagement, Profile userProfile) {
		this.mf = mf;
		this.databaseManagement = databaseManagement;
		this.panelProgram = panelProgram;
		this.threadDatabaseManagement = threadDatabaseManagement;
		this.userProfile = userProfile;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new LineBorder(Color.black));
	}
	
	@Override
	public void run() {
		do{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while (threadDatabaseManagement.isAlive());
		
		initGUI();
		
	}
	
	public void initGUI() {
		for (int i = 0; i < databaseManagement.getProfileList().size(); i++) {
			JPanel chatPanel = new JPanel();
			chatPanel.setMinimumSize(new Dimension(200, 60));
			chatPanel.setMaximumSize(new Dimension(400, 60));
			chatPanel.setPreferredSize(new Dimension(mf.getFrame().getWidth()/3, 60));
			chatPanel.setBorder(new LineBorder(Color.BLACK));
			chatPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.LINE_AXIS));
			chatPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			
			Image profileImage = null;
			try {
				profileImage = ImageIO.read(new ByteArrayInputStream(databaseManagement.getProfileList().get(i).getByteImage()));
				profileImage = profileImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon ii = new ImageIcon(profileImage);
			JLabel lbl = new JLabel();
			lbl.setIcon(ii);
			
			chatPanel.add(lbl);
			chatPanel.add(Box.createRigidArea(new Dimension(15, 0)));
			
			JLabel username = new JLabel(databaseManagement.getProfileList().get(i).getNickname(), JLabel.LEFT);
			username.setFont(new Font("Calibri", Font.BOLD, 15));
			chatPanel.add(username);
			
			chatWindow = new PanelChatWindow(panelProgram, databaseManagement, databaseManagement.getProfileList().get(i), userProfile);
			chatPanel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					//chatWindow.resized();
					lastClicked = chatWindow;
					panelProgram.setChatWindow(chatWindow);
					
				}
			});
			
			add(chatPanel);
		}
	}
	
	public void resized() {
		removeAll();
		
		initGUI();
		revalidate();
		
		if (lastClicked != null) {
			lastClicked.resized();
		}
	}
	
	public PanelChatWindow getPanelChatWindow() {
		return chatWindow;
	}
	
	public void doResizing() {
		removeAll();
		
		initGUI();
		revalidate();
		repaint();
	}

	
}
