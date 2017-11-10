package jaims_development_studio.jaims.api.sendables;

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
	
	@SuppressWarnings("unused")
	private SendableConfirmation() {
		this(null);
	}
	
	public SendableConfirmation(EConfirmationType confirmationType) {
		super(ESendableType.CONFIRMATION, 10);
		this.confirmationType = confirmationType;
	}
	
	public EConfirmationType getConfirmationType() {
		return confirmationType;
	}
	
	public void setConfirmationType(EConfirmationType confirmationType) {
		this.confirmationType = confirmationType;
	}
	
	
}
