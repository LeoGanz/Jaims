package jaims_development_studio.jaims.server.network.sendables;

import java.awt.Image;
import java.util.Date;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "SendableMessage")
@DiscriminatorValue(value = SendableType.Values.MESSAGE)
public class SendableMessage extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	private final UUID			sender, recipient;
	private final String		message;
	private final Image			image;
	private Date				timestampServerReceived, timestampServerSent, timestampDelivered, timestampRead;
	private MessageState		state				= MessageState.UNSENT;
	
	public SendableMessage(UUID sender, UUID recipient, String message) {
		this(sender, recipient, message, null);
	}
	
	public SendableMessage(UUID sender, UUID recipient, String message, Image image) {
		super(SendableType.MESSAGE);
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.image = image;
	}
	
	public SendableMessageResponse buildMessageResponse() {
		SendableMessageResponse messageResponse = new SendableMessageResponse(sender, getTimestampSent(), timestampServerReceived, timestampServerSent, timestampDelivered, timestampRead, state);
		return messageResponse;
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
	
	public MessageState getState() {
		return state;
	}
	
	@Override
	public void setTimestampSent() {
		super.setTimestampSent();
		state = MessageState.SENT;
	}
	
	public void setTimestampServerReceived() {
		timestampServerReceived = new Date();
		state = MessageState.SERVER_RECEIVED;
	}
	
	public Date getTimestampServerReceived() {
		return timestampServerReceived;
	}
	
	public void setTimestampServerSent() {
		timestampServerSent = new Date();
		state = MessageState.SERVER_SENT;
	}
	
	public Date getTimestampServerSent() {
		return timestampServerSent;
	}
	
	public void setTimestampDelivered() {
		timestampDelivered = new Date();
		state = MessageState.DELIVERED;
	}
	
	public Date getTimestampDelivered() {
		return timestampDelivered;
	}
	
	public void setTimestampRead() {
		timestampRead = new Date();
		state = MessageState.READ;
	}
	
	public Date getTimestampRead() {
		return timestampRead;
	}
	
	public enum MessageState {
		UNSENT, SENT, SERVER_RECEIVED, SERVER_SENT, DELIVERED, READ
	}
}
