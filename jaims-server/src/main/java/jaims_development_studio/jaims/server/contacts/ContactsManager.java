package jaims_development_studio.jaims.server.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jaims_development_studio.jaims.api.contacts.Contacts;
import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.UpdateTrackingUuidEntityManager;


public class ContactsManager extends UpdateTrackingUuidEntityManager<Contacts> {
	
	private final ProfileManager profileManager;

	public ContactsManager(UserManager userManager) {
		super(new ContactsDAO(), userManager, EEntityType.CONTACTS);
		profileManager = userManager.getProfileManager();
	}
	
	public List<Sendable> getContactProfilesAsSendables(Contacts c) {
		List<Sendable> profiles = new ArrayList<>(c.getContacts().size());
		for (UUID uuid : c.getContacts())
			profiles.add(new SendableProfile(profileManager.get(uuid)));
		return profiles;
	}
}
