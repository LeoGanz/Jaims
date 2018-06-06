package jaims_development_studio.jaims.api.message;

/**
 * @author WilliGross
 */
public class MessageAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public MessageAlreadyExistsException() {
		super();
	}
	
	public MessageAlreadyExistsException(String s) {
		super(s);
	}
	
}
