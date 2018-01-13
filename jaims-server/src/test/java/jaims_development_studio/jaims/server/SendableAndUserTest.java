package jaims_development_studio.jaims.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableTextMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.account.AccountDAO;
import jaims_development_studio.jaims.server.user.UserDAO;

public class SendableAndUserTest {
	
	@SuppressWarnings("static-method")
	@Test
	public void test() {
		String username = "SendableTester";
		AccountDAO accountDAO = new AccountDAO();
		UserDAO userDAO = new UserDAO();
		
		Account account = accountDAO.get(username);
		if (account == null) {
			account = new Account(username, "123456", username + "@test.com");
			Assert.assertNotNull("Acccount shouldn't be null!", account);
			try {
				accountDAO.saveOrUpdate(account);
			} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
				Assert.fail("Couldn't create account, even though nothing was fetched for the same username!");
			}
		}
		
		Assert.assertEquals("Fetched account object should match original!", account, accountDAO.get(username));
		System.out.println(account.getUsername());
		
		User user = userDAO.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userDAO.saveOrUpdate(user);
		}

		List<Sendable> sendables = new ArrayList<>();
		//		sendables.add(new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL));
		//		sendables.add(new SendableMessage(account.getUuid(), account.getUuid(), "Hi!"));
		sendables.add(new SendableLogin(username, "PW"));

		for (Sendable s : sendables)
			user.enqueueSendable(s);

		userDAO.saveOrUpdate(user);
		
		Assert.assertEquals("Sendables should be equal!", sendables.get(0), user.takeSendable());
		userDAO.saveOrUpdate(user); //important!!
	}

	@SuppressWarnings("static-method")
	@Test
	public void testSendables() {
		String username = "SendableTester2";
		AccountDAO accountDAO = new AccountDAO();
		UserDAO userDAO = new UserDAO();
		
		Account account = accountDAO.get(username);
		if (account == null) {
			account = new Account(username, "123456", username + "@test.com");
			Assert.assertNotNull("Acccount shouldn't be null!", account);
			try {
				accountDAO.saveOrUpdate(account);
			} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
				Assert.fail("Couldn't create account, even though nothing was fetched for the same username!");
			}
		}
		
		Assert.assertEquals("Fetched account object should match original!", account, accountDAO.get(username));
		System.out.println(account.getUsername());
		
		User user = userDAO.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userDAO.saveOrUpdate(user);
		}

		while (!user.noSendableQueued())
			user.takeSendable();

		List<Sendable> sendables = new ArrayList<>();
		sendables.add(new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL));
		sendables.add(new SendableTextMessage(account.getUuid(), account.getUuid(), "Hi!"));
		sendables.add(new SendableLogin(username, "PW"));

		for (Sendable s : sendables)
			user.enqueueSendable(s);

		userDAO.saveOrUpdate(user);
		
		Assert.assertEquals("Sendable lists' length should be equal!", sendables.size(), user.numberOfQueuedSendables());
	}
	
}
