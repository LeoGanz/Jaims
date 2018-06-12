package jaims_development_studio.jaims.api.sendables;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * This contains info that needs to be sent back to the sender client, like if the message has been delivered / read.
 * SendableMessage is supposed to have a method that generates an instance of this class.
 *
 * @author WilliGross
 */
@Entity(name = "SendableMessageResponse")
@DiscriminatorValue(value = ESendableType.Values.MESSAGE_RESPONSE)
public class SendableMessageResponse extends Sendable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "MESSAGE_ID", columnDefinition = "BINARY(16)")
	private final UUID			messageID;

	@Column(name = "RECIPIENT_UUID", columnDefinition = "BINARY(16)")
	private final UUID			recipient;

	@Column(name = "TS_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date			timestampSent;

	@Column(name = "TS_SERVER_RECEIVED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date			timestampServerReceived;

	@Column(name = "TS_SERVER_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date			timestampServerSent;

	@Column(name = "TS_DELIVERED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date			timestampDelivered;

	@Column(name = "TS_READ", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date			timestampRead;

	@Column(name = "MESSAGE_STATE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private final EMessageState	state;

	@SuppressWarnings("unused")
	private SendableMessageResponse() {
		this(null, null, null, null, null, null, null, null);
	}
	
	public SendableMessageResponse(UUID messageID, UUID recipient, Date timestampSent, Date timestampServerReceived,
			Date timestampServerSent, Date timestampDelivered, Date timestampRead, EMessageState state) {
		this(messageID, recipient, timestampSent, timestampServerReceived, timestampServerSent, timestampDelivered, 
				timestampRead, state, ESendableType.MESSAGE_RESPONSE);
	}

	public SendableMessageResponse(UUID messageID, UUID recipient, Date timestampSent, Date timestampServerReceived, Date timestampServerSent, Date timestampDelivered, Date timestampRead, EMessageState state, ESendableType sendableType) {
		super(sendableType);
		this.messageID = messageID;
		this.recipient = recipient;
		this.timestampSent = timestampSent;
		this.timestampServerReceived = timestampServerReceived;
		this.timestampServerSent = timestampServerSent;
		this.timestampDelivered = timestampDelivered;
		this.timestampRead = timestampRead;
		this.state = state;
	}

	public UUID getMessageID() {
		return messageID;
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

	public EMessageState getState() {
		return state;
	}

}
