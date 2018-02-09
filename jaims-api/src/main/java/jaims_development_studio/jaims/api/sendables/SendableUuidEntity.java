package jaims_development_studio.jaims.api.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.api.util.AccountUuidEntity;
import jaims_development_studio.jaims.api.util.UuidEntity;

/**
 * @author WilliGross
 */
@Entity(name = "SendableUuidEntity")
@DiscriminatorValue(value = ESendableType.Values.UUID_ENTITY)
public class SendableUuidEntity extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	
	private final EEntityType			entityType;

	private UuidEntity			uuidEntity;
	
	@SuppressWarnings("unused")
	private SendableUuidEntity() {
		this(null, EEntityType.OTHER);
	}

	public SendableUuidEntity(UuidEntity uuidEntity, EEntityType entityType) {
		this(uuidEntity, entityType, ESendableType.UUID_ENTITY);
	}

	public SendableUuidEntity(UuidEntity uuidEntity, EEntityType entityType, ESendableType sendableType) {
		super(sendableType, 2);
		this.entityType = entityType;
		if ((uuidEntity != null) && (uuidEntity instanceof AccountUuidEntity))
			this.uuidEntity = ((AccountUuidEntity) uuidEntity).copyWithoutAccount();
	}

	public UuidEntity getEntity() {
		return uuidEntity;
	}

	public EEntityType getEntityType() {
		return entityType;
	}
	
}
