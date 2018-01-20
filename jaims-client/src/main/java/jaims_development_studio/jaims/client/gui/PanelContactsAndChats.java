package jaims_development_studio.jaims.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.client.chatObjects.ChatObject;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.database.ReadFromDatabase;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class PanelContactsAndChats extends JTabbedPane implements Runnable {

	/**
	 *
	 */
	private static final long		serialVersionUID	= 1L;
	private static final Logger		LOG					= LoggerFactory.getLogger(PanelContactsAndChats.class);

	PanelContacts					pc;
	PanelChatWithUsers				pcwu;
	PanelAccount					pa;
	JaimsFrame						frame;
	ClientMain						cm;
	String							html1				= "<html><body style = margin:0;background:#ffffff;padding:0px;width:86.5px;height:10px;border-radius:5px;text-align:center;border:none;><font size=15; color=#000000><b>";
	String							html2				= "</b></font></body></html>";
	JPanel							q;
	ArrayList<UUID>					panels				= new ArrayList<>();
	JScrollPane						scrollpane;
	private ArrayList<ContactPanel>	listCp;
	private List<ContactPanel>		list;

	public PanelContactsAndChats(JaimsFrame frame, ClientMain cm) {

		// TODO Auto-generated constructor stub
		this.frame = frame;
		this.cm = cm;
	};

	private void initGUI() {

		list = Collections.synchronizedList(new ArrayList<ContactPanel>());
		for (ChatObject co : ReadFromDatabase.chatObjectsList) {
			list.add(new ContactPanel(co, frame, cm));
		}
		list.sort(new Comparator<ContactPanel>() {

			@Override
			public int compare(ContactPanel o1, ContactPanel o2) {

				if (o1.getChatObject().getProfileContact().getNickname()
						.compareTo(o2.getChatObject().getProfileContact().getNickname()) > 0) {
					return 1;
				} else if (o1.getChatObject().getProfileContact().getNickname()
						.compareTo(o2.getChatObject().getProfileContact().getNickname()) < 0) {
					return -1;
				} else {
					return 0;
				}
			}

		});

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		{
			p.add(Box.createRigidArea(new Dimension(0, 3)));
			for (ContactPanel cp : list) {
				p.add(cp.getContactPanel());
				p.add(new LinePanel());
				p.add(Box.createRigidArea(new Dimension(0, 5)));
			}
		}

		JScrollPane scrollpane2 = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {

				scrollpane2.getViewport().repaint();
			}
		});

		synchronized (this) {
			listCp = new ArrayList();
		}

		for (ContactPanel cp : list) {

			if (cp.chatExists())
				listCp.add(cp);
		}
		listCp.sort(new Comparator<ContactPanel>() {

			@Override
			public int compare(ContactPanel o1, ContactPanel o2) {

				if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
						.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
								.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) > 0) {
					return -1;
				} else if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
						.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
								.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) < 0) {
					return 1;
				} else {
					return 0;
				}
			}

		});
		q = new JPanel();
		q.setLayout(new BoxLayout(q, BoxLayout.PAGE_AXIS));
		{
			q.add(Box.createRigidArea(new Dimension(0, 3)));
			for (ContactPanel cp : listCp) {
				q.add(cp.getChatPanel());
				q.add(new LinePanel());
				q.add(Box.createRigidArea(new Dimension(0, 5)));
				panels.add(cp.getChatObject().getProfileContact().getUuid());
			}
		}

		scrollpane = new JScrollPane(q, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(e -> scrollpane.getViewport().repaint());
		addTab(html1 + "Chats" + html2, scrollpane);
		addTab(html1 + "Contacts" + html2, scrollpane2);

		revalidate();
		repaint();

		setFont(new Font("Sans Serif", Font.BOLD, 15));

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

				setPreferredSize(new Dimension(250, frame.getHeight() - 120));
				revalidate();
				repaint();
			}
		});

		setPreferredSize(new Dimension(250, frame.getHeight() - 120));
		ReadFromDatabase.chatObjectsList = null;
	}

	private ArrayList<Message> getMessageList(ChatObject co) {

		return co.getList();
	}

	public void checkChatPanels(ContactPanel cp) {

		if (panels.contains(cp.getChatObject().getProfileContact().getUuid()) == false) {
			cp.createChatPanel();
			listCp.add(cp);
			listCp.sort(new Comparator<ContactPanel>() {

				@Override
				public int compare(ContactPanel o1, ContactPanel o2) {

					if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
							.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
									.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) > 0) {
						return -1;
					} else if (getMessageList(o1.getChatObject()).get(getMessageList(o1.getChatObject()).size() - 1)
							.getTimestampRecieved().compareTo(getMessageList(o2.getChatObject())
									.get(getMessageList(o2.getChatObject()).size() - 1).getTimestampRecieved()) < 0) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			q.removeAll();
			for (ContactPanel c : listCp) {
				q.add(c.getChatPanel());
				q.add(new LinePanel());
				q.add(Box.createRigidArea(new Dimension(0, 5)));
				panels.add(c.getChatObject().getProfileContact().getUuid());
			}
			scrollpane.repaint();
		}
	}

	public ClientMain getClientMain() {

		return cm;
	}

	@Override
	public void run() {

		initGUI();

	}

	public void addMessageToChat(SendableMessage sm) {

		Thread thread = new Thread() {

			@Override
			public void run() {

				for (ContactPanel cp : list) {
					if (cp.getChatObject().getProfileContact().getUuid().equals(sm.getSender())) {
						if (cp.chatExists())
							cp.getPanelChatMessages().addMessageFromContact(sm);
						else {
							cp.createChatPanel();
							cp.getPanelChatMessages().addMessageFromContact(sm);
						}
						break;
					}
				}
			}
		};
		thread.start();

	}

	public void addNewContact() {

	}

}