package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.user.UserNotOnlineException;

/**
 * This type of {@link Sendable} and all subtypes will not be persisted by the server. They will only be forwarded to
 * their recipient. If the recipient is not online or not registered exceptions will be thrown.
 *
 * @author WilliGross
 * @see UserNotOnlineException
 * @see UserNotFoundException
 */
public class SendableDirectDelivery extends Sendable {

	private static final long	serialVersionUID	= 1L;
	
	private UUID				recipient;

	public SendableDirectDelivery(ESendableType sendableType) {
		super(sendableType, 8);
	}

	public UUID getRecipient() {
		return recipient;
	}

	public void setRecipient(UUID recipient) {
		this.recipient = recipient;
	}
	
}
