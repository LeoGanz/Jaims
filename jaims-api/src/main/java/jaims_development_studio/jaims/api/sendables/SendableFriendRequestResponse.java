package jaims_development_studio.jaims.api.sendables;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author WilliGross
 */
@Entity(name = "SendableFriendRequestResponse")
@DiscriminatorValue(value = ESendableType.Values.FRIEND_REQUEST_RESPONSE)
public class SendableFriendRequestResponse extends SendableMessageResponse {
	
	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "SENDER_UUID", columnDefinition = "BINARY(16)")
	private final UUID			sender;
	
	@Column(name = "ACCEPTED", columnDefinition = "BOOLEAN")
	private final boolean		accepted;
	
	@Column(name = "DENIED", columnDefinition = "BOOLEAN")
	private final boolean		denied;
	
	@SuppressWarnings("unused")
	private SendableFriendRequestResponse() {
		this(null, null, null, null, null, null, null, null, null, false, false);
	}
	
	public SendableFriendRequestResponse(UUID friendRequestID, UUID sender, UUID recipient, Date timestampSent, 
			Date timestampServerReceived, Date timestampServerSent, Date timestampDelivered, Date timestampRead, 
			EMessageState state, boolean accepted, boolean denied) {
		super(friendRequestID, recipient, timestampSent, timestampServerReceived, timestampServerSent, timestampDelivered, 
				timestampRead, state, ESendableType.FRIEND_REQUEST_RESPONSE);
		this.sender = sender;
		this.accepted = accepted;
		this.denied = denied;
	}
	
	public SendableFriendRequestResponse(SendableMessageResponse messageResponse, UUID sender, boolean accepted, boolean denied) {
		this(messageResponse.getMessageID(), sender, messageResponse.getRecipient(), messageResponse.getTimestampSent(),
				messageResponse.getTimestampServerReceived(), messageResponse.getTimestampServerSent(),
				messageResponse.getTimestampDelivered(), messageResponse.getTimestampRead(),
				messageResponse.getState(), accepted, denied);
	}
	
	public UUID getSender() {
		return sender;
	}
	
	public boolean isAccepted() {
		return accepted;
	}
	
	public boolean isDenied() {
		return denied;
	}

}
