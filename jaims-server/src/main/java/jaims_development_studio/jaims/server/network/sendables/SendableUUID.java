package jaims_development_studio.jaims.server.network.sendables;

import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "SendableUUID")
@DiscriminatorValue(value = SendableType.Values.UUID)
public class SendableUUID extends Sendable {
	
	private static final long	serialVersionUID	= 1L;

	private final UUID			uuid;

	public SendableUUID(UUID uuid) {
		super(SendableType.UUID);
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
	
}
