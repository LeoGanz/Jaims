package jaims_development_studio.jaims.server.network.sendables;

import java.util.Date;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;
import jaims_development_studio.jaims.server.network.sendables.SendableMessage.MessageState;

/**
 * This contains info that needs to be sent back to the sender client, like if the message has been delivered / read.
 * SendableMessage is supposed to have a method that generates an instance of this class.
 */
@Entity(name = "SendableMessageResponse")
@DiscriminatorValue(value = SendableType.Values.MESSAGE_RESPONSE)
public class SendableMessageResponse extends Sendable {

	private static final long	serialVersionUID	= 1L;
	private final UUID			recipient;
	private final Date			timestampSent, timestampServerReceived, timestampServerSent, timestampDelivered, timestampRead;
	private final MessageState	state;

	public SendableMessageResponse(UUID recipient, Date timestampSent, Date timestampServerReceived,
			Date timestampServerSent, Date timestampDelivered, Date timestampRead, MessageState state) {
		super(SendableType.MESSAGE_RESPONSE);
		this.recipient = recipient;
		this.timestampSent = timestampSent;
		this.timestampServerReceived = timestampServerReceived;
		this.timestampServerSent = timestampServerSent;
		this.timestampDelivered = timestampDelivered;
		this.timestampRead = timestampRead;
		this.state = state;
	}

	public UUID getRecipient() {
		return recipient;
	}

	@Override
	public Date getTimestampSent() {
		return timestampSent;
	}

	public Date getTimestampServerReceived() {
		return timestampServerReceived;
	}

	public Date getTimestampServerSent() {
		return timestampServerSent;
	}

	public Date getTimestampDelivered() {
		return timestampDelivered;
	}

	public Date getTimestampRead() {
		return timestampRead;
	}

	public MessageState getState() {
		return state;
	}

}
