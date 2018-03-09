package jaims_development_studio.jaims.client.gui.messagePanels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.SwingWorker;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.ERequestType;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.client.chatObjects.ChatInformation;
import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class ManageMessagePanels {

	private GUIMain								guiMain;
	private ArrayList<SimpleContact>			list;
	private HashMap<UUID, ArrayList<Message>>	availableChats			= new HashMap<>();
	private HashMap<UUID, ChatInformation>		contactChatInformation	= new HashMap<>();
	private HashMap<UUID, PanelShowMessages>	availableChatPanels		= new HashMap<>();
	private HashMap<UUID, PanelChat>			availablePanelChats		= new HashMap<>();

	public ManageMessagePanels(GUIMain guiMain) {

		this.guiMain = guiMain;
	}

	public void createMessageLists() {

		ArrayList<SimpleContact> list = guiMain.getSimpleChatContacts();
		for (int i = 0; i < list.size(); i++) {
			availableChats.put(list.get(i).getContactID(), guiMain.getMessageList(list.get(i).getContactID()));
		}

		createChatInformationList(list);

	}

	private void createChatInformationList(ArrayList<SimpleContact> list) {

		for (int i = 0; i < list.size(); i++) {
			contactChatInformation.put(list.get(i).getContactID(), new ChatInformation());
		}
		createChatPanels(list);
	}

	private void createChatPanels(ArrayList<SimpleContact> list) {

		PanelShowMessages panelShowMessages;
		for (int i = 0; i < list.size(); i++) {
			panelShowMessages = new PanelShowMessages(guiMain, availableChats.get(list.get(i).getContactID()),
					list.get(i).getContactID(), this);
			availableChatPanels.put(list.get(i).getContactID(), panelShowMessages);
		}

		createPanelChats(list);
	}

	private void createPanelChats(ArrayList<SimpleContact> list) {

		for (int i = 0; i < list.size(); i++) {
			PanelChat pc = new PanelChat(guiMain, list.get(i).getContactID(),
					new PanelChatWindowTop(list.get(i), guiMain), availableChatPanels.get(list.get(i).getContactID()));
			availablePanelChats.put(list.get(i).getContactID(), pc);
		}

		this.list = list;

	}

	public PanelChat getChatPanelForUser(UUID uuid) {

		if (availablePanelChats.containsKey(uuid))
			return availablePanelChats.get(uuid);
		else {
			guiMain.updateHasChat(true, uuid);
			availableChats.put(uuid, new ArrayList<Message>());
			contactChatInformation.put(uuid, new ChatInformation());
			PanelShowMessages ps = new PanelShowMessages(guiMain, availableChats.get(uuid), uuid, this);
			availableChatPanels.put(uuid, ps);
			PanelChat pc = new PanelChat(guiMain, uuid, new PanelChatWindowTop(guiMain.getSimpleContact(uuid), guiMain),
					ps);
			guiMain.addChatUser(guiMain.getSimpleContact(uuid));
			availablePanelChats.put(uuid, pc);
			return pc;
		}
	}

	public ArrayList<SimpleContact> sortChatPanels(ArrayList<SimpleContact> list) {

		try {
			list.sort((o1, o2) -> {

				if (availableChats.get(o1.getContactID()).get(availableChats.get(o1.getContactID()).size() - 1)
						.getRecieved().compareTo(availableChats.get(o2.getContactID())
								.get(availableChats.get(o2.getContactID()).size() - 1).getRecieved()) > 0) {
					return -1;
				} else if (availableChats.get(o1.getContactID()).get(availableChats.get(o1.getContactID()).size() - 1)
						.getRecieved().compareTo(availableChats.get(o2.getContactID())
								.get(availableChats.get(o2.getContactID()).size() - 1).getRecieved()) < 0) {
					return 1;
				} else {
					return 0;
				}
			});
		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();

		}
		return list;
	}

	public void addMessageToChat(jaims_development_studio.jaims.api.message.Message m, EMessageType messageType) {

		if (m.getMessageType().equals(EMessageType.TEXT)) {
			TextMessage tm = (TextMessage) m;
			if (availablePanelChats.containsKey(m.getSender()))
				availableChatPanels.get(tm.getSender()).addNewTextMessage(tm);
			else {
				sendEnquiry(m.getSender());
				while (guiMain.hasEntry(m.getSender()) == false) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				getChatPanelForUser(m.getSender());
				availableChatPanels.get(tm.getSender()).addNewTextMessage(tm);
			}

		} else if (m.getMessageType().equals(EMessageType.IMAGE)) {

		} else if (m.getMessageType().equals(EMessageType.VOICE)) {

		} else if (m.getMessageType().equals(EMessageType.FILE)) {

		} else if (m.getMessageType().equals(EMessageType.LOCATION)) {

		} else if (m.getMessageType().equals(EMessageType.OTHER)) {

		}
	}

	public void updateChatPanel(ParentPanel pp) {

		availablePanelChats.remove(pp.getPanelUUID());
		availablePanelChats.put(pp.getPanelUUID(), (PanelChat) pp);
	}

	public ChatInformation getContactChatInformation(UUID uuid) {

		return contactChatInformation.get(uuid);
	}

	public void updateMessages() {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {

				for (int i = 0; i < list.size(); i++)
					availableChatPanels.get(list.get(i).getContactID()).updateMessages();

				return null;
			}

		};
		worker.execute();
	}

	public void addNewMessage(UUID uuid, Message m) {

		try {
			availableChats.get(uuid).add(m);
		} catch (NullPointerException npe) {
			ArrayList<Message> list = new ArrayList<>();
			list.add(m);
			availableChats.put(uuid, list);
		}

		guiMain.updateChatPanels();

	}

	private void sendEnquiry(UUID uuid) {

		SendableRequest sr = new SendableRequest(ERequestType.PROFILE, uuid);
		guiMain.sendSendable(sr);
	}

}
