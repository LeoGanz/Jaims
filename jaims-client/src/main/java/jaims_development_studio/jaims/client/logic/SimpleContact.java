package jaims_development_studio.jaims.client.logic;

import java.util.UUID;

/**
 * This class represents a contact with the least minimum of needed such as the
 * user's {@link UUID} which is needed to get all other information from the
 * database.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class SimpleContact {

	private UUID	contactID;
	private String	contactNickname;
	private boolean	chatExists;

	/**
	 * Constructor of class with all possible parameters to initialise fields
	 * 
	 * @param contactID
	 *            the contact's UUID
	 * @param contactNickname
	 *            the contacts nickname
	 * @param chatExists
	 *            boolean whether a chat with the contact exists
	 * 
	 * @see UUID
	 */
	public SimpleContact(UUID contactID, String contactNickname, boolean chatExists) {

		this.contactID = contactID;
		this.contactNickname = contactNickname;
		this.chatExists = chatExists;

	}

	/**
	 * Constructor
	 * 
	 * @param contactID
	 *            the contact's UUID
	 * @param contactNickname
	 *            the contact's nickname
	 * 
	 * @see UUID
	 */
	public SimpleContact(UUID contactID, String contactNickname) {

		this.contactID = contactID;
		this.contactNickname = contactNickname;
	}

	/**
	 * Constructor
	 * 
	 * @param contactNickname
	 *            the contact's nickname
	 * 
	 * @see UUID
	 */
	public SimpleContact(String contactNickname) {

		this.contactNickname = contactNickname;
	}

	/**
	 * @param uuid
	 *            the contact's new UUID
	 * 
	 * @see UUID
	 */
	public void setUUID(UUID uuid) {

		contactID = uuid;
	}

	/**
	 * @return the contact's UUID.
	 * 
	 * @see UUID
	 */
	public UUID getContactID() {

		return contactID;
	}

	/**
	 * @return the contact's nickname
	 */
	public String getContactNickname() {

		return contactNickname;
	}

	/**
	 * 
	 * @return true if chat with contact exists, otherwise false
	 */
	public boolean chatExists() {

		return chatExists;
	}

	/**
	 * 
	 * @param b
	 *            boolean indicates whether chat with contact exists
	 */
	public void setChatExists(boolean b) {

		chatExists = b;
	}
}
