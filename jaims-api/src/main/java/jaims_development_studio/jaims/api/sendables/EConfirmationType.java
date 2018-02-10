package jaims_development_studio.jaims.api.sendables;

/**
 * @author WilliGross
 */
public enum EConfirmationType {
	LOGIN_SUCCESSFUL(Values.LOGIN_SUCCESSFUL),
	REGISTRATION_SUCCESSFUL(Values.REGISTRATION_SUCCESSFUL),
	PROFILE_UPDATE_SUCCESFUL(Values.PROFILE_UPDATE_SUCCESFUL),
	SETTINGS_UPDATE_SUCCESFUL(Values.SETTINGS_UPDATE_SUCCESFUL);
	
	private String value;
	
	private EConfirmationType(String value) {
		//		if (!this.value.equals(value))
		//			throw new IllegalArgumentException("Incorrect use of ConfirmationType!");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {
		
		public static final String	REGISTRATION_SUCCESSFUL		= "REGISTRATION_SUCCESSFUL";
		public static final String	LOGIN_SUCCESSFUL			= "LOGIN_SUCCESSFUL";
		public static final String	PROFILE_UPDATE_SUCCESFUL	= "PROFILE_UPDATE_SUCCESFUL";
		public static final String	SETTINGS_UPDATE_SUCCESFUL	= "SETTINGS_UPDATE_SUCCESFUL";
	}
}