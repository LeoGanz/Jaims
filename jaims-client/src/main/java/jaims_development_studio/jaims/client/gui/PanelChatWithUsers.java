package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelChatWithUsers extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6014240532566432438L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelChatWithUsers.class);

	List<ContactPanel> panels;
	
	public PanelChatWithUsers(List<ContactPanel> panels) {
		this.panels = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ContactPanel cp : panels)
			this.panels.add(cp);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public void initGUI() {
		
		Comparator<ContactPanel> comp = (o1, o2) -> {
			if (o1.getChatObject().getList().get(o1.getChatObject().getList().size()-1).getTimestampRecieved().compareTo(o2.getChatObject().getList().get(o2.getChatObject().getList().size()-1).getTimestampRecieved()) > 0)
				return 1;
			else if (o1.getChatObject().getList().get(o1.getChatObject().getList().size()-1).getTimestampRecieved().compareTo(o2.getChatObject().getList().get(o2.getChatObject().getList().size()-1).getTimestampRecieved()) < 0 )
				return -1;
			else
				return 0;
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
	
	public List<ContactPanel> getList() {
		return panels;
	}

}
