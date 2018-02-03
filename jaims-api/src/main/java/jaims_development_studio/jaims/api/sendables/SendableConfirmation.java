package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity(name = "SendableConfirmation")
@DiscriminatorValue(value = ESendableType.Values.CONFIRMATION)
public class SendableConfirmation extends Sendable {

	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "CONFIRMATION_TYPE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private EConfirmationType	confirmationType;
	
	@Column(name = "STORED_UUID", columnDefinition = "BINARY(16)")
	private UUID				storedUuid;
	
	@SuppressWarnings("unused")
	private SendableConfirmation() {
		this(null, null);
	}
	
	public SendableConfirmation(EConfirmationType confirmationType, UUID uuid) {
		super(ESendableType.CONFIRMATION, 10);
		this.confirmationType = confirmationType;
		storedUuid = uuid;
	}
	
	public EConfirmationType getConfirmationType() {
		return confirmationType;
	}
	
	public void setConfirmationType(EConfirmationType confirmationType) {
		this.confirmationType = confirmationType;
	}
	
	public UUID getStoredUuid() {
		return storedUuid;
	}
	
	public void setStoredUuid(UUID uuid) {
		storedUuid = uuid;
	}
	
}
