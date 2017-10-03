package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelContactsAndChats extends JTabbedPane implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelContactsAndChats.class);
	
	PanelContacts pc;
	PanelChatWithUsers pcwu;
	PanelAccount pa;
	JFrame frame;
	
	public PanelContactsAndChats(JFrame frame) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
	};
	
	private void initGUI() {		
		Thread thread = new Thread(pc = new PanelContacts(this));
		thread.start();
		
		Thread thread2 = new Thread(pcwu = new PanelChatWithUsers());
		thread2.start();
		
		try {
			thread.join();
			thread2.join();
		} catch (InterruptedException e) {
			LOG.error("Failed to join Thread");
		}
		
		setFont(new Font("Calibri", Font.BOLD, 15));
		
		JScrollPane scrollpane = new JScrollPane(pcwu);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollpane.getViewport().repaint();
			}
		});		
		addTab("Chats", scrollpane);
		
		JScrollPane scrollpane2 = new JScrollPane(pc);
		scrollpane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				scrollpane2.getViewport().repaint();				
			}
		});
		addTab("Contacts", scrollpane2);
		
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
