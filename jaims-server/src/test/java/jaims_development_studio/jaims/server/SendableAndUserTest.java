package jaims_development_studio.jaims.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import jaims_development_studio.jaims.server.account.Account;
import jaims_development_studio.jaims.server.account.AccountDAO;
import jaims_development_studio.jaims.server.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.network.sendables.EConfirmationType;
import jaims_development_studio.jaims.server.network.sendables.Sendable;
import jaims_development_studio.jaims.server.network.sendables.SendableConfirmation;
import jaims_development_studio.jaims.server.user.User;
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
		sendables.add(new SendableConfirmation(EConfirmationType.LOGIN_SUCCESSFUL));

		for (Sendable s : sendables)
			user.enqueueSendable(s);

		userDAO.saveOrUpdate(user);
	}
	
}
