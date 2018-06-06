package jaims_development_studio.jaims.server;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.user.UserManager;

/**
 * @author WilliGross
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountManagerTest {

	private final Logger	LOG			= LoggerFactory.getLogger(AccountManagerTest.class);
	
	private AccountManager	accountManager;
	private UserManager		userManager;
	private ProfileManager	profileManager;
	private final String	username	= "TestUser";

	@Before
	public void setup() {
		userManager = new UserManager(null);
		profileManager = new ProfileManager(userManager);
		accountManager = new AccountManager(userManager, profileManager);
	}

	@Test
	public void testTestUser() {
		accountManager.delete(username);

		Account queryedTestAccount = accountManager.get(username);
		Assert.assertNull("Account shouldn't be available after deletion", queryedTestAccount);

		Assert.assertTrue("After delete operation username should be available!", accountManager.isUsernameAvailable(username));

		Account accountTestUser = new Account(username, "test", "email@test.com");
		try {
			accountManager.save(accountTestUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("Caught UserNameNotAvailableException! accountManager.isUsernameAvailable(username) should have checked that!");
		}

		Assert.assertFalse("After save operation username should not be available!", accountManager.isUsernameAvailable(username));

		Account account = accountManager.get(username);
		Assert.assertNotNull("Selected Account '" + username + "'. NotNull: ", account);
		Assert.assertEquals("Saved and fetched accounts should be equal!", account, accountTestUser);
		
		Assert.assertNotNull("gettUuidForUsername() returned null", accountManager.getUuidForUsername(username));
		//		System.out.println(accountManager.getUuidForUsername(username));
	}

	@Test
	public void zzUsernameList() {
		List<String> usernames = accountManager.getAllRegisteredUsernames();
		StringBuilder stringBuilder = new StringBuilder("Usernames from all accounts in the database: \n");
		for (String s : usernames)
			stringBuilder.append(s + "\n");

		LOG.info(stringBuilder.toString());
	}

}
