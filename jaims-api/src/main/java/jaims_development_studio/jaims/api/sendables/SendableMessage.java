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
public class SendableMessage extends SendableUuidEntity {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private SendableMessage() {
		this(null);
	}

	public SendableMessage(Message message) {
		super(message, EEntityType.MESSAGE, ESendableType.MESSAGE);
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "MESSAGES")
	public Message getMessage() {
		return (Message) getEntity();
	}
	
	public EMessageType getMessageType() {
		return getMessage().getMessageType();
	}

	public void setTimestampServerReceived() {
		getMessage().setTimestampServerReceived();
	}

	public SendableMessageResponse buildMessageResponse() {
		return getMessage().buildMessageResponse();
	}
	
	public void setTimestampServerSent() {
		getMessage().setTimestampServerSent();
	}
	
}
