package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.xml.soap.SAAJResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PanelContactsAndChats extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PanelContactsAndChats.class);
	
	PanelContacts pc;
	PanelChatWithUsers pcwu;
	
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
		
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.setFont(new Font("Calibri", Font.BOLD, 15));
		//jtp.setBorder(new LineBorder(Color.GRAY));
		jtp.addTab("Chats", pcwu);
		
		JScrollPane jsp = new JScrollPane(pc);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		jsp.setPreferredSize(new Dimension((int) pc.getPreferredSize().getWidth(), (int) pc.getPreferredSize().getHeight()));
		jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				pc.repaint();
				jsp.repaint();				
			}
		});
		jtp.addTab("Contacts", jsp);
		jtp.setPreferredSize(new Dimension(400, 400));
		
		add(jtp);
		
	}

	@Override
	public void run() {
		initGUI();
		
	}

}
