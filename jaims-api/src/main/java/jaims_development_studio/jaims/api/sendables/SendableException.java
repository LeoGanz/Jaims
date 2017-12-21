package jaims_development_studio.jaims.api.sendables;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity(name = "SendableException")
@DiscriminatorValue(value = ESendableType.Values.EXCEPTION)
public class SendableException extends Sendable {
	
	private static final long	serialVersionUID	= 1L;

	@Column(name = "EXCEPTION")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private final Exception		exception;

	@SuppressWarnings("unused")
	private SendableException() {
		this(null);
	}
	
	public SendableException(Exception exception) {
		super(ESendableType.EXCEPTION, 30);
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
	
}
