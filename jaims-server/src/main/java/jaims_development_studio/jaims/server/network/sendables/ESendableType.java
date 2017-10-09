package jaims_development_studio.jaims.server.network.sendables;

public enum ESendableType {
	REGISTRATION(Values.REGISTRATION), LOGIN(Values.LOGIN), DELETE_ACCOUNT(Values.DELETE_ACCOUNT), //delete
	MESSAGE(Values.MESSAGE), MESSAGE_RESPONSE(Values.MESSAGE_RESPONSE), EXCEPTION(Values.EXCEPTION), COMMAND(Values.COMMAND), UUID(Values.UUID), PROFILE(Values.PROFILE), CONFIRMATION(Values.CONFIRMATION), OTHER(Values.OTHER); //maybe friend request? confirmation (after sth like account deletion, login ...)?
	
	private String value;
	
	private ESendableType(String value) {
		//		if (!this.value.equals(value))
		//			throw new IllegalArgumentException("Incorrect use of SendableType!");
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@SuppressWarnings("hiding")
	public static class Values {
		
		public static final String	REGISTRATION		= "REGISTRATION";
		public static final String	LOGIN				= "LOGIN";
		public static final String	DELETE_ACCOUNT		= "DELETE_ACCOUNT";		//delete
		public static final String	MESSAGE				= "MESSAGE";
		public static final String	MESSAGE_RESPONSE	= "MESSAGE_RESPONSE";
		public static final String	EXCEPTION			= "EXCEPTION";
		public static final String	COMMAND				= "COMMAND";
		public static final String	UUID				= "UUID";
		public static final String	PROFILE				= "PROFILE";
		public static final String	CONFIRMATION		= "CONFIRMATION";
		public static final String	OTHER				= "OTHER";
	}
}
