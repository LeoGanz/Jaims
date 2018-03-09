package jaims_development_studio.jaims.client.gui.messagePanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.VoiceMessage;

public class PanelShowMessages extends JPanel {

	private GUIMain					guiMain;
	private UUID					contactUUID;
	private ArrayList<TextMessage>	allTextMessages	= new ArrayList<>();
	private ManageMessagePanels		mmp;

	public PanelShowMessages(GUIMain guiMain, ArrayList<Message> mList, UUID contactID, ManageMessagePanels mmp) {

		this.guiMain = guiMain;
		this.mmp = mmp;
		contactUUID = contactID;
		initGUI(mList, contactID);
	}

	private void initGUI(ArrayList<Message> mList, UUID contactID) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(0, 5, 0, 5));
		add(Box.createRigidArea(new Dimension(0, 5)));

		for (Message m : mList) {
			mmp.getContactChatInformation(contactID).addTotalNumberMessages();
			if (m.getSender().equals(guiMain.getUserUUID())) {
				mmp.getContactChatInformation(contactID).addNumberOwnMessages();
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage((String) m.getMessage(), guiMain, true);
					allTextMessages.add(tm);
					JPanel ptm = new JPanel();
					ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
					ptm.setBackground(new Color(0, 0, 0, 0));
					ptm.setOpaque(false);
					ptm.add(Box.createHorizontalGlue());
					ptm.add(tm);
					add(ptm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberOwnTextMessages();
					mmp.getContactChatInformation(contactID).setNumberWordsUsed(m.getMessage().split(" ").length);
					mmp.getContactChatInformation(contactID).setNumberOwnWordsUsed(m.getMessage().split(" ").length);
				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.RIGHT_ALIGNMENT);
					add(vm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberTotalVoiceMessages();
					mmp.getContactChatInformation(contactID).addNumberOwnVoiceMessages();
				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}
			} else {
				mmp.getContactChatInformation(contactID).addNumberContactMessages();
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage(m.getMessage(), guiMain, false);
					allTextMessages.add(tm);

					JPanel ptm = new JPanel();
					ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
					ptm.setBackground(new Color(0, 0, 0, 0));
					ptm.setOpaque(false);
					ptm.add(tm);
					ptm.add(Box.createHorizontalGlue());
					add(ptm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberContactTextMessages();
					mmp.getContactChatInformation(contactID).setNumberWordsUsed(m.getMessage().split(" ").length);
					mmp.getContactChatInformation(contactID)
							.setNumberContactWordsUsed(m.getMessage().split(" ").length);
				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.LEFT_ALIGNMENT);
					add(vm);
					add(Box.createRigidArea(new Dimension(0, 5)));

					mmp.getContactChatInformation(contactID).addNumberTotalVoiceMessages();
					mmp.getContactChatInformation(contactID).addNumberContactVoiceMessages();
				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}

			}
		}
	}

	public void addNewUserMessage(String message) {

		mmp.getContactChatInformation(contactUUID).addTotalNumberMessages();
		mmp.getContactChatInformation(contactUUID).addNumberOwnMessages();

		TextMessage tm = new TextMessage(message, guiMain, true);
		JPanel ptm = new JPanel();
		ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
		ptm.setBackground(new Color(0, 0, 0, 0));
		ptm.add(Box.createHorizontalGlue());
		ptm.setOpaque(false);
		ptm.add(tm);
		add(ptm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		revalidate();
		repaint();
		guiMain.repaintParentPanel();
		allTextMessages.add(tm);

		mmp.getContactChatInformation(contactUUID).addNumberOwnTextMessages();
		mmp.getContactChatInformation(contactUUID).setNumberWordsUsed(message.split(" ").length);
		mmp.getContactChatInformation(contactUUID).setNumberOwnWordsUsed(message.split(" ").length);
	}

	public void addNewTextMessage(jaims_development_studio.jaims.api.message.TextMessage m) {

		mmp.getContactChatInformation(m.getSender()).addTotalNumberMessages();
		mmp.getContactChatInformation(m.getSender()).addNumberOwnMessages();

		TextMessage tm = new TextMessage(m.getMessage(), guiMain, false);
		JPanel ptm = new JPanel();
		ptm.setLayout(new BoxLayout(ptm, BoxLayout.LINE_AXIS));
		ptm.setBackground(new Color(0, 0, 0, 0));
		ptm.setOpaque(false);
		ptm.add(tm);
		ptm.add(Box.createHorizontalGlue());
		add(ptm);
		add(Box.createRigidArea(new Dimension(0, 5)));

		revalidate();
		repaint();
		allTextMessages.add(tm);

		mmp.getContactChatInformation(m.getSender()).addNumberOwnTextMessages();
		mmp.getContactChatInformation(m.getSender()).setNumberWordsUsed(m.getMessage().split(" ").length);
		mmp.getContactChatInformation(m.getSender()).setNumberOwnWordsUsed(m.getMessage().split(" ").length);

	}

	public void updateMessages() {

		Thread tTextMessages = new Thread() {
			@Override
			public void run() {

				for (TextMessage tm : allTextMessages)
					tm.updateMessage();
			}
		};
		tTextMessages.start();
	}

	public UUID getContactID() {

		return contactUUID;
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
