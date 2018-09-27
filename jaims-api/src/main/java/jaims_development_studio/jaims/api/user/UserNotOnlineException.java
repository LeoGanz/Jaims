package jaims_development_studio.jaims.api.user;

/**
 * This exception indicates that a problem occurred because of a user not being online.
 * 
 * @author WilliGross
 */
public class UserNotOnlineException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public UserNotOnlineException() {
		super();
	}
	
	public UserNotOnlineException(String s) {
		super(s);
	}
	
}
