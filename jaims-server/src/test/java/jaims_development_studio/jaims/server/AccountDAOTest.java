package jaims_development_studio.jaims.server;

import org.junit.Assert;
import org.junit.Test;

import jaims_development_studio.jaims.server.account.Account;
import jaims_development_studio.jaims.server.account.AccountDAO;
import jaims_development_studio.jaims.server.account.UserNameNotAvailableException;

public class AccountDAOTest {
	
	private final AccountDAO	accountDAO	= new AccountDAO();
	private final String		username	= "TestUser";
	
	@Test
	public void testTestUser() {
		accountDAO.delete(username);

		Account queryedTestUser = accountDAO.get(username);
		System.out.println(queryedTestUser == null ? "No Account found for '" + username + "' (correct)" : queryedTestUser.toStringName());
		
		Assert.assertTrue("After delete operation username should be available!", accountDAO.isUsernameAvailable(username));
		
		Account accountTestUser = new Account(username, "test", "email@test.com");
		try {
			System.out.println("saveOrUpdate TestUser");
			accountDAO.saveOrUpdate(accountTestUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("Caught UserNameNotAvailableException! accountDAO.isUsernameAvailable(username) should have checked that!");
		}
		
		System.out.println("isUsernameAvailable TestUser");
		Assert.assertFalse("After saveOrUpdate operation username should not be available!", accountDAO.isUsernameAvailable(username));
		
		Account account = accountDAO.get(username);
		System.out.println(account.toStringName());
		Assert.assertNotNull("Selected Account '" + username + "'. NotNull: ", account);
		Assert.assertEquals("Saved and fetched accounts should be equal!", account, accountTestUser);
	}

	@Test
	public void testTmpUser() {
		Account tmpUser = accountDAO.get("Tmp");
		System.out.println("TmpUser-UUID:" + tmpUser);
		if (tmpUser == null)
			tmpUser = new Account("Tmp", "test", "email");
		
		Assert.assertArrayEquals(new String[] { "Tmp" }, new String[] { tmpUser.getUsername() });
		
		try {
			System.out.println("saveOrUpdate Tmp");
			accountDAO.saveOrUpdate(tmpUser);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			System.out.println("Username 'Tmp' is not available anymore!");
		}
	}
	
}
