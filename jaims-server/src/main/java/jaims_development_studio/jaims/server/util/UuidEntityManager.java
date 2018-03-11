package jaims_development_studio.jaims.server.util;

import java.util.ArrayList;
import java.util.List;
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
import jaims_development_studio.jaims.api.sendables.SendableSendableGroup;
import jaims_development_studio.jaims.api.sendables.SendableSettings;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.util.EntityUpdateFailedException;
import jaims_development_studio.jaims.api.util.NoEntityAvailableException;
import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;
import jaims_development_studio.jaims.api.util.UuidEntity;
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

	public void saveOrUpdateEntity(UuidEntity uuidEntity) {
		if (uuidEntity.getEntityType() != entityType)
			throw new IllegalArgumentException("Invalid entity type: " + uuidEntity.getEntityType() + ". Expected: " + entityType);
		if (!(uuidEntity instanceof UpdateTrackingUuidEntity))
			throw new IllegalArgumentException("Cannot process non update tracking uuid enitites!");
		
		//Note: do not update field 'lastUpdated' as that would interfere with the client's update requests
		@SuppressWarnings("unchecked")
		E newUuidEntity = (E) uuidEntity; //checked more or less with if statements
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

		List<UUID> uuids = null;

		//Convert username to uuid
		if (request.getUniversalUuid() != null) {
			uuids = new ArrayList<>();
			uuids.add(request.getUniversalUuid());
		} else if (request.getUniversalString() != null)
			uuids = userManager.getUuidsForUsername(request.getUniversalString());

		List<UpdateTrackingUuidEntity> entities = null;
		if (uuids != null) {
			entities = new ArrayList<>(uuids.size());
			for (UUID uuid : uuids)
				entities.add(get(uuid));
		}

		if ((entities == null) || (entities.size() == 0) || ((entities.size() == 1) && (entities.get(0) == null)))
			switch (entityType) {
				case SETTINGS:
					entities = new ArrayList<>(1);
					entities.add(new Settings());
					break;
				default:
					throw new NoEntityAvailableException("There is no" + entityType + "available for UUID " + request.getUniversalUuid() + " or username " + request.getUniversalString());
			}

		Sendable response;
		
		if (entities.size() == 1)
			if ((request.getUniversalDate() == null) || (entities.get(0).getLastUpdated() == null) || (request.getUniversalDate().compareTo(entities.get(0).getLastUpdated()) < 0))

				switch (entityType) {
					case PROFILE:
						response = new SendableProfile((Profile) entities.get(0));
						break;
					case SETTINGS:
						response = new SendableSettings((Settings) entities.get(0));
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
		else {
			List<Sendable> sendables = new ArrayList<>(entities.size());
			switch (entityType) {
				case PROFILE:
					for (UpdateTrackingUuidEntity entity : entities)
						sendables.add(new SendableProfile((Profile) entity));
					break;
				case SETTINGS:
					for (UpdateTrackingUuidEntity entity : entities)
						sendables.add(new SendableSettings((Settings) entity));
					break;
				default:
					throw new InvalidSendableException("Cannot process entity of type " + entityType, request);
			}
			response = new SendableSendableGroup(sendables);
		}
		sendSendable(response, requester);
	}
}
