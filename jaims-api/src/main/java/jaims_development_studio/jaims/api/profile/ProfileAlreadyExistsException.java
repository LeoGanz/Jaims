package jaims_development_studio.jaims.api.profile;

import java.util.UUID;

public class ProfileAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private UUID				uuid;
	
	public ProfileAlreadyExistsException() {
		super();
	}
	
	public ProfileAlreadyExistsException(String s) {
		super(s);
	}

	public ProfileAlreadyExistsException(UUID uuid) {
		super();
		this.uuid = uuid;
	}

	public ProfileAlreadyExistsException(UUID uuid, String s) {
		super(s);
		this.uuid = uuid;
	}

	public UUID getProfileUUID() {
		return uuid;
	}
	
}
