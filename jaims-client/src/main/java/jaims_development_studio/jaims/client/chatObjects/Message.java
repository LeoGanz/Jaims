package jaims_development_studio.jaims.client.chatObjects;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable{
	UUID sender, recipient;
	String message;
	Image image;
	Date timestampRecieved, timestampSent;

	public Message(UUID sender, UUID recipient, String message, Date timestampSent, Date timestampRecieved) {
		this(sender, recipient, message, null, timestampSent, timestampRecieved);
	}

	public Message(UUID sender, UUID recipient, Image image, Date timestampSend, Date timestampRecieved) {
		this(sender, recipient, null, image, timestampSend, timestampRecieved);
	}

	public Message(UUID sender, UUID recipient, String message, Image image, Date timestampSent,
			Date timestampRecieved) {
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.image = image;
		this.timestampSent = timestampSent;
		this.timestampRecieved = timestampRecieved;
	}

	public UUID getSender() {
		return sender;
	}

	public UUID getRecipient() {
		return recipient;
	}

	public String getMessage() {
		return message;
	}

	public Image getImage() {
		return image;
	}
	
	public Date getTimestampSent() {
		return timestampSent;
	}
	
	public Date getTimestampRecieved() {
		return timestampRecieved;
	}
	
	public Object getMessageObject() {
		if (message != null) {
			return message;
		}else if (image != null) {
			return image;
		}else {
			return null;
		}
	}

}
