package jaims_development_studio.jaims.server;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import jaims_development_studio.jaims.server.account.Account;
import jaims_development_studio.jaims.server.account.AccountDAO;
import jaims_development_studio.jaims.server.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.user.User;
import jaims_development_studio.jaims.server.user.UserDAO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOTest {
	
	private final AccountDAO	accountDAO	= new AccountDAO();
	private final UserDAO		userDao		= new UserDAO();
	private final String		username	= "TestUser";
	private final String		usernameTmp	= "Tmp";
	
	@Test
	public void testTestUser() {
		accountDAO.delete(username);

		Account queryedTestUser = accountDAO.get(username);
		Assert.assertNull("Account shouldn't be available after deletion", queryedTestUser);

		Assert.assertTrue("After delete operation username should be available!", accountDAO.isUsernameAvailable(username));

		Account accountTestUser = new Account(username, "test", "email@test.com");
		try {
			accountDAO.saveOrUpdate(accountTestUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("Caught UserNameNotAvailableException! accountDAO.isUsernameAvailable(username) should have checked that!");
		}

		Assert.assertFalse("After saveOrUpdate operation username should not be available!", accountDAO.isUsernameAvailable(username));

		Account account = accountDAO.get(username);
		Assert.assertNotNull("Selected Account '" + username + "'. NotNull: ", account);
		Assert.assertEquals("Saved and fetched accounts should be equal!", account, accountTestUser);
	}

	@Test
	public void testTmpUser() {
		Account tmpUser = accountDAO.get(usernameTmp);
		if (tmpUser == null)
			tmpUser = new Account(usernameTmp, "test", "email");
		
		Assert.assertEquals(usernameTmp, tmpUser.getUsername());
		
		try {
			accountDAO.saveOrUpdate(tmpUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("Username '" + usernameTmp + "' is not available anymore!");
		}
	}

	@Test
	public void zTestUser() {
		Account account = accountDAO.get(usernameTmp);
		Assert.assertNotNull("No account fetched for username '" + usernameTmp + "'!", account);
		
		User user = userDao.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			Assert.assertNotNull("Account reference should not be null!", user.getAccount());
			userDao.saveOrUpdate(user);
			
			User fetchedUser = userDao.get(account.getUuid());
			Assert.assertNotNull("Account reference should not be null after database fetch!", fetchedUser.getAccount());
			Assert.assertEquals("Saved and fetched users should be equal!", user, fetchedUser);
		}
		
		user.setLastSeen();
		userDao.saveOrUpdate(user);
	}
	
}
