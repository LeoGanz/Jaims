package jaims_development_studio.jaims.client.directFileExchange.directFileExchangeSendables;

import java.io.Serializable;
import java.util.UUID;

import jaims_development_studio.jaims.client.directFileExchange.EFileExchangeType;

public class DFESendable implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2595997084329595958L;
	private EFileExchangeType	type;
	private UUID				sender, recipient;

	public DFESendable(EFileExchangeType type, UUID sender, UUID recipient) {

		this.type = type;
		this.sender = sender;
		this.recipient = recipient;
	}

	public EFileExchangeType getType() {

		return type;
	}

	public UUID getSender() {

		return sender;
	}

	public UUID getRecipient() {

		return recipient;
	}

}
