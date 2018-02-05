package jaims_development_studio.jaims.client.gui.showContacts;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelTabbedPane extends JPanel {

	private GUIMain		guiMain;
	private boolean		chatsSelected		= true;
	private JScrollPane	jspChats, jspContacts;
	private JPanel		panelChats, panelContacts, panelCenter;
	private int			xChats				= 10, yChats = 30, xContacts = -130, yContacts = 30, xJspChats = 0,
			xJspContacts = 260;
	private boolean		animationRunning	= false;
	private JButton		btChats, btContacts;

	public PanelTabbedPane(GUIMain guiMain) {

		this.guiMain = guiMain;
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());

		JPanel panelTabs = new JPanel();
		panelTabs.setBackground(Color.GRAY);
		panelTabs.setPreferredSize(new Dimension(250, 35));
		panelTabs.setMaximumSize(new Dimension(350, 35));
		panelTabs.setLayout(new BoxLayout(panelTabs, BoxLayout.LINE_AXIS));
		{
			btChats = new JButton() {
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Sans Serif", Font.BOLD, 14));
					int xChat = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Chats") / 2;
					int yChats = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;

					g2.setColor(Color.WHITE);
					g2.drawString("Chats", xChat, yChats);

					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(getWidth() - 1, 5, getWidth() - 1, getHeight() - 10);
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
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());

					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("Sans Serif", Font.BOLD, 14));
					int xChat = getWidth() / 2 - g2.getFontMetrics(g2.getFont()).stringWidth("Contacts") / 2;
					int yChats = getHeight() / 2 - g2.getFontMetrics(g2.getFont()).getHeight() / 2 + 15;

					g2.setColor(Color.WHITE);
					g2.drawString("Contacts", xChat, yChats);

					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(2.0F));
					g2.drawLine(-1, 5, -1, getHeight() - 10);

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
				@Override
				public void paintComponent(Graphics g) {

					g.setColor(Color.DARK_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			panelContacts.setLayout(new BoxLayout(panelContacts, BoxLayout.PAGE_AXIS));
			panelContacts.setOpaque(false);
			JLabel lbl = new JLabel(list.get(0).getContactNickname().substring(0, 1).toUpperCase(), JLabel.LEFT);
			lbl.setForeground(Color.CYAN);
			lbl.setBackground(new Color(0, 0, 0, 0));
			lbl.setOpaque(false);
			lbl.setFont(new Font("Sans Serif", Font.BOLD, 18));
			panelContacts.add(Box.createRigidArea(new Dimension(0, 5)));
			panelContacts.add(lbl);
			panelContacts.add(Box.createRigidArea(new Dimension(0, 3)));

			for (int i = 0; i < list.size(); i++) {
				if (i + i < list.size()) {
					PanelContactShowing panelContactShowing = new PanelContactShowing(guiMain, list.get(i));
					panelContacts.add(panelContactShowing);

					if (list.get(i).getContactNickname().substring(0, 1).toUpperCase()
							.equals(list.get(i + 1).getContactNickname().substring(0, 1).toUpperCase())) {
						panelContacts.add(Box.createRigidArea(new Dimension(0, 5)));
					} else {
						panelContacts.add(Box.createRigidArea(new Dimension(0, 15)));
						JLabel label = new JLabel(list.get(i + 1).getContactNickname().substring(0, 1).toUpperCase(),
								JLabel.LEFT);
						label.setForeground(Color.CYAN);
						label.setBackground(new Color(0, 0, 0, 0));
						label.setOpaque(false);
						label.setFont(new Font("Sans Serif", Font.BOLD, 18));
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
		jspContacts.setOpaque(false);
		jspContacts.setBackground(new Color(0, 0, 0, 0));
		jspContacts.setOpaque(false);
	}

	public void buildPanelChatContacts(ArrayList<SimpleContact> list) {

		ArrayList<SimpleContact> sortedList = guiMain.getMessagePanelManager().sortChatPanels(list);
		panelChats = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.setColor(Color.DARK_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelChats.setLayout(new BoxLayout(panelChats, BoxLayout.PAGE_AXIS));
		panelChats.setOpaque(false);

		for (SimpleContact contact : sortedList) {
			PanelContactShowing panelContactShowing = new PanelContactShowing(guiMain, contact);
			panelChats.add(panelContactShowing);
			panelChats.add(Box.createRigidArea(new Dimension(0, 5)));
		}

		jspChats = new JScrollPane(panelChats, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspChats.setOpaque(true);
		jspChats.setBackground(Color.LIGHT_GRAY);
		jspChats.setOpaque(false);
		add(jspChats, BorderLayout.CENTER);
	}

	private void handleAnimationToContacts() {

		if (animationRunning == false && chatsSelected) {
			remove(jspChats);
			add(jspContacts, BorderLayout.CENTER);
			revalidate();
			repaint();

			animationRunning = true;

			Timer timer = new Timer(10, e -> {

				if (xChats < 130) {
					xChats += 10;
					yChats = btChats.getHeight() - 5;
					btChats.repaint();
				}

				if (xContacts < 10) {
					xContacts += 10;
					yContacts = btContacts.getHeight() - 5;
					btContacts.repaint();
				}

				// if (xJspChats > -260) {
				// xJspChats -= 10;
				// jspChats.setLocation(xJspChats, 0);
				// panelCenter.revalidate();
				// }
				//
				// if (xJspContacts > 0) {
				// xJspContacts -= 10;
				// jspContacts.setLocation(xJspContacts, 0);
				// panelCenter.revalidate();
				// panelCenter.revalidate();
				// }
				if (xContacts >= 10) {
					((Timer) e.getSource()).stop();
					animationRunning = false;
				}

			});
			timer.start();
			chatsSelected = false;
		}
	}

	private void handleAnimationToChats() {

		if (animationRunning == false && chatsSelected == false) {
			remove(jspContacts);
			add(jspChats, BorderLayout.CENTER);
			repaint();
			animationRunning = true;

			Timer timer = new Timer(10, e -> {

				if (xChats > 10) {
					xChats -= 10;
					yChats = btChats.getHeight() - 5;
					btChats.repaint();
				}

				if (xContacts > -130) {
					xContacts -= 10;
					yContacts = btContacts.getHeight() - 5;
					btContacts.repaint();
				}

				// if (xJspChats < 0) {
				// xJspChats += 10;
				// jspChats.setLocation(xJspChats, 0);
				// panelCenter.revalidate();
				// }
				//
				// if (xJspContacts < 260) {
				// xJspContacts += 10;
				// jspContacts.setLocation(xJspContacts, 0);
				// panelCenter.revalidate();
				// panelCenter.revalidate();
				// }

				if (xChats <= 10) {
					((Timer) e.getSource()).stop();
					animationRunning = false;
				}

			});
			timer.start();
			chatsSelected = true;
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
