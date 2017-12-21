package jaims_development_studio.jaims.server.user;


public class UserNotLoggedInException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UserNotLoggedInException() {
		super();
	}
	
	public UserNotLoggedInException(String s) {
		super(s);
	}

}
