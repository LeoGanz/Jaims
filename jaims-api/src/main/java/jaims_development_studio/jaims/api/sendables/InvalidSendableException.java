package jaims_development_studio.jaims.api.sendables;

/**
 * @author WilliGross
 */
public class InvalidSendableException extends Exception {
	
	private static final long	serialVersionUID	= 1L;
	private final Sendable		cause;
	
	public InvalidSendableException(Sendable cause) {
		super();
		this.cause = cause;
	}

	public InvalidSendableException(String s, Sendable cause) {
		super(s);
		this.cause = cause;
	}
	
	public Sendable getSendable() {
		return cause;
	}

}
