package jaims_development_studio.jaims.server;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.message.MessageAlreadyExistsException;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.message.MessageManager;
import jaims_development_studio.jaims.server.user.UserManager;

/**
 * @author WilliGross
 */
public class MessageAndFallbackUUIDGeneratorTest {

	private final Logger	LOG			= LoggerFactory.getLogger(MessageAndFallbackUUIDGeneratorTest.class);

	private static UUID staticUUID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

	private UserManager		userManager;
	private AccountManager	accountManager;
	private MessageManager	messageManager;
	private final String	username	= "FallbackUUIDGeneratorTester";
	private Account			account;
	private User			user;
	
	@Before
	public void setup() {
		userManager = new UserManager(null);
		accountManager = userManager.getAccountManager();
		messageManager = userManager.getMessageManager();

		account = accountManager.get(username);
		if (account == null)
			account = new Account(username, "test", username + "@email.com");

		Assert.assertEquals("Account should have the username that was assigned to it", username, account.getUsername());

		try {
			accountManager.save(account);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("No exception expected as account should be updated instead of overwritten if username already exists");
		}

		user = userManager.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userManager.save(user);
		}
	}

	@Test
	public void test() throws UserNotFoundException, InvalidSendableException {
		TextMessage message1 = new TextMessage(null, account.getUuid(), "Testmessage1");
		try {
			messageManager.deliverMessage(new SendableMessage(message1), null);
		} catch (@SuppressWarnings("unused") MessageAlreadyExistsException e) {
			Assert.fail("MessageAlreadyExistsException should not occur for messasge1, as UUID is generated each time");
		}

		boolean messageWithStaticUUIDAlreadyExists = false;
		TextMessage message2 = new TextMessage(null, account.getUuid(), "Testmessage2 (static ID)");
		message2.setUuid(staticUUID);
		try {
			messageManager.deliverMessage(new SendableMessage(message2), null);
		} catch (@SuppressWarnings("unused") MessageAlreadyExistsException e) {
			LOG.info("Caught MessageAlreadyExistsException which is fine as you aren't supposed to try to update messages!");
			messageWithStaticUUIDAlreadyExists = true;
		}
		
		if (messageWithStaticUUIDAlreadyExists) //only one sendable exists
			user.takeSendable(); //clean up
		else {
			//exactly two sendables should be available
			UUID uuid1 = ((SendableMessage) user.takeSendable()).getMessage().getUuid(); //not necessarily corresponds with message 1
			UUID uuid2 = ((SendableMessage) user.takeSendable()).getMessage().getUuid();//not necessarily corresponds with message 2
			
			if (!(uuid1.equals(staticUUID) || uuid2.equals(staticUUID))) {
				System.out.println("uuid1: " + uuid1);
				System.out.println("uuid2: " + uuid2);
				Assert.fail("Both messages' uuids don't match with the static UUID!");
			}
		}
		
		userManager.save(user);
	}
	
}
