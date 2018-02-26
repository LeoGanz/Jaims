package jaims_development_studio.jaims.api.sendables;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.Message;

/**
 * @author WilliGross
 */
@Entity(name = "SendableMessage")
@DiscriminatorValue(value = ESendableType.Values.MESSAGE)
public class SendableMessage extends Sendable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "MESSAGE", columnDefinition = "BINARY(16)")
	private final Message		message;
	
	@SuppressWarnings("unused")
	private SendableMessage() {
		this(null);
	}
	
	public SendableMessage(Message message) {
		super(ESendableType.MESSAGE);
		this.message = message;
	}
	

	public Message getMessage() {
		return message;
	}

	public EMessageType getMessageType() {
		return message.getMessageType();
	}
	
	@Override
	public void setTimestampSent() {
		super.setTimestampSent();
		message.setTimestampSent();
	}
	
	public void setTimestampServerReceived() {
		message.setTimestampServerReceived();
	}
	
	public void setTimestampServerSent() {
		message.setTimestampServerSent();
	}
	
	public void setTimestampDelivered() {
		message.setTimestampDelivered();
	}
	
	public void setTimestampRead() {
		message.setTimestampRead();
	}
	
	public SendableMessageResponse buildMessageResponse() {
		return getMessage().buildMessageResponse();
	}

}
