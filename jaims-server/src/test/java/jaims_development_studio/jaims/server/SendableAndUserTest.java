package jaims_development_studio.jaims.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableTextMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.user.UserManager;

public class SendableAndUserTest {

	private AccountManager	accountManager;
	private UserManager		userManager;
	private ProfileManager	profileManager;

	@Before
	public void setup() {
		userManager = new UserManager(null);
		profileManager = new ProfileManager(userManager);
		accountManager = new AccountManager(userManager, profileManager);
	}
	
	@Test
	public void test() {
		String username = "SendableTester";

		Account account = accountManager.get(username);
		if (account == null) {
			account = new Account(username, "123456", username + "@test.com");
			Assert.assertNotNull("Acccount shouldn't be null!", account);
			try {
				accountManager.save(account);
			} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
				Assert.fail("Couldn't create account, even though nothing was fetched for the same username!");
			}
		}

		Assert.assertEquals("Fetched account object should match original!", account, accountManager.get(username));
		System.out.println(account.getUsername());

		User user = userManager.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userManager.save(user);
		}
		
		List<Sendable> sendables = new ArrayList<>(); //test probably only works if test sendables can be sorted unambiguously
		sendables.add(new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL, UUID.randomUUID()));
		sendables.add(new SendableTextMessage(account.getUuid(), account.getUuid(), "Hi!"));
		sendables.add(new SendableLogin(username, "PW"));

		sendables.sort((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()));
		
		for (Sendable s : sendables)
			user.enqueueSendable(s);
		
		userManager.save(user);
		
		List<Sendable> retrievedSendables = new ArrayList<>();
		while (!user.noSendableQueued())
			retrievedSendables.add(user.takeSendable());
		
		retrievedSendables.sort((s1, s2) -> Integer.compare(s1.getPriority(), s2.getPriority()));

		Assert.assertEquals("Sendables should be equal!", sendables, retrievedSendables);
		userManager.save(user); //important!!
	}

}
