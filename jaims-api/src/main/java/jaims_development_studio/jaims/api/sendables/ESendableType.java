package jaims_development_studio.jaims.api.sendables;

/**
 * @author WilliGross
 */
public enum ESendableType {
	REGISTRATION(Values.REGISTRATION),
	LOGIN(Values.LOGIN),
	DELETE_ACCOUNT(Values.DELETE_ACCOUNT), //delete
	CONFIRMATION(Values.CONFIRMATION),
	REQUEST(Values.REQUEST),
	STORED_UUID(Values.STORED_UUID),
	PROFILE(Values.PROFILE),
	SETTINGS(Values.SETTINGS),
	MESSAGE(Values.MESSAGE),
	MESSAGE_RESPONSE(Values.MESSAGE_RESPONSE),
	FRIEND_REQUEST_RESPONSE(Values.FRIEND_REQUEST_RESPONSE),
	COMMAND(Values.COMMAND),
	EXCEPTION(Values.EXCEPTION),
	SENDABLE_GROUP(Values.SENDABLE_GROUP),
	OTHER(Values.OTHER); //maybe friend request?
	
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
		
		public static final String	REGISTRATION			= "REGISTRATION";
		public static final String	LOGIN					= "LOGIN";
		public static final String	DELETE_ACCOUNT			= "DELETE_ACCOUNT";			//delete
		public static final String	CONFIRMATION			= "CONFIRMATION";
		public static final String	REQUEST					= "REQUEST";
		public static final String	STORED_UUID				= "STORED_UUID";
		public static final String	PROFILE					= "PROFILE";
		public static final String	SETTINGS				= "SETTINGS";
		public static final String	MESSAGE					= "MESSAGE";
		public static final String	MESSAGE_RESPONSE		= "MESSAGE_RESPONSE";
		public static final String	FRIEND_REQUEST_RESPONSE	= "FRIEND_REQUEST_RESPONSE";
		public static final String	COMMAND					= "COMMAND";
		public static final String	EXCEPTION				= "EXCEPTION";
		public static final String	SENDABLE_GROUP			= "SENDABLE_GROUP";
		public static final String	OTHER					= "OTHER";
	}
}
