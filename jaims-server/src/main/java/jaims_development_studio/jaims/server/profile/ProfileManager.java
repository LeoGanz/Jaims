package jaims_development_studio.jaims.server.profile;

import java.util.Date;
import java.util.UUID;

import javax.persistence.OptimisticLockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.InternalServerErrorException;
import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.profile.NoProfileAvailableException;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.profile.ProfileAlreadyExistsException;
import jaims_development_studio.jaims.api.profile.ProfileUpdateFailedException;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.ERequestType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableTypeException;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.network.ClientManager;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.EntityManager;

public class ProfileManager extends EntityManager<Profile> {

	private final Logger				LOG	= LoggerFactory.getLogger(ClientManager.class);
	private final UserManager			userManager;

	public ProfileManager(UserManager userManager) {
		super(new ProfileDAO());
		this.userManager = userManager;
	}

	/**
	 * Server is not supposed to create Profiles. It is the client's job to send a SendableProfile which the server will
	 * manage. Use method linked below instead.
	 *
	 * @see #saveOrUpdateProfile(SendableProfile)
	 */
	@Deprecated
	public Profile newProfile(Account account, String nickname, String description, String status, byte[] profilePicture) throws ProfileAlreadyExistsException {

		if (isUuidRegistered(account.getUuid()))
			throw new ProfileAlreadyExistsException(account.getUuid(), "A profile for the specified account / uuid already exists");

		Profile profile = new Profile(account, nickname, description, status, profilePicture, new Date());

		save(profile);

		return profile;
	}

	public void saveOrUpdateProfile(SendableProfile sendableProfile) {
		//Note: do not update profile field 'lastUpdated' as that would interfere with the client's update requests
		Profile newProfile = sendableProfile.getProfile();
		Profile oldProfile = get(newProfile.getUuid());

		if ((oldProfile != null) && (oldProfile.getLastUpdated().compareTo(newProfile.getLastUpdated()) > 0)) {
			Exception exception = new ProfileUpdateFailedException("Profile Update Failed for profile with uuid " + newProfile.getUuid());
			SendableException sendableException = new SendableException(exception);
			sendSendable(sendableException, newProfile.getUuid());
			return;
		}

		Account account = userManager.getAccountManager().get(newProfile.getUuid());
		newProfile.setAccount(account);

		newProfile.setUuid(null);
		oldProfile = null;
		try {
			save(newProfile);
		} catch (OptimisticLockException e) {
			LOG.error("Error when trying to persist new Profile: ", e);
			InternalServerErrorException internalServerError = new InternalServerErrorException("Internal error when saving profiles!");
			SendableException sendableException = new SendableException(internalServerError);
			sendSendable(sendableException, account.getUuid());
			return;
		}
		
		SendableConfirmation sendableConfirmation = new SendableConfirmation(EConfirmationType.PROFILE_UPDATE_SUCCESFUL, account.getUuid());
		sendSendable(sendableConfirmation, account.getUuid());
	}
	

	private void sendSendable(Sendable sendable, UUID uuid) {
		User user = userManager.get(uuid);
		if (user != null)
			user.enqueueSendable(sendable);
	}

	public void manageProfileRequest(SendableRequest request) throws InvalidSendableTypeException, NoProfileAvailableException {
		if (request.getRequestType() != ERequestType.PROFILE)
			throw new InvalidSendableTypeException("Cannot process profile for a SendableRequest without RequestType PROFILE", request);

		//Convert username to uuid
		if ((request.getUniversalUuid() == null) && (request.getUniversalString() != null))
			request.setUniversalUuid(userManager.getUuidForUsername(request.getUniversalString()));
		
		Profile profile = get(request.getUniversalUuid());

		if (profile == null)
			throw new NoProfileAvailableException("There is no profile available for UUID " + request.getUniversalUuid() + " or username " + request.getUniversalString());

		User user = userManager.get(request.getUniversalUuid());

		if ((request.getUniversalDate() == null) || (request.getUniversalDate().compareTo(profile.getLastUpdated()) < 0))
			user.enqueueSendable(new SendableProfile(profile));
	}

}
