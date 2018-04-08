package jaims_development_studio.jaims.client.logic;

import java.util.UUID;

public class SimpleContact {

	private UUID	contactID;
	private String	contactNickname;
	private boolean	chatExists;

	public SimpleContact(UUID contactID, String contactNickname, boolean chatExists) {

		this.contactID = contactID;
		this.contactNickname = contactNickname;
		this.chatExists = chatExists;

	}

	public SimpleContact(UUID contactID, String contactNickname) {

		this.contactID = contactID;
		this.contactNickname = contactNickname;
	}

	public SimpleContact(String contactNickname) {

		this.contactNickname = contactNickname;
	}

	public void setUUID(UUID uuid) {

		contactID = uuid;
	}

	public UUID getContactID() {

		return contactID;
	}

	public String getContactNickname() {

		return contactNickname;
	}

	public boolean chatExists() {

		return chatExists;
	}

	public void setChatExists(boolean b) {

		chatExists = b;
	}
}
