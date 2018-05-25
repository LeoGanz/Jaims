package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jaims_development_studio.jaims.client.chatObjects.ChatInformation;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class PanelContactAndChatInformation extends ParentPanel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private GUIMain				guiMain;
	private SimpleContact		simpleContact;
	private ChatInformation		chatInformation;
	private Image				profilePicture;

	public PanelContactAndChatInformation(GUIMain guiMain, SimpleContact simpleContact,
			ChatInformation chatInformation) {

		this.guiMain = guiMain;
		this.simpleContact = simpleContact;
		this.chatInformation = chatInformation;

		profilePicture = guiMain.getProfileImage(simpleContact.getContactID()).getScaledInstance(100, 100,
				Image.SCALE_SMOOTH);
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		super.setPanelUUID(UUID.randomUUID());

		JLabel lbl = new JLabel(simpleContact.getContactNickname(), JLabel.CENTER);
		lbl.setBackground(new Color(0, 0, 0, 0));
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("Sans Serif", Font.BOLD, 16));
		lbl.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
		add(lbl, BorderLayout.PAGE_START);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.DARK_GRAY);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

		JPanel panelImage = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {

				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());

				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setClip(new RoundRectangle2D.Double(5, 5, 100, 100, 30, 30));
				g2.drawImage(profilePicture, 5, 5, this);

				g2.setClip(0, 0, getWidth(), getHeight());
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(1.8F));
				g2.drawRoundRect(5, 5, 100, 100, 30, 30);
			}
		};
		panelImage.setMinimumSize(new Dimension(110, 110));
		panelImage.setPreferredSize(panelImage.getMinimumSize());
		panelImage.setMaximumSize(panelImage.getMinimumSize());
		panelImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		centerPanel.add(panelImage);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 100)));

		JPanel panelStatus = new JPanel();
		panelStatus.setLayout(new BorderLayout());
		panelStatus.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(Color.WHITE), "Status"),
				new EmptyBorder(5, 5, 5, 5)));
		panelStatus.setForeground(Color.WHITE);
		panelStatus.setBackground(new Color(0, 0, 0, 0));
		panelStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelStatus.setOpaque(false);
		{
			JTextArea jta = new JTextArea(guiMain.getContactStatus(simpleContact.getContactID()));
			jta.setBackground(new Color(0, 0, 0, 0));
			jta.setOpaque(false);
			jta.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
			jta.setFont(new Font("Sans Serif", Font.PLAIN, 12));
			jta.setForeground(Color.white);
			panelStatus.add(jta);
		}
		centerPanel.add(panelStatus);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelDescription = new JPanel();
		panelDescription.setLayout(new BorderLayout());
		panelDescription.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(Color.WHITE), "Description"),
				new EmptyBorder(5, 5, 5, 5)));
		panelDescription.setForeground(Color.WHITE);
		panelDescription.setBackground(new Color(0, 0, 0, 0));
		panelDescription.setOpaque(false);
		panelDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		{
			JTextArea jta = new JTextArea(guiMain.getContactStatus(simpleContact.getContactID()));
			jta.setBackground(new Color(0, 0, 0, 0));
			jta.setOpaque(false);
			jta.setBorder(new LineBorder(new Color(0, 0, 0, 0)));
			jta.setLineWrap(true);
			jta.setWrapStyleWord(true);
			jta.setFont(new Font("Sans Serif", Font.PLAIN, 12));
			jta.setForeground(Color.white);
			panelDescription.add(jta);
		}
		centerPanel.add(panelDescription);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		JPanel panelStats = new JPanel();
		panelStats.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 2), "Chat-Statistiken"));
		panelStats.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.PAGE_AXIS));
		panelStats.setBackground(new Color(0, 0, 0, 0));
		panelStats.setOpaque(false);
		{
			panelStats.add(Box.createRigidArea(new Dimension(0, 5)));
			String[] arr = {"", ""};
			Object[][] data = {
					{"Insgesamt versandte Nachrichten: ", "<html><b>" + chatInformation.getTotalNumberMessages()},
					{"Davon Textnachrichten: ", "<html><b>" + chatInformation.getNumberTotalTextMessages()},
					{"Davon Sprachnachrichten: ", "<html><b>" + chatInformation.getNumberTotalVoiceMessages()},
					{"Selbst versandte Nachrichten: ", "<html><b>" + chatInformation.getNumberOwnMessages()},
					{"Davon Textnachrichten: ", "<html><b>" + chatInformation.getNumberOwnTextMessages()},
					{"Wörter benutzt von " + guiMain.getLoggedInUser().getContactNickname() + ": ",
							"<html><b>" + chatInformation.getNumberOwnWordsUsed()},
					{"Von " + simpleContact.getContactNickname() + " versandte Nachrichten: ",
							"<html><b>" + chatInformation.getNumberContactMessages()},
					{"Davon Textnachrichten: ", "<html><b>" + chatInformation.getNumberContactTextMessages()},
					{"Wörter benutzt von " + simpleContact.getContactNickname() + ": ",
							"<html><b>" + chatInformation.getNumberContactWordsUsed()},
					{"Insgesamt benutze Wörter: ", "<html><b>" + chatInformation.getNumberWordsUsed()},
					{"Selbst versandte Sprachnachrichten: ", "<html><b>" + chatInformation.getNumberOwnVoiceMessages()},
					{"Von " + simpleContact.getContactNickname() + " versandte Sprachnachrichten: ",
							"<html><b>" + chatInformation.getNumberContactVoiceMessages()}};
			JTable tableStats = new JTable(data, arr);
			tableStats.setForeground(Color.LIGHT_GRAY);
			tableStats.setFont(new Font("SansSerif", Font.PLAIN, 13));
			tableStats.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			tableStats.getColumnModel().getColumn(1).setMaxWidth(100);
			tableStats.getColumnModel().getColumn(1).setMinWidth(95);
			tableStats.getColumnModel().getColumn(1).setResizable(false);
			tableStats.setBorder(new LineBorder(Color.BLACK));
			tableStats.setAlignmentX(Component.CENTER_ALIGNMENT);
			tableStats.setBackground(new Color(0, 0, 0, 0));
			tableStats.setOpaque(false);
			tableStats.setRowHeight(25);
			panelStats.add(tableStats);
		}
		centerPanel.add(panelStats);

		JScrollPane jsp = new JScrollPane(centerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(jsp, BorderLayout.CENTER);

	}

}
