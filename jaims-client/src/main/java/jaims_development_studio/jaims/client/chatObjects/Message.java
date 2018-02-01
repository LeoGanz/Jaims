package jaims_development_studio.jaims.client.chatObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private UUID				messageID, sender, recipient;
	private String				messageType, message;
	private Date				delievered, read, recieved;

	public Message(UUID messageID, UUID sender, UUID recipient, String messageType, Date delievered, Date read,
			Date recieved, String message) {

		this.messageID = messageID;
		this.sender = sender;
		this.recipient = recipient;
		this.messageType = messageType;
		this.delievered = delievered;
		this.read = read;
		this.recieved = recieved;
		this.message = message;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	public UUID getMessageID() {

		return messageID;
	}

	public UUID getSender() {

		return sender;
	}

	public UUID getRecipient() {

		return recipient;
	}

	public String getMessageType() {

		return messageType;
	}

	public String getMessage() {

		return message;
	}

	public Date getDelievered() {

		return delievered;
	}

	public Date getRead() {

		return read;
	}

	public Date getRecieved() {

		return recieved;
	}

}
