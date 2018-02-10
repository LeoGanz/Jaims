package jaims_development_studio.jaims.api.sendables;

/**
 * @author WilliGross
 */
public enum EEntityType {
	PROFILE(Values.PROFILE),
	SETTINGS(Values.SETTINGS),
	OTHER(Values.OTHER);

	private String value;
	
	private EEntityType(String value) {
		//		if (!this.value.equals(value))
		//			throw new IllegalArgumentException("Incorrect use of EMessageType!");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public ERequestType correspondingRequestType() {
		switch (value) {
			case Values.PROFILE:
				return ERequestType.PROFILE;
			case Values.SETTINGS:
				return ERequestType.SETTINGS;

			default:
				return ERequestType.OTHER;
		}
	}

	public EConfirmationType correspondingConfirmationType() {
		switch (value) {
			case Values.PROFILE:
				return EConfirmationType.PROFILE_UPDATE_SUCCESFUL;
			case Values.SETTINGS:
				return EConfirmationType.SETTINGS_UPDATE_SUCCESFUL;
			
			default:
				return null;
		}
	}

	@SuppressWarnings("hiding")
	public static class Values {
		
		public static final String	PROFILE			= "PROFILE";
		public static final String	SETTINGS		= "SETTINGS";
		public static final String	OTHER			= "OTHER";
	}
}
