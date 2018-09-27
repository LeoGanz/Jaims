package jaims_development_studio.jaims.server.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableDirectDelivery;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.user.UserNotOnlineException;
import jaims_development_studio.jaims.server.network.ClientConnection;
import jaims_development_studio.jaims.server.network.ClientManager;

/**
 * This manager processes all direct deliveries, that is all {@link Sendable}s that are a subclass of
 * {@link SendableDirectDelivery}. Those Sendables are not persisted but only forwarded to the receiving user. This
 * operation takes place at connection level instead of user level. The {@link ClientConnection}s are provided by the
 * {@link ClientManager}.
 *
 * @author WilliGross
 * @see SendableDirectDelivery
 */
public class DirectDeliveryManager {

	private final Logger		LOG	= LoggerFactory.getLogger(DirectDeliveryManager.class);
	private final ClientManager	clientManager;

	public DirectDeliveryManager(ClientManager userManager) {
		clientManager = userManager;
	}
	
	/**
	 * This method forwards a {@link SendableDirectDelivery} to its recipient. It is not persisted.
	 *
	 * @param sendable the delivery that is to be forwarded
	 * @throws InvalidSendableException if no recipient UUID is provided
	 * @throws UserNotOnlineException if the recipient is not online
	 * @throws UserNotFoundException if no user with the specified ID is registered
	 */
	public void deliver(SendableDirectDelivery sendable) throws InvalidSendableException, UserNotFoundException, UserNotOnlineException {
		UUID recipientUuid = sendable.getRecipient();

		if (recipientUuid == null)
			throw new InvalidSendableException("Recipient of DirectDelivery cannot be null", sendable);
		
		ClientConnection recipient = clientManager.getConnection(recipientUuid);

		if (recipient == null) {
			if (clientManager.getUserManager().isUuidRegistered(recipientUuid))
				throw new UserNotOnlineException("DirectDelivery can not be forwarded because user " + recipientUuid + " is not online.");
			throw new UserNotFoundException("DirectDelivery can not be forwarded because no user with ID " + recipientUuid + " is registered.");
		}

		recipient.manageSendSendable(sendable);
		LOG.debug("DirectDelivery delivered to " + recipientUuid);
	}

}
