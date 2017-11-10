package jaims_development_studio.jaims.api.sendables;

import java.awt.Image;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "SendableMessage")
@DiscriminatorValue(value = ESendableType.Values.MESSAGE)
public class SendableMessage extends Sendable {

	private static final long	serialVersionUID	= 1L;
	@Column(name = "SENDER_UUID", columnDefinition = "BINARY(16)")
	private final UUID			sender;
	@Column(name = "RECIPIENT_UUID", columnDefinition = "BINARY(16)")
	private final UUID			recipient;
	@Column(name = "MESSAGE", columnDefinition = "LONGVARCHAR")
	private final String		message;
	@Column(name = "IMAGE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private final Image			image;
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
	private SendableMessage() {
		this(null, null, null);
	}

	public SendableMessage(UUID sender, UUID recipient, String message) {
		this(sender, recipient, message, null);
	}

	public SendableMessage(UUID sender, UUID recipient, String message, Image image) {
		super(ESendableType.MESSAGE, 5);
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

	public EMessageState getState() {
		return state;
	}

	@Override
	public void setTimestampSent() {
		super.setTimestampSent();
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