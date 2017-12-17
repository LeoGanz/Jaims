package jaims_development_studio.jaims.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Profile;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class ContactPanel extends JPanel{
	
	private static final Logger LOG = LoggerFactory.getLogger(ContactPanel.class);
	ChatObjects co;
	PanelChatMessages pcm;
	PanelChat pc;
	JaimsFrame jf;
	ClientMain cm;
	
	public ContactPanel(ChatObjects co, JaimsFrame jf, ClientMain cm) {
		this.co = co;
		this.jf = jf;
		this.cm = cm;
		initGUI(co.getProfileContact(), jf, cm);
	}
	
	private void initGUI(Profile pf, JaimsFrame jf, ClientMain cm) {
		
		ContactPanel cp = this;
		
		
		pcm = new PanelChatMessages(jf, pf, co, this);
		Thread thread = new Thread(pcm);
		thread.start();
		pc = new PanelChat(pf, pcm);
		Thread thread2 = new Thread(pc);
		thread2.start();
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				cm.setAcvtiveContactPanel(cp);
				cm.setMessagePanel(pc);
			}
		});
	}
	
	public void setPanel(ContactPanel cp) {
		
		cm.setAcvtiveContactPanel(cp);
		cm.setMessagePanel(pc);
		
		System.out.println(pc.getSize().getWidth());
	}
	
	public ChatObjects getChatObject() {
		return co;
	}
	
	public ClientMain getClientMain() {
		return cm;
	}
}
