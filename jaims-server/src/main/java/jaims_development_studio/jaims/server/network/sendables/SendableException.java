package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "SendableException")
@DiscriminatorValue(value = SendableType.Values.EXCEPTION)
public class SendableException extends Sendable {
	
	private static final long serialVersionUID = 1L;

	private final Exception		exception;

	public SendableException(Exception exception) {
		super(SendableType.EXCEPTION);
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
	
}
