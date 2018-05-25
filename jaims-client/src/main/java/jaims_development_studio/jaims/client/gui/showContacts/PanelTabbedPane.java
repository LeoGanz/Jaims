package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelTabbedPane extends JPanel {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;
	private GUIMain						guiMain;
	private boolean						chatsSelected		= true, paintChats = true, paintContacts = false;
	private JScrollPane					jspChats, jspContacts;
	private JPanel						panelChats, panelContacts, panelTabs;
	private int							xChats				= 10, xContacts = -130, yContacts = 30;
	private int							paintChatWidth		= 130, paintChatHeight = 35, paintContactWidth = 0,
			paintContactHeight = 0;
	private boolean						animationRunning	= false;
	private JButton						btChats, btContacts;
	private ArrayList<SimpleContact>	sortedList;

	public PanelTabbedPane(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());

		panelTabs = new JPanel();
		panelTabs.setBackground(Color.GRAY);
		panelTabs.setPreferredSize(new Dimension(250, 35));
		panelTabs.setMaximumSize(new Dimension(350, 35));
		panelTabs.setLayout(new BoxLayout(panelTabs, BoxLayout.LINE_AXIS));
		{
			btChats = new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
					if (paintChats) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(0, 0, paintChatWidth, paintChatHeight);
					}

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Sans Serif", Font.BOLD, 14));
					int xChat = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Chats") / 2;
					int yChats = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;

					g2.setColor(Color.WHITE);
					g2.drawString("Chats", xChat, yChats);

					g2.setColor(new Color(191, 30, 30));
					g2.setStroke(new BasicStroke(3));
					g2.fillOval(xChats, getHeight() - 5, getWidth() - 20, 4);

				}
			};
			btChats.setPreferredSize(new Dimension(125, 35));
			btChats.setMaximumSize(new Dimension(175, 35));
			btChats.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btChats.setBorderPainted(false);
			btChats.addActionListener(e -> handleAnimationToChats());
			panelTabs.add(btChats);

			btContacts = new JButton() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
					if (paintContacts) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(0, 0, paintContactWidth, paintContactHeight);
					}

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Sans Serif", Font.BOLD, 14));
					int xChat = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Contacts") / 2;
					int yChats = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;

					g2.setColor(Color.WHITE);
					g2.drawString("Contacts", xChat, yChats);

					g2.setColor(new Color(191, 30, 30));
					g2.setStroke(new BasicStroke(3));
					g2.fillOval(xContacts, yContacts, 105, 4);

				}
			};
			btContacts.setPreferredSize(new Dimension(125, 35));
			btContacts.setMaximumSize(new Dimension(175, 35));
			btContacts.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btContacts.setBorderPainted(false);
			btContacts.addActionListener(e -> handleAnimationToContacts());
			panelTabs.add(btContacts);
		}
		add(panelTabs, BorderLayout.PAGE_START);
	}

	public void buildPanelContacts(ArrayList<SimpleContact> list) {

		panelContacts = null;
		jspContacts = null;

		if (list.size() > 0) {
			list.sort((o1, o2) -> {

				if (o1.getContactNickname().compareTo(o2.getContactNickname()) > 0) {
					return 1;
				} else if (o1.getContactNickname().compareTo(o2.getContactNickname()) < 0) {
					return -1;
				} else {
					return 0;
				}
			});

			panelContacts = new JPanel() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			panelContacts.setLayout(new BoxLayout(panelContacts, BoxLayout.PAGE_AXIS));
			panelContacts.setOpaque(false);
			panelContacts.setBorder(new EmptyBorder(0, 3, 0, 3));
			JLabel lbl = new JLabel(list.get(0).getContactNickname().substring(0, 1).toUpperCase());
			lbl.setForeground(Color.white);
			lbl.setFont(new Font("Sans Serif", Font.BOLD, 32));
			lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelContacts.add(Box.createRigidArea(new Dimension(0, 5)));
			panelContacts.add(lbl);
			panelContacts.add(Box.createRigidArea(new Dimension(0, 3)));

			for (int i = 0; i < list.size(); i++) {

				if (i + 1 < list.size()) {
					PanelContactShowing panelContactShowing = new PanelContactShowing(guiMain, list.get(i));
					panelContacts.add(panelContactShowing);

					if (list.get(i).getContactNickname().substring(0, 1).toUpperCase()
							.equals(list.get(i + 1).getContactNickname().substring(0, 1).toUpperCase())) {
						panelContacts.add(Box.createRigidArea(new Dimension(0, 5)));
					} else {
						panelContacts.add(Box.createRigidArea(new Dimension(0, 15)));
						JLabel label = new JLabel(list.get(i + 1).getContactNickname().substring(0, 1).toUpperCase());
						label.setForeground(Color.WHITE);
						label.setBackground(new Color(0, 0, 0, 0));
						label.setOpaque(false);
						label.setFont(new Font("Sans Serif", Font.BOLD, 32));
						label.setAlignmentX(Component.CENTER_ALIGNMENT);
						panelContacts.add(label);
						panelContacts.add(Box.createRigidArea(new Dimension(0, 3)));
					}
				} else {
					PanelContactShowing panelContactShowing = new PanelContactShowing(guiMain, list.get(i));
					panelContacts.add(panelContactShowing);
				}
			}
		} else {
			panelContacts = new JPanel() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
				}

			};
			panelContacts.setLayout(new BoxLayout(panelContacts, BoxLayout.PAGE_AXIS));
			panelContacts.setOpaque(false);

		}

		jspContacts = new JScrollPane(panelContacts, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspContacts.setBackground(Color.LIGHT_GRAY);
		jspContacts.setOpaque(false);
	}

	public void buildPanelChatContacts(ArrayList<SimpleContact> list) {

		sortedList = guiMain.getMessagePanelManager().sortChatPanels(list);
		panelChats = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelChats.setLayout(new BoxLayout(panelChats, BoxLayout.PAGE_AXIS));
		panelChats.setOpaque(false);
		panelChats.add(Box.createRigidArea(new Dimension(0, 5)));

		for (SimpleContact contact : sortedList) {
			if (contact.chatExists()) {
				PanelContactShowing panelContactShowing = new PanelContactShowing(guiMain, contact);
				panelChats.add(panelContactShowing);
				panelChats.add(Box.createRigidArea(new Dimension(0, 5)));
			}
		}

		jspChats = new JScrollPane(panelChats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspChats.setBackground(Color.LIGHT_GRAY);
		jspChats.setOpaque(false);
		add(jspChats, BorderLayout.CENTER);
	}

	public void addChatUser(SimpleContact simpleContact) {

		boolean containsChat = false;

		for (SimpleContact sc : sortedList) {
			if (simpleContact.getContactID().equals(sc.getContactID())) {
				containsChat = true;
				break;
			}
		}

		if (containsChat == false) {
			PanelContactShowing pcs = new PanelContactShowing(guiMain, simpleContact);
			panelChats.add(Box.createRigidArea(new Dimension(0, 5)));
			panelChats.add(pcs, 1);
			panelChats.add(Box.createRigidArea(new Dimension(0, 5)), 2);
			panelChats.revalidate();
			panelChats.repaint();
			jspChats.revalidate();
			jspChats.repaint();
			revalidate();
			repaint();
			sortedList.add(simpleContact);
		}
	}

	public void setUserOnTop(UUID uuid) {

		if (panelChats.getComponent(1) instanceof PanelContactShowing
				&& ((PanelContactShowing) panelChats.getComponent(1)).getPanelID().equals(uuid))
			return;

		for (Component c : panelChats.getComponents()) {
			if (c instanceof PanelContactShowing) {
				if (((PanelContactShowing) c).getPanelID().equals(uuid)) {
					panelChats.remove(c);
					panelChats.add(c, 1);
					panelChats.add(Box.createRigidArea(new Dimension(0, 5)), 2);
				}
			}
		}

		panelChats.revalidate();
		repaint();
	}

	public void addContact() {

		removeAll();

		buildPanelContacts(guiMain.getSimpleContacts());
		add(panelTabs, BorderLayout.PAGE_START);
		if (chatsSelected)
			add(jspChats, BorderLayout.CENTER);
		else
			add(jspContacts, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	public void removeContact(UUID panelID) {

		removeAll();

		buildPanelContacts(guiMain.getSimpleChatContacts());

		for (Component c : panelChats.getComponents()) {
			if (c instanceof PanelContactShowing) {
				if (((PanelContactShowing) c).getPanelID().equals(panelID)) {
					panelChats.remove(c);
					panelChats.revalidate();
					panelChats.repaint();

					break;
				}
			}
		}

		add(panelTabs, BorderLayout.PAGE_START);
		if (chatsSelected)
			add(jspChats, BorderLayout.CENTER);
		else
			add(jspContacts, BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	private void handleAnimationToContacts() {

		if (animationRunning == false && chatsSelected) {
			remove(jspChats);
			add(jspContacts, BorderLayout.CENTER);
			revalidate();
			repaint();

			animationRunning = true;
			paintContacts = true;

			Timer timer = new Timer(10, e -> {

				if (xChats < 130) {
					xChats += 10;
					btChats.repaint();
				}

				if (xContacts < 10) {
					xContacts += 10;
					yContacts = btContacts.getHeight() - 5;
					btContacts.repaint();
				}

				if (xContacts >= 10) {
					((Timer) e.getSource()).stop();
					animationRunning = false;
				}

			});
			timer.start();

			Timer t = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (paintContactWidth < 130) {
						paintContactWidth += 10;
					}

					if (paintContactHeight < 35) {
						paintContactHeight += 5;
					}

					if (paintChatWidth > 0)
						paintChatWidth -= 10;

					if (paintChatHeight > 0)
						paintChatHeight -= 5;

					if (paintContactWidth >= 130) {
						((Timer) e.getSource()).stop();
						paintChats = false;
					}

				}
			});
			t.start();
			chatsSelected = false;
		}
	}

	private void handleAnimationToChats() {

		if (animationRunning == false && chatsSelected == false) {
			remove(jspContacts);
			add(jspChats, BorderLayout.CENTER);
			jspChats.revalidate();
			revalidate();
			repaint();
			animationRunning = true;
			paintChats = true;

			Timer timer = new Timer(10, e -> {

				if (xChats > 10) {
					xChats -= 10;
					btChats.repaint();
				}

				if (xContacts > -130) {
					xContacts -= 10;
					yContacts = btContacts.getHeight() - 5;
					btContacts.repaint();
				}

				if (xChats <= 10) {
					((Timer) e.getSource()).stop();
					animationRunning = false;
				}

			});
			timer.start();

			Timer t = new Timer(10, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (paintChatWidth < 130)
						paintChatWidth += 10;

					if (paintChatHeight < 35)
						paintChatHeight += 5;

					if (paintContactWidth > 0)
						paintContactWidth -= 10;

					if (paintContactHeight > 0)
						paintContactHeight -= 5;

					if (paintChatWidth >= 130) {
						((Timer) e.getSource()).stop();
						paintContacts = false;
					}
				}
			});
			t.start();
			chatsSelected = true;
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
