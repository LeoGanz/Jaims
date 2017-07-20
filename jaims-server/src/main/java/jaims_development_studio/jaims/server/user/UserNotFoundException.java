package jaims_development_studio.jaims.server.user;


public class UserNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super();
	}
	
	public UserNotFoundException(String s) {
		super(s);
	}

}
