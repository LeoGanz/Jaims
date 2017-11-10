package jaims_development_studio.jaims.api.sendables;

public enum EMessageState {
	UNSENT(Values.UNSENT), SENT(Values.SENT), SERVER_RECEIVED(Values.SERVER_RECEIVED), SERVER_SENT(Values.SERVER_SENT), DELIVERED(Values.DELIVERED), READ(Values.READ);

	private String value;
	
	private EMessageState(String value) {
		//		if (!this.value.equals(value))
		//			throw new IllegalArgumentException("Incorrect use of ConfirmationType!");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {
		
		public static final String	UNSENT			= "UNSENT";
		public static final String	SENT			= "SENT";
		public static final String	SERVER_RECEIVED	= "SERVER_RECEIVED";
		public static final String	SERVER_SENT		= "SERVER_SENT";
		public static final String	DELIVERED		= "DELIVERED";
		public static final String	READ			= "READ";
	}
}