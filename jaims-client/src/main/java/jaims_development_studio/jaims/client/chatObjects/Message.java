package jaims_development_studio.jaims.client.chatObjects;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UUID sender, recipient;
	String message;
	Image image;
	Date timestampRecieved, timestampSent;
	boolean voiceMessage;

	public Message(UUID sender, UUID recipient, String message, Date timestampSent, Date timestampRecieved, boolean voiceMessage) {
		this(sender, recipient, message, null, timestampSent, timestampRecieved, voiceMessage);
	}

	public Message(UUID sender, UUID recipient, Image image, Date timestampSend, Date timestampRecieved, boolean voiceMessage) {
		this(sender, recipient, null, image, timestampSend, timestampRecieved, voiceMessage);
	}

	public Message(UUID sender, UUID recipient, String message, Image image, Date timestampSent,
			Date timestampRecieved, boolean voiceMessage) {
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.image = image;
		this.timestampSent = timestampSent;
		this.timestampRecieved = timestampRecieved;
		this.voiceMessage = voiceMessage;
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
	
	public void setTimestampSent(Date sent) {
		timestampSent = sent;
	}
	
	public Date getTimestampRecieved() {
		return timestampRecieved;
	}
	
	public boolean getVoiceMessage() {
		return voiceMessage;
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
