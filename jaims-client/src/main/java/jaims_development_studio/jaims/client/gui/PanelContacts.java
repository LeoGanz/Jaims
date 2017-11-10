package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelContacts extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelContacts.class);
	
	PanelContactsAndChats pcc;
	
	List<ContactPanel> panels;

	public PanelContacts(List<ContactPanel> list) {
		panels = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ContactPanel cp : list) {
			panels.add(cp);
		}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public void initGUI() {
		
		
		Comparator<ContactPanel> comp = new Comparator<ContactPanel>() {

			@Override
			public int compare(ContactPanel o1, ContactPanel o2) {
				if (o1.getChatObject().getProfileContact().getNickname().compareTo(o2.getChatObject().getProfileContact().getNickname()) > 0) {
					return 1;
				}else if (o1.getChatObject().getProfileContact().getNickname().compareTo(o2.getChatObject().getProfileContact().getNickname()) < 0 ) {
					return -1;
				}else {
					return 0;
				}
			}
			
		};
		panels.sort(comp);
		for (int i = 0; i < panels.size(); i++) {
			add(panels.get(i));
			add(Box.createRigidArea(new Dimension(0, 5)));
		}
	}
	

	
	@Override
	public void run() {
		initGUI();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
	}

}
