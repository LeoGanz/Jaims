package jaims_development_studio.jaims.client.gui.messagePanels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import jaims_development_studio.jaims.client.chatObjects.Message;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.SimpleContact;

public class ManageMessagePanels {

	private GUIMain								guiMain;
	private HashMap<UUID, ArrayList<Message>>	availableChats		= new HashMap<>();
	private HashMap<UUID, PanelShowMessages>	availableChatPanels	= new HashMap<>();
	private HashMap<UUID, PanelChat>			availablePanelChats	= new HashMap<>();

	public ManageMessagePanels(GUIMain guiMain) {

		this.guiMain = guiMain;
		createMessageLists();
	}

	private void createMessageLists() {

		ArrayList<SimpleContact> list = guiMain.getSimpleChatContacts();
		for (int i = 0; i < list.size(); i++) {
			availableChats.put(list.get(i).getContactID(), guiMain.getMessageList(list.get(i).getContactID()));
		}

		createChatPanels(list);
	}

	private void createChatPanels(ArrayList<SimpleContact> list) {

		PanelShowMessages panelShowMessages;
		for (int i = 0; i < list.size(); i++) {
			panelShowMessages = new PanelShowMessages(guiMain, availableChats.get(list.get(i).getContactID()));
			availableChatPanels.put(list.get(i).getContactID(), panelShowMessages);
		}

		createPanelChats(list);
	}

	private void createPanelChats(ArrayList<SimpleContact> list) {

		for (int i = 0; i < list.size(); i++) {
			PanelChat pc = new PanelChat(guiMain, availableChatPanels.get(list.get(i)).getContactID(),
					new PanelChatWindowTop(guiMain.getSimpleContact(list.get(i).getContactID()), guiMain),
					availableChatPanels.get(list.get(i).getContactID()));
			availablePanelChats.put(list.get(i).getContactID(), pc);
		}
	}

	public PanelChat getChatPanelForUser(UUID uuid) {

		if (availablePanelChats.containsKey(uuid))
			return availablePanelChats.get(uuid);
		else {
			return null;
		}
	}

	public ArrayList<SimpleContact> sortChatPanels(ArrayList<SimpleContact> list) {

		list.sort(new Comparator<SimpleContact>() {

			@Override
			public int compare(SimpleContact o1, SimpleContact o2) {

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
			}
		});
		return list;
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

}
