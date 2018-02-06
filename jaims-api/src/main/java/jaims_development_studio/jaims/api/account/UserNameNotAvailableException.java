package jaims_development_studio.jaims.api.account;


public class UserNameNotAvailableException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserNameNotAvailableException() {
		super();
	}

	public UserNameNotAvailableException(String s) {
		super(s);
	}

}
