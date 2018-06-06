package jaims_development_studio.jaims.client.chatObjects;

import java.util.Date;
import java.util.UUID;

/**
 * This class represents a message which the client uses to retrieve data from
 * the database, and use it for further processing (that mainly concerns sorting
 * and displaying). This class exists because the api's version of a message
 * cannot be used because it doesn't contain the necessary information. It also
 * has no methods to change values of the variables but only returns the values
 * of the field which were initialised when the constructor was called.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class ClientInternMessage {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private UUID				messageID, sender, recipient;
	private String				messageType, message;
	private Date				sent, read, recieved;

	public ClientInternMessage(UUID messageID, UUID sender, UUID recipient, String messageType, Date sent, Date read,
			Date recieved, String message) {

		this.messageID = messageID;
		this.sender = sender;
		this.recipient = recipient;
		this.messageType = messageType;
		this.sent = sent;
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

	public Date getSent() {

		return sent;
	}

	public Date getRead() {

		return read;
	}

	public Date getRecieved() {

		return recieved;
	}
}
