package jaims_development_studio.jaims.api.sendables;

public class InvalidSendableTypeException extends Exception {

	private static final long	serialVersionUID	= 1L;
	private final Sendable		cause;

	public InvalidSendableTypeException(Sendable cause) {
		super();
		this.cause = cause;
	}
	
	public InvalidSendableTypeException(String s, Sendable cause) {
		super(s);
		this.cause = cause;
	}

	public Sendable getSendable() {
		return cause;
	}
	
}
