package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SendableUUID")
@DiscriminatorValue(value = ESendableType.Values.UUID)
public class SendableUUID extends Sendable {
	
	private static final long	serialVersionUID	= 1L;

	@Column(name = "STORED_UUID", columnDefinition = "BINARY(16)")
	private final UUID			uuid;

	@SuppressWarnings("unused")
	private SendableUUID() {
		this(null);
	}

	public SendableUUID(UUID uuid) {
		super(ESendableType.UUID, 3);
		this.uuid = uuid;
	}

	public UUID getStoredUuid() {
		return uuid;
	}
	
}
