package jaims_development_studio.jaims.client.chatObjects;

import java.util.ArrayList;

public class ChatObject {

	ClientProfile		profileContact;
	ArrayList<Message>	list	= new ArrayList<>();

	public ChatObject(ClientProfile profile) {

		profileContact = profile;
	}

	public void setProfileContact(ClientProfile profile) {

		this.profileContact = profile;
	}

	public void setMessageObjectsArray(ArrayList<Message> list) {

		this.list = list;
	}

	public ClientProfile getProfileContact() {

		return profileContact;
	}

	public ArrayList<Message> getList() {

		return list;
	}

}
