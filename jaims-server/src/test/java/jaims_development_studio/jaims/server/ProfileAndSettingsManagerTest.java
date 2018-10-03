package jaims_development_studio.jaims.server;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.settings.SettingsManager;
import jaims_development_studio.jaims.server.user.UserManager;

/**
 * @author WilliGross
 */
public class ProfileAndSettingsManagerTest {

	private AccountManager	accountManager;
	private UserManager		userManager;
	private ProfileManager	profileManager;
	private SettingsManager	settingsManager;
	private final String	username	= "Profiler";

	@Before
	public void setup() {
		userManager = new UserManager(null);
		profileManager = userManager.getProfileManager();
		settingsManager = userManager.getSettingsManager();
		accountManager = userManager.getAccountManager();

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
			profile = new Profile(account.getUuid(), account, account.getUsername(), "Nick", "Beschreibung", "testing", null, new Date());
			profileManager.saveOrUpdateEntity(profile);
		}

		Profile retrievedProfile = profileManager.get(accountManager.getUuidForUsername(username));

		Assert.assertEquals("Saved and retrieved  profiles should be equal", profile, retrievedProfile);
	}

	@Test
	public void testSettings() {
		Settings settings = settingsManager.get(accountManager.getUuidForUsername(username));
		if (settings == null) {
			Account account = accountManager.get(username);
			Assert.assertNotNull("Account shouldn't be null", account);
			settings = new Settings(account.getUuid());
			settingsManager.saveOrUpdateEntity(settings);
		}

		Settings retrievedSettings = settingsManager.get(accountManager.getUuidForUsername(username));

		Assert.assertEquals("Saved and retrieved settings should be equal", settings, retrievedSettings);
	}

}
