package jaims_development_studio.jaims.api.message;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.api.sendables.SendableFriendRequestResponse;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;

/**
 * @author WilliGross
 */
@Entity(name = "FriendRequest")
@DiscriminatorValue(value = EMessageType.Values.FRIEND_REQUEST)
public class FriendRequest extends TextMessage {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ACCEPTED", columnDefinition = "BOOLEAN")
	private boolean				accepted;

	@Column(name = "DENIED", columnDefinition = "BOOLEAN")
	private boolean				denied;

	@SuppressWarnings("unused")
	private FriendRequest() {
		this(null, null, null);
	}
	
	public FriendRequest(UUID sender, UUID recipient, String message) {
		this(sender, recipient, message, EMessageType.FRIEND_REQUEST);
	}
	
	protected FriendRequest(UUID sender, UUID recipient, String message, EMessageType messageType) {
		super(sender, recipient, message, messageType);
	}
	
	public void accept() {
		accepted = true;
		denied = false;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void deny() {
		accepted = false;
		denied = true;
	}

	public boolean isDenied() {
		return denied;
	}
	
	/**
	 * @return the same as {@link #buildFriendRequestResponse()}
	 */
	@Override
	public SendableMessageResponse buildMessageResponse() {
		return buildFriendRequestResponse();
	}

	public SendableFriendRequestResponse buildFriendRequestResponse() {
		return new SendableFriendRequestResponse(super.buildMessageResponse(), getRecipient(), accepted, denied);
	}
	
}
