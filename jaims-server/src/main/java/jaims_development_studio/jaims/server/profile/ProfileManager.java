package jaims_development_studio.jaims.server.profile;

import java.util.Date;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.profile.ProfileAlreadyExistsException;
import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.util.UuidEntity;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.UpdateTrackingUuidEntityManager;

/**
 * @author WilliGross
 */
public class ProfileManager extends UpdateTrackingUuidEntityManager<Profile> {
	
	public ProfileManager(UserManager userManager) {
		super(new ProfileDAO(), userManager, EEntityType.PROFILE);
	}
	
	/**
	 * Server is not supposed to create Profiles. It is the client's job to send a SendableProfile which the server will
	 * manage. Use method linked below instead.
	 *
	 * @see UpdateTrackingUuidEntityManager#saveOrUpdateEntity(UuidEntity)
	 */
	@Deprecated
	public Profile newProfile(Account account, String nickname, String description, String status, byte[] profilePicture) throws ProfileAlreadyExistsException {
		
		if (isUuidRegistered(account.getUuid()))
			throw new ProfileAlreadyExistsException(account.getUuid(), "A profile for the specified account / uuid already exists");
		
		Profile profile = new Profile(account, nickname, description, status, profilePicture, new Date());
		
		save(profile);
		
		return profile;
	}
	
}
