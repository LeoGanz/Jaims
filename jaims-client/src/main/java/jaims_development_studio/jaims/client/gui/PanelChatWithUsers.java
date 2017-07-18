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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.plaf.synth.SynthSpinnerUI;

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
	ArrayList<ChatPanel> list = new ArrayList<>();

	public PanelChatWithUsers(MainFrame mf, DatabaseManagement databaseManagement, PanelProgram panelProgram, Thread threadDatabaseManagement, Profile userProfile) {
		this.mf = mf;
		this.databaseManagement = databaseManagement;
		this.panelProgram = panelProgram;
		this.threadDatabaseManagement = threadDatabaseManagement;
		this.userProfile = userProfile;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new LineBorder(Color.DARK_GRAY));
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
		list.clear();
		for (int i = 0; i < databaseManagement.getProfileList().size(); i++) {
			
			ChatPanel chatPanel = new ChatPanel(mf);
			chatPanel.setMaximumSize(new Dimension(mf.getFrame().getWidth()/3, 60));
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
			chatPanel.setChatWindow(chatWindow);
			chatPanel.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
					//chatWindow.resized();
					lastClicked = chatPanel.getChatWindow();
					panelProgram.setChatWindow(chatPanel.getChatWindow());

					
				}
			});
			
			list.add(chatPanel);
			
		}
		
		Comparator<ChatPanel> comp = new Comparator<ChatPanel>() {

			@Override
			public int compare(ChatPanel p1, ChatPanel p2) {
				if (p1.getChatWindow().getLastMessage().before(p2.getChatWindow().getLastMessage())) {
					return -1;
				}else if(p1.getChatWindow().getLastMessage().after(p2.getChatWindow().getLastMessage())) {
					return 1;
				}else {
					return 0;
				}
			}
		};
		Collections.sort(list, comp);
		list.sort(comp);
		
		for (ChatPanel panel : list) {
			add(panel);
			
		}
	}
	
	public void resized() {
		removeAll();
		list.clear();
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
