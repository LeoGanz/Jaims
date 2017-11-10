package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.database.ReadFromDatabase;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelContactsAndChats extends JTabbedPane implements Runnable {

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

		pc = new PanelContacts(list);
		pcwu = new PanelChatWithUsers(list);
		


		Thread thread = new Thread(pc);
		thread.start();

		Thread thread2 = new Thread(pcwu);
		thread2.start();

		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException ise) {
			LOG.error("Failed to join Thread");
		}


		JScrollPane scrollpane2 = new JScrollPane(pc, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollpane2.getViewport().repaint();				
			}
		});

		setFont(new Font("Calibri", Font.BOLD, 15));

		JScrollPane scrollpane = new JScrollPane(pcwu, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(e -> scrollpane.getViewport().repaint());
		addTab("Chats", scrollpane);
		addTab("Contacts", scrollpane2);

		revalidate();
		repaint();

		setPreferredSize(new Dimension(250, frame.getHeight()-120));
		
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				revalidate();
				repaint();				
			}
			
		});
		frame.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				setPreferredSize(new Dimension(250, frame.getHeight()-120));
				revalidate();
				repaint();
			}
		});

		// setPreferredSize(new Dimension(250, 400));
		setPreferredSize(new Dimension(250, frame.getHeight() - 120));

	}

	public ClientMain getClientMain() {
		return cm;
	}
	
	@Override
	public void run() {
		initGUI();

	}

}