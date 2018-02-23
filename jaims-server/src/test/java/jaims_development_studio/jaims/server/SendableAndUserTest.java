package jaims_development_studio.jaims.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.user.UserManager;

public class SendableAndUserTest {
	
	private UserManager		userManager;
	
	@Before
	public void setup() {
		userManager = new UserManager(null);
	}

	@Test
	public void test() {
		String username = "SendableTester";
		
		Account account = userManager.getAccountManager().get(username);
		if (account == null) {
			account = new Account(username, "123456", username + "@test.com");
			Assert.assertNotNull("Acccount shouldn't be null!", account);
			try {
				userManager.getAccountManager().save(account);
			} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
				Assert.fail("Couldn't create account, even though nothing was fetched for the same username!");
			}
		}
		
		Assert.assertEquals("Fetched account object should match original!", account,
				userManager.getAccountManager().get(username));
		System.out.println(account.getUsername());
		
		User user = userManager.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userManager.save(user);
		}


		List<Sendable> sendables = new ArrayList<>(); //test probably only works if test sendables can be sorted unambiguously
		sendables.add(new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL, UUID.randomUUID()));
		SendableMessage sendableMessage = new SendableMessage(
				new TextMessage(account.getUuid(), account.getUuid(), "Hi!"));
		try {
			userManager.deliverMessage(sendableMessage);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Caught exceptions. See log for details.");
		}
		sendables.add(new SendableLogin(username, "PW"));

		for (Sendable s : sendables)
			user.enqueueSendable(s);
		
		sendables.add(sendableMessage);

		userManager.save(user);
		List<Sendable> retrievedSendables = new ArrayList<>();
		while (!user.noSendableQueued())
			retrievedSendables.add(user.takeSendable());

		sendables.sort((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()));
		retrievedSendables.sort((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()));
		
		Assert.assertEquals("Sendables should be equal!", sendables, retrievedSendables);
		userManager.save(user); //important!!
	}
	
}
