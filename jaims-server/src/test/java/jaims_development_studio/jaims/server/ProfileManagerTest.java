package jaims_development_studio.jaims.server;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.user.UserManager;

public class ProfileManagerTest {

	private AccountManager	accountManager;
	private UserManager		userManager;
	private ProfileManager	profileManager;
	private final String	username	= "Profiler";
	
	@Before
	public void setup() {
		userManager = new UserManager(null);
		profileManager = new ProfileManager(userManager);
		accountManager = new AccountManager(userManager, profileManager);
		
		Account account = accountManager.get(username);
		if (account == null)
			account = new Account(username, "test", "email");
		
		Assert.assertEquals("User should have the username that was assigned to it", username, account.getUsername());
		
		try {
			accountManager.save(account);
		} catch (@SuppressWarnings("unused") UserNameNotAvailableException e) {
			Assert.fail("No exception expected as user should be updated instead of overwritten if username already exists");
		}
	}

	@Test
	public void testProfiles() {
		Profile profile = profileManager.get(accountManager.getUuidForUsername(username));
		if (profile == null) {
			Account account = accountManager.get(username);
			Assert.assertNotNull("Account shouldn't be null", account);
			profile = new Profile(account.getUuid(), account, "Nick", "Beschreibung", "testing", null, new Date());
			SendableProfile sendableProfile = new SendableProfile(profile);
			profileManager.saveOrUpdateProfile(sendableProfile);
		}
		
		Profile retrievedProfile = profileManager.get(accountManager.getUuidForUsername(username));
		
		Assert.assertEquals("Saved and retrieved  profiles should be equal", profile, retrievedProfile);
	}

}
