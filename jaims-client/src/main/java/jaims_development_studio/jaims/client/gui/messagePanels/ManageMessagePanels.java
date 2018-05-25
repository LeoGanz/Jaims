package jaims_development_studio.jaims.client.gui.messagePanels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.message.VoiceMessage;
import jaims_development_studio.jaims.client.chatObjects.ChatInformation;
import jaims_development_studio.jaims.client.chatObjects.ClientInternMessage;
import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.logic.SimpleContact;

/**
 * This class is responsible for building a panels needed for chatting with a
 * user and associating these panels with the user. This happens by storing the
 * built panels into <code>HashMap</code>s with the panel as the object and the
 * user's <code>UUID</code> as the key. This enables the program to simply
 * retrieve a users chat panel by asking <code>ManageMessagePanels</code> to
 * return the panel for a given <code>UUID</code>. This class furthermore also
 * holds <code>ChatInformation</code> objects for every user.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 * @see HashMap
 * @see UUID
 * @see ChatInformation
 */
public class ManageMessagePanels {

	private static final Logger								LOG							= LoggerFactory
			.getLogger(ManageMessagePanels.class);

	private GUIMain											guiMain;
	private ArrayList<SimpleContact>						list;
	private HashMap<UUID, ArrayList<ClientInternMessage>>	availableChats				= new HashMap<>();
	private HashMap<UUID, ChatInformation>					contactChatInformation		= new HashMap<>();
	private HashMap<UUID, PanelShowMessages>				availablePanelShowMessages	= new HashMap<>();
	private HashMap<UUID, PanelChat>						availablePanelChats			= new HashMap<>();

	/**
	 * Constructor of this class. Initialises the <code>GUIMain</code> instance of
	 * this class.
	 * 
	 * @param guiMain
	 *            a GUIMain instance
	 * 
	 * @see GUIMain
	 */
	public ManageMessagePanels(GUIMain guiMain) {

		this.guiMain = guiMain;
	}

	/**
	 * Creates a <code>HashMap</code> which holds an <code>ArrayList</code> with all
	 * available messages for every user. Calls
	 * {@link #createChatInformationList(ArrayList)}
	 * 
	 * @see ClientInternMessage
	 * @see GUIMain#getMessageList(UUID)
	 * @see HashMap
	 * @see ArrayList
	 */
	public void createMessageLists() {

		ArrayList<SimpleContact> list = guiMain.getSimpleChatContacts();
		for (int i = 0; i < list.size(); i++) {
			availableChats.put(list.get(i).getContactID(), guiMain.getMessageList(list.get(i).getContactID()));
		}

		createChatInformationList(list);

	}

	/**
	 * This methods iterates through a given <code>ArrayList</code> which holds all
	 * <code>SimpleContact</code>s that have an existing chat. While doing that it
	 * creates a <code>ChatInformation</code> instance for every user and adds it
	 * together with the user's <code>UUID</code> to a HashMap. After having done
	 * that it calls {@link #createPanelsShowMessages(ArrayList)}
	 * 
	 * @param list
	 *            the given list full of SimpleContacts
	 * 
	 * @see SimpleContact
	 * @see ChatInformation
	 * @see ArrayList
	 * @see UUID
	 * 
	 */
	private void createChatInformationList(ArrayList<SimpleContact> list) {

		for (int i = 0; i < list.size(); i++) {
			contactChatInformation.put(list.get(i).getContactID(), new ChatInformation());
		}
		createPanelsShowMessages(list);
	}

	/**
	 * This methods iterates through a given <code>ArrayList</code> which holds all
	 * <code>SimpleContact</code>s that have an existing chat. While doing that it
	 * creates a <code>PanelShowMessages</code> object for every user and adds it
	 * together with the user's <code>UUID</code> to a HashMap. After having done
	 * that it calls {@link #createPanelChats(ArrayList)}
	 * 
	 * @param list
	 *            the ArrayList to iterate through
	 * 
	 * @see SimpleContact
	 * @see PanelShowMessages
	 * @see UUID
	 * @see ArrayList
	 * @see HashMap
	 */
	private void createPanelsShowMessages(ArrayList<SimpleContact> list) {

		PanelShowMessages panelShowMessages;
		for (int i = 0; i < list.size(); i++) {
			panelShowMessages = new PanelShowMessages(guiMain, availableChats.get(list.get(i).getContactID()),
					list.get(i).getContactID(), this);
			availablePanelShowMessages.put(list.get(i).getContactID(), panelShowMessages);
		}

		createPanelChats(list);
	}

	/**
	 * This methods iterates through a given <code>ArrayList</code> which holds all
	 * <code>SimpleContact</code>s that have an existing chat. While doing that it
	 * creates a <code>PanelChat</code> object for every user and adds it together
	 * with the user's <code>UUID</code> to a HashMap.
	 * 
	 * @param list
	 *            the ArrayList to iterate through
	 * 
	 * @see SimpleContact
	 * @see PanelChat
	 * @see UUID
	 * @see ArrayList
	 * @see HashMap
	 */
	private void createPanelChats(ArrayList<SimpleContact> list) {

		for (int i = 0; i < list.size(); i++) {
			PanelChat pc = new PanelChat(guiMain, list.get(i).getContactID(),
					new PanelChatWindowTop(list.get(i), guiMain),
					availablePanelShowMessages.get(list.get(i).getContactID()));
			availablePanelChats.put(list.get(i).getContactID(), pc);
		}

		this.list = list;

	}

