package jaims_development_studio.jaims.api.profile;

public class InsufficientPermissionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public InsufficientPermissionException() {
		super();
	}
	
	public InsufficientPermissionException(String s) {
		super(s);
	}
	
}
