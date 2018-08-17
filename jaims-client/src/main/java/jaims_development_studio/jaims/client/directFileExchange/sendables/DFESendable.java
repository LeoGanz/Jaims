package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

public class DFESendable {
	
	private UUID sendableID, sender, recipient;
	private EDFESendableType sType;
	
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