	/**
	 * Returns the <code>PanelChat</code> object associated with the user
	 * represented by the given <code>UUID</code>. If the HashMap has no existing
	 * entry for this key then all panels needed for a conversation are built. <br>
	 * See {@link #createMessageLists()} for what happens in this case and what
	 * objects and references are going to be built.
	 * 
	 * @param uuid
	 *            the user's representation
	 * @return the PanelChat for the User specified by the UUID
	 * 
	 * @see PanelChat
	 */
	public PanelChat getChatPanelForUser(UUID uuid) {

		if (availablePanelChats.containsKey(uuid))
			return availablePanelChats.get(uuid);
		else {
			availableChats.put(uuid, new ArrayList<ClientInternMessage>());
			contactChatInformation.put(uuid, new ChatInformation());
			PanelShowMessages ps = new PanelShowMessages(guiMain, availableChats.get(uuid), uuid, this);
			availablePanelShowMessages.put(uuid, ps);
			PanelChat pc = new PanelChat(guiMain, uuid, new PanelChatWindowTop(guiMain.getSimpleContact(uuid), guiMain),
					ps);
			guiMain.addChatUser(guiMain.getSimpleContact(uuid));
			availablePanelChats.put(uuid, pc);
			return pc;
		}
	}

	/**
	 * This method sorts a given <code>ArrayList</code> full of
	 * <code>SimpleContacts</code> based on when their last message was received.
	 * 
	 * @param list
	 *            list with all user that have a chat
	 * @return the sorted ArrayList
	 * 
	 * @see SimpleContact
	 */
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

			LOG.error("Index out of bounds!", e);

		} catch (NullPointerException npe) {
			LOG.error("List didn't exist!", npe);
		}
		return list;
	}

	/**
	 * This method receives a message and decides based on the
	 * <code>EMessageType</code> which method of the <code>PanelShowMessages</code>
	 * has to be called to add the message to the chat.
	 * 
	 * @param m
	 *            the message to be added
	 * @param messageType
	 *            the message's type
	 * 
	 * @see EMessageType
	 * @see Message
	 * @see PanelShowMessages
	 */
	public void addMessageToChat(Message m, EMessageType messageType) {

		if (m.getMessageType().equals(EMessageType.TEXT)) {
			TextMessage tm = (TextMessage) m;
			if (availablePanelChats.containsKey(m.getSender())) {
				availablePanelShowMessages.get(tm.getSender()).addNewTextMessage(tm);
				availablePanelChats.get(tm.getSender()).setScrollBarValueToMax();
			} else {

				getChatPanelForUser(m.getSender());
				availablePanelShowMessages.get(tm.getSender()).addNewTextMessage(tm);
			}

		} else if (m.getMessageType().equals(EMessageType.IMAGE)) {

		} else if (m.getMessageType().equals(EMessageType.VOICE)) {
			VoiceMessage vm = (VoiceMessage) m;
			if (availablePanelChats.containsKey(m.getSender())) {
				availablePanelShowMessages.get(vm.getSender()).addNewVoiceMessage(vm);
				availablePanelChats.get(vm.getSender()).setScrollBarValueToMax();
			} else {
				getChatPanelForUser(m.getSender());
				availablePanelShowMessages.get(vm.getSender()).addNewVoiceMessage(vm);
			}
		} else if (m.getMessageType().equals(EMessageType.FILE)) {

		} else if (m.getMessageType().equals(EMessageType.LOCATION)) {

		} else if (m.getMessageType().equals(EMessageType.OTHER)) {

		}
	}

	/**
	 * Updates a <code>PanelChat</code> for any user.
	 * 
	 * @param pp
	 *            the new PanelChat object
	 * 
	 * @see PanelChat
	 * @see ParentPanel
	 */
	public void updateChatPanel(ParentPanel pp) {

		availablePanelChats.remove(pp.getPanelUUID());
		availablePanelChats.put(pp.getPanelUUID(), (PanelChat) pp);
	}

	/**
	 * Returns the <code>ChatInformation</code> object for a user.
	 * 
	 * @param uuid
	 *            the user's UUID whose ChatInformation the program is looking for
	 * @return the user's ChatInformation
	 * 
	 * @see ChatInformation
	 */
	public ChatInformation getContactChatInformation(UUID uuid) {

		return contactChatInformation.get(uuid);
	}

	/**
	 * Calls {@link PanelShowMessages#updateMessages()} for every
	 * <code>PanelShowMessages available. This is done in a <code>SwingWorker</code>
	 * in order to retain the client's working order and let the user have a good
	 * user experience.
	 * 
	 * @see PanelShowMessages
	 * @see SwingWorker
	 */
	public void updateMessages() {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {

				for (int i = 0; i < list.size(); i++)
					availablePanelShowMessages.get(list.get(i).getContactID()).updateMessages();

				return null;
			}

		};
		worker.execute();
	}
}
