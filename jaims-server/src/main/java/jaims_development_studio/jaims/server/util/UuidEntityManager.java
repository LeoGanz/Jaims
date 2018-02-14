package jaims_development_studio.jaims.server.util;

import java.util.UUID;

import javax.persistence.OptimisticLockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.InternalServerErrorException;
import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.EConfirmationType;
import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.sendables.SendableSettings;
import jaims_development_studio.jaims.api.sendables.SendableUuidEntity;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.util.EntityUpdateFailedException;
import jaims_development_studio.jaims.api.util.NoEntityAvailableException;
import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;
import jaims_development_studio.jaims.server.user.UserManager;

/**
 * @author WilliGross
 * @param <E> the type of entity this manager is supposed to take care of
 */
public class UuidEntityManager<E extends UpdateTrackingUuidEntity> extends EntityManager<E> {
	
	private final Logger		LOG	= LoggerFactory.getLogger(UuidEntityManager.class);
	
	private final UserManager	userManager;
	private final EEntityType	entityType;
	
	public UuidEntityManager(DAO<E> dao, UserManager userManager, EEntityType entityType) {
		super(dao);
		this.userManager = userManager;
		this.entityType = entityType;
	}
	
	protected void sendSendable(Sendable sendable, UUID uuid) {
		User user = userManager.get(uuid);
		if (user != null)
			user.enqueueSendable(sendable);
	}
	
	public void saveOrUpdateEntity(SendableUuidEntity sendableUuidEntity) throws InvalidSendableException {
		if (sendableUuidEntity.getEntityType() != entityType)
			throw new InvalidSendableException("Invalid request type: " + sendableUuidEntity.getEntityType() + ". Expected: " + entityType, sendableUuidEntity);
		if (!(sendableUuidEntity.getEntity() instanceof UpdateTrackingUuidEntity))
			throw new InvalidSendableException("Cannot process non update tracking uuid enitites!", sendableUuidEntity);

		//Note: do not update field 'lastUpdated' as that would interfere with the client's update requests
		@SuppressWarnings("unchecked")
		E newUuidEntity = (E) sendableUuidEntity.getEntity(); //checked more or less with if statements
		E oldUuidEntity = get(newUuidEntity.getUuid());

		if ((oldUuidEntity != null) && (oldUuidEntity.getLastUpdated().compareTo(newUuidEntity.getLastUpdated()) > 0)) {
			Exception exception = new EntityUpdateFailedException("Entity update failed for" + entityType + "with uuid " + newUuidEntity.getUuid());
			SendableException sendableException = new SendableException(exception);
			sendSendable(sendableException, newUuidEntity.getUuid());
			return;
		}

		Account account = userManager.getAccountManager().get(newUuidEntity.getUuid());
		newUuidEntity.setAccount(account);

		newUuidEntity.setUuid(null);
		oldUuidEntity = null;
		try {
			save(newUuidEntity);
		} catch (OptimisticLockException e) {
			LOG.error("Error when trying to persist new " + entityType + ": ", e);
			InternalServerErrorException internalServerError = new InternalServerErrorException("Internal error when saving profiles!");
			SendableException sendableException = new SendableException(internalServerError);
			sendSendable(sendableException, account.getUuid());
			return;
		}
		
		SendableConfirmation sendableConfirmation = new SendableConfirmation(entityType.correspondingConfirmationType(), account.getUuid());
		sendSendable(sendableConfirmation, account.getUuid());
	}
	
	public void manageRequest(SendableRequest request, UUID requester)
			throws InvalidSendableException, NoEntityAvailableException {
		if (request.getRequestType() != entityType.correspondingRequestType())
			throw new InvalidSendableException("Invalid request type: " + request.getRequestType() + ". Expected: " + entityType, request);
		
		//Convert username to uuid
		if ((request.getUniversalUuid() == null) && (request.getUniversalString() != null))
			request.setUniversalUuid(userManager.getUuidForUsername(request.getUniversalString()));
		
		UpdateTrackingUuidEntity entity = get(request.getUniversalUuid());
		
		if (entity == null)
			switch (entityType) {
				case SETTINGS:
					entity = new Settings();
					break;
				default:
					throw new NoEntityAvailableException("There is no" + entityType + "available for UUID " + request.getUniversalUuid() + " or username " + request.getUniversalString());
			}
		
		Sendable response;
		if ((request.getUniversalDate() == null) || (entity.getLastUpdated() == null)
				|| (request.getUniversalDate().compareTo(entity.getLastUpdated()) < 0))
			switch (entityType) {
				case PROFILE:
					response = new SendableProfile((Profile) entity);
					break;
				case SETTINGS:
					response = new SendableSettings((Settings) entity);
					break;
				default:
					throw new InvalidSendableException("Cannot process entity of type " + entityType, request);
			}
		else
			switch (entityType) {
				case PROFILE:
					response = new SendableConfirmation(EConfirmationType.PROFILE_UP_TO_DATE,
							request.getUniversalUuid());
					break;
				case SETTINGS:
					response = new SendableConfirmation(EConfirmationType.SETTINGS_UP_TO_DATE,
							request.getUniversalUuid());
					break;
				default:
					throw new InvalidSendableException("Cannot process entity of type " + entityType, request);
			}
		sendSendable(response, requester);
	}
}
