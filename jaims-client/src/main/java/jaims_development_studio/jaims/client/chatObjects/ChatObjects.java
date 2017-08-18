package jaims_development_studio.jaims.client.chatObjects;

import java.util.ArrayList;

public class ChatObjects {
	
	Profile profileContact;
	ArrayList<Message> messageObjects = new ArrayList<>();
	
	public ChatObjects(Profile profile) {
		profileContact = profile;
	}
	
	public void setProfileContact(Profile profile) {
		this.profileContact = profile;
	}
	
	public void addMessageObject(Message messageObject) {
		messageObjects.add(messageObject);
	}
	
	public void setMessageObjectsArray(ArrayList<Message> list) {
		messageObjects = list;
	}
	
	public Profile getProfileContact() {
		return profileContact;
	}
	
	public ArrayList<Message> getList() {
		return messageObjects;
	}

}
