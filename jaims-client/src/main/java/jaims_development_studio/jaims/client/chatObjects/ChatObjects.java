package jaims_development_studio.jaims.client.chatObjects;

import java.util.ArrayList;

public class ChatObjects {
	
	Profile profileContact;
	String messageObjects;
	
	public ChatObjects(Profile profile) {
		profileContact = profile;
	}
	
	public void setProfileContact(Profile profile) {
		this.profileContact = profile;
	}
	
	public void setMessageObjectsArray(String list) {
		messageObjects = list;
	}
	
	public Profile getProfileContact() {
		return profileContact;
	}
	
	public String getList() {
		return messageObjects;
	}

}
