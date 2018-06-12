package jaims_development_studio.jaims.api.message;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jaims_development_studio.jaims.api.sendables.EMessageState;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.util.UuidEntity;

/**
 * @author WilliGross
 */
@Entity(name = "Message")
@Table(name = "MESSAGES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MESSAGE_TYPE", discriminatorType = DiscriminatorType.STRING,
columnDefinition = "VARCHAR(64)", length = 64)
@DiscriminatorValue(value = EMessageType.Values.OTHER)
public class Message extends UuidEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "SENDER_UUID", columnDefinition = "BINARY(16)")
	private final UUID			sender;
	
	@Column(name = "RECIPIENT_UUID", columnDefinition = "BINARY(16)")
	private final UUID			recipient;
	
	@Column(name = "MESSAGE_TYPE", columnDefinition = "VARCHAR(64)", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private final EMessageType	messageType;

	@Column(name = "TS_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampSent;
	
	@Column(name = "TS_SERVER_RECEIVED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampServerReceived;
	
	@Column(name = "TS_SERVER_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampServerSent;
	
	@Column(name = "TS_DELIVERED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampDelivered;
	
	@Column(name = "TS_READ", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampRead;
	
	@Column(name = "MESSAGE_STATE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private EMessageState		state				= EMessageState.UNSENT;

	@SuppressWarnings("unused")
	private Message() {
		this(null, null, null);
	}

	protected Message(UUID sender, UUID recipient, EMessageType messageType) {
		this.messageType = messageType;
		this.sender = sender;
		this.recipient = recipient;
	}
	
	public SendableMessageResponse buildMessageResponse() {
		SendableMessageResponse messageResponse = new SendableMessageResponse(getUuid(), sender, timestampSent,
				timestampServerReceived, timestampServerSent, timestampDelivered, timestampRead, state);
		return messageResponse;
	}

	public EMessageType getMessageType() {
		return messageType;
	}

	public UUID getSender() {
		return sender;
	}

	public UUID getRecipient() {
		return recipient;
	}
	
	public EMessageState getState() {
		return state;
	}

	public void setTimestampSent() {
		timestampSent = new Date();
		state = EMessageState.SENT;
	}

	public void setTimestampServerReceived() {
		timestampServerReceived = new Date();
		state = EMessageState.SERVER_RECEIVED;
	}

	public Date getTimestampServerReceived() {
		return timestampServerReceived;
	}

	public void setTimestampServerSent() {
		timestampServerSent = new Date();
		state = EMessageState.SERVER_SENT;
	}

	public Date getTimestampServerSent() {
		return timestampServerSent;
	}

	public void setTimestampDelivered() {
		timestampDelivered = new Date();
		state = EMessageState.DELIVERED;
	}

	public Date getTimestampDelivered() {
		return timestampDelivered;
	}

	public void setTimestampRead() {
		timestampRead = new Date();
		state = EMessageState.READ;
	}

	public Date getTimestampRead() {
		return timestampRead;
	}
	
}
