package jaims_development_studio.jaims.server.message;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.message.EMessageType;
import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.EntityManager;

public class MessageManager extends EntityManager<Message> {
	
	private final Logger		LOG	= LoggerFactory.getLogger(MessageManager.class);
	private final UserManager	userManager;
	
	public MessageManager(UserManager userManager) {
		super(new MessageDAO());
		this.userManager = userManager;
	}
	
	public void deliverMessage(SendableMessage sendableMessage, Server server)
			throws UserNotFoundException, InvalidSendableException {
		Message message = sendableMessage.getMessage();
		save(message);
		
		UUID recipientUuid = message.getRecipient();
		
		if (recipientUuid == null)
			throw new InvalidSendableException("Recipient of Message cannot be null", sendableMessage);
		
		//Message (command) for the server
		if ((server != null) && recipientUuid.equals(server.getServerUUID())
				&& (message.getMessageType() == EMessageType.TEXT)) {
			UUID senderUuid = message.getSender();
			User sender = userManager.get(senderUuid);
			
			if (sender == null)
				throw new UserNotFoundException("Could not find account of sender with UUID " + senderUuid + "!");
			
			TextMessage sendableTextMessage = (TextMessage) message;
			
			LOG.info(sender.getName() + ": " + sendableTextMessage.getMessage());
			
			if (sendableTextMessage.getMessage().startsWith("/"))
				server.addPendingCommand(sendableTextMessage.getMessage(), sender);
			return;
		}
		
		User recipient = userManager.get(recipientUuid);
		
		if (recipient == null)
			throw new UserNotFoundException("Could not deliver message to user " + recipientUuid + "!");
		
		recipient.enqueueSendable(sendableMessage);
		userManager.save(recipient);
	}
	
}
