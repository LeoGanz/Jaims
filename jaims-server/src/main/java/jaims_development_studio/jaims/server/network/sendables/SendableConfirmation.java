package jaims_development_studio.jaims.server.network.sendables;

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
	private EConfirmationType	type;
	
	public SendableConfirmation() {
		super(ESendableType.CONFIRMATION, 10);
	}
	
	public SendableConfirmation(EConfirmationType type) {
		super(ESendableType.CONFIRMATION, 10);
		this.type = type;
	}
	
	public EConfirmationType getConfirmationType() {
		return type;
	}
	
	public void setConfirmationType(EConfirmationType type) {
		this.type = type;
	}
	
	
}
