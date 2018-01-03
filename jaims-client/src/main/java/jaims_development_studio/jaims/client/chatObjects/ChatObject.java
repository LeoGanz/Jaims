package jaims_development_studio.jaims.client.chatObjects;

public class ChatObject {

	Profile	profileContact;
	String	messageObjects;

	public ChatObject(Profile profile) {
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
