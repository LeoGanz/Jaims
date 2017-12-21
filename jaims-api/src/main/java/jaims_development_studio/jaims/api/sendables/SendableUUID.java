package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SendableUUID")
@DiscriminatorValue(value = ESendableType.Values.STORED_UUID)
public class SendableUUID extends Sendable {

	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "STORED_UUID", columnDefinition = "BINARY(16)")
	private final UUID			stored_uuid;
	
	@SuppressWarnings("unused")
	private SendableUUID() {
		this(null);
	}
	
	public SendableUUID(UUID uuid) {
		super(ESendableType.STORED_UUID, 3);
		stored_uuid = uuid;
	}
	
	public UUID getStoredUuid() {
		return stored_uuid;
	}

}
