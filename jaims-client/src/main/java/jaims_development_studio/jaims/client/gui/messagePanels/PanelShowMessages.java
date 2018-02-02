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

import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.TextMessage;
import jaims_development_studio.jaims.client.gui.customGUIComponents.messages.VoiceMessage;

public class PanelShowMessages extends JPanel {

	private GUIMain	guiMain;
	private UUID	contactUUID;

	public PanelShowMessages(GUIMain guiMain, ArrayList<Message> mList) {

		this.guiMain = guiMain;
		initGUI(mList);
	}

	private void initGUI(ArrayList<Message> mList) {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createRigidArea(new Dimension(0, 5)));

		for (Message m : mList) {
			if (m.getSender().equals(guiMain.getUserUUID())) {
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage(m.getMessage(), guiMain, true);
					tm.setAlignmentX(Component.RIGHT_ALIGNMENT);
					add(tm);
					add(Box.createRigidArea(new Dimension(0, 5)));

				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.RIGHT_ALIGNMENT);
					add(vm);
					add(Box.createRigidArea(new Dimension(0, 5)));
				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}
			} else {
				contactUUID = m.getSender();
				if (m.getMessageType().equals("TEXT")) {
					TextMessage tm = new TextMessage(m.getMessage(), guiMain, true);
					tm.setAlignmentX(Component.LEFT_ALIGNMENT);
					add(tm);
					add(Box.createRigidArea(new Dimension(0, 5)));

				} else if (m.getMessageType().equals("IMAGE")) {

				} else if (m.getMessageType().equals("VOICE")) {
					VoiceMessage vm = new VoiceMessage(m.getSender(), m.getMessage(), true, guiMain);
					vm.setAlignmentX(Component.LEFT_ALIGNMENT);
					add(vm);
					add(Box.createRigidArea(new Dimension(0, 5)));

				} else if (m.getMessageType().equals("FILE")) {

				} else if (m.getMessageType().equals("LOCATION")) {

				} else if (m.getMessageType().equals("OTHER")) {

				} else {

				}

			}
		}
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