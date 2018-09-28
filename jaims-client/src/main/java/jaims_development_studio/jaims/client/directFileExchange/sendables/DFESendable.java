package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

/**
 * 
 * This class represents the sendable all other sendables extend.<br>
 * It only holds basic fiels, all other sendables need too, such as the ID of
 * the sendable, the sender's and recipient's UUID and the
 * {@link EDFESendableType}.
 * 
 * @since v0.1.0
 * 
 * @author Bu88le
 *
 */
public class DFESendable {

	private UUID				sendableID, sender, recipient;
	private EDFESendableType	sType;

	public DFESendable(UUID sendableID, UUID sender, UUID recipient, EDFESendableType sType) {

		this.sendableID = sendableID;
		this.sType = sType;
		this.sender = sender;
		this.recipient = recipient;
	}

	public UUID getSendableID() {

		return sendableID;
	}

	public UUID getSender() {

		return sender;
	}

	public UUID getRecipient() {

		return recipient;
	}

	public EDFESendableType getType() {

		return sType;
	}
}
