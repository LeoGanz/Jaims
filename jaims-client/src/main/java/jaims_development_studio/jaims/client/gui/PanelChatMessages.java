package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Profile;

public class PanelChatMessages extends JScrollPane{
	
	public PanelChatMessages(JaimsFrame jf, Profile userProfile, ChatObjects co) {
		initGUI(jf,userProfile,co);
	
	}
	
	private void initGUI(JaimsFrame jf,Profile userProfile,ChatObjects co) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		
		for (int i = 0; i < co.getList().size(); i++) {
			if (co.getList().get(i).getSender() == userProfile.getUuid()) {
				//Case Sender = User
				
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.add(Box.createRigidArea(new Dimension(50, 0)), BorderLayout.LINE_START);
				
				if (co.getList().get(i).getMessageObject() instanceof String) {
					JLabel lbl = new JLabel((String) co.getList().get(i).getMessageObject());
					
					p.add(lbl, BorderLayout.CENTER);
					p.add(Box.createRigidArea(new Dimension(8, 0)));
					
				}else if (co.getList().get(i).getMessageObject() instanceof Image) {
					
				}else {
					
				}
				
				panel.add(p);
			}else {
				//Case Sender = Contact
				
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.add(Box.createRigidArea(new Dimension(8, 0)), BorderLayout.LINE_START);
				
				if (co.getList().get(i).getMessageObject() instanceof String) {
					JLabel lbl = new JLabel((String) co.getList().get(i).getMessageObject());
					
					p.add(lbl, BorderLayout.CENTER);
					p.add(Box.createRigidArea(new Dimension(50, 0)));
					
				}else if (co.getList().get(i).getMessageObject() instanceof Image) {
					
				}else {
					
				}
				
				panel.add(p);
			}
		}
		
		add(panel);
	}
	
	

}
