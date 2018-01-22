package jaims_development_studio.jaims.api.sendables;

public enum ERequestType {
	DELETE_ACCOUNT(Values.DELETE_ACCOUNT), PROFILE(Values.PROFILE), OTHER(Values.OTHER);

	private String value;
	
	private ERequestType(String value) {
		//		if (!this.value.equals(value))
		//			throw new IllegalArgumentException("Incorrect use of EMessageType!");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {
		
		public static final String	DELETE_ACCOUNT	= "DELETE_ACCOUNT";
		public static final String	PROFILE			= "PROFILE";
		public static final String	OTHER			= "OTHER";
	}
}
