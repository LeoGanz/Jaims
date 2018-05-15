package jaims_development_studio.jaims.server;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.contacts.Contacts;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.contacts.ContactsManager;
import jaims_development_studio.jaims.server.user.UserManager;

public class ContactsTest {
	
	private UserManager		userManager;
	private final String	username	= "Contacter";

	@Before
	public void setUp() {
		userManager = new UserManager(null);
		
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

		User user = userManager.get(account.getUuid());
		if (user == null) {
			user = new User(account);
			userManager.save(user);
		}
	}
	
	@Test
	public void test() {
		UUID accUUID = userManager.getUuidForUsername(username);
		ContactsManager contactsManager = userManager.getContactsManager();
		Contacts contacts = contactsManager.get(accUUID);
		if (contacts == null) {
			contacts = new Contacts();
			contacts.setUuid(accUUID);
			contacts.setAccount(userManager.getAccountManager().get(accUUID));
			contactsManager.saveOrUpdateEntity(contacts);
		}


		//		UUID[] uuids = { UUID.randomUUID(), UUID.randomUUID() };
		Assert.assertNotNull("Contacts shouldn't be null as it should have been stored just before", contacts);
		//		contacts.addContacts(uuids);
		contacts.updateLastUpdated();
		contactsManager.saveOrUpdateEntity(contacts);
		
		//		Assert.assertArrayEquals(uuids,
		//				contactsManager.get(userManager.getUuidForUsername(username)).getContacts().toArray());
	}
	
}
