package jaims_development_studio.jaims.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.user.UserManager;

/**
 * @author WilliGross
 */
public class UserManagerTest {

	private AccountManager	accountManager;
	private UserManager		userManager;
	private final String	usernameTmp	= "Tmp";

	@Before
	public void setup() {
		userManager = new UserManager(null);
		accountManager = userManager.getAccountManager();
		
		Account tmpUser = accountManager.get(usernameTmp);
		if (tmpUser == null)
			tmpUser = new Account(usernameTmp, "test", "email");

		Assert.assertEquals("User should have the username that was assigned to it", usernameTmp, tmpUser.getUsername());

		try {
			accountManager.save(tmpUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("No exception expected as user should be updated instead of overwritten if usrname already exists");
		}
	}

	@Test
	public void testUserManager() {
		Account account = accountManager.get(usernameTmp);
		Assert.assertNotNull("No account fetched for username '" + usernameTmp + "'!", account);

		User user = userManager.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			Assert.assertNotNull("Account reference should not be null!", user.getAccount());
			userManager.save(user);

			User fetchedUser = userManager.get(account.getUuid());
			Assert.assertNotNull("Account reference should not be null after database fetch!", fetchedUser.getAccount());
			Assert.assertEquals("Saved and fetched users should be equal!", user, fetchedUser);
		}

		user.updateLastSeen();
		userManager.save(user);
	}

}
