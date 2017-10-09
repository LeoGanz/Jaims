package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.database.ReadFromDatabase;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelContactsAndChats extends JTabbedPane implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelContactsAndChats.class);
	
	PanelContacts pc;
	PanelChatWithUsers pcwu;
	PanelAccount pa;
	JaimsFrame frame;
	ClientMain cm;
	
	public PanelContactsAndChats(JaimsFrame frame, ClientMain cm) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		this.cm = cm;
	};
	
	private void initGUI() {
		List<ContactPanel> list = Collections.synchronizedList(new ArrayList<ContactPanel>());
		
		for (int i = 0; i < ReadFromDatabase.chatObjectsList.size(); i++) {
			ContactPanel cp = new ContactPanel(ReadFromDatabase.chatObjectsList.get(i), frame, cm);
			list.add(cp);
		}
		
		
		pcwu = new PanelChatWithUsers(list);
		pcwu.initGUI();
		
		setFont(new Font("Calibri", Font.BOLD, 15));
		
		JScrollPane scrollpane = new JScrollPane(pcwu);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollpane.getViewport().repaint();
			}
		});		
		addTab("Chats", scrollpane);
		
		
		
		
		
		pc = new PanelContacts(list);
		pc.initGUI();		
		JScrollPane scrollpane2 = new JScrollPane(pc);
		scrollpane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollpane2.getViewport().repaint();				
			}
		});
		addTab("Contacts", pc);
		
		revalidate();
		repaint();
		//setPreferredSize(new Dimension(250, 400));
		setPreferredSize(new Dimension(250, frame.getHeight()-120));
	}

	@Override
	public void run() {
		initGUI();
		
	}

}
