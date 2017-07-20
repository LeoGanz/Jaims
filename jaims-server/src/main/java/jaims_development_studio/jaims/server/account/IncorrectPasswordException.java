package jaims_development_studio.jaims.server.account;


public class IncorrectPasswordException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectPasswordException() {
		super();
	}

	public IncorrectPasswordException(String s) {
		super(s);
	}
	
}
