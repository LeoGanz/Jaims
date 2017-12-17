package jaims_development_studio.jaims.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.chatObjects.ChatObjects;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.database.DatabaseConnection;
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
	String html1 = "<html><body style = margin:0;background:#ffffff;padding:0px;width:86.5px;height:10px;border-radius:5px;text-align:center;border:none;><font size=15; color=#000000><b>";
	String html2 = "</b></font></body></html>";
	
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
		addTab(html1 + "Chats" + html2, scrollpane);
		addTab(html1 + "Contacts" + html2, scrollpane2);

		revalidate();
		repaint();
		
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