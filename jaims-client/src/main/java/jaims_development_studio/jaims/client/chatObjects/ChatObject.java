package jaims_development_studio.jaims.client.chatObjects;

import java.util.ArrayList;

import jaims_development_studio.jaims.api.profile.Profile;

public class ChatObject {

	Profile				profileContact;
	ArrayList<Message>	list	= new ArrayList<>();

	public ChatObject(Profile profile) {

		profileContact = profile;
	}

	public void setProfileContact(Profile profile) {

		this.profileContact = profile;
	}

	public void setMessageObjectsArray(ArrayList<Message> list) {

		this.list = list;
	}

	public Profile getProfileContact() {

		return profileContact;
	}

	public ArrayList<Message> getList() {

		return list;
	}

}
