package jaims_development_studio.jaims.api.sendables;

/**
 * This enum can be used to specify the type of a {@link SendableRequest}. Use <b>'DELETE_ACCOUNT'</b> when requesting
 * the deletion of <i>your own</i> account, <b>'PROFILE'</b> when requesting an updated profile for your own or for a
 * foreign account and <b>SETTINGS</b> for the latest updates of an user's settings collection. <b>CONTACTS</b> can be
 * used to request your own contact list. Constant <b>'OTHER'</b> is used to show that no type has been set for a
 * {@link SendableRequest}. <br>
 * </br>
 * More request types are to be implemented.
 *
 * @author WilliGross
 */
public enum ERequestType {
	DELETE_ACCOUNT(Values.DELETE_ACCOUNT),
	PROFILE(Values.PROFILE),
	SETTINGS(Values.SETTINGS),
	CONTACTS(Values.CONTACTS),
	OTHER(Values.OTHER);
	
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
		public static final String	SETTINGS		= "SETTINGS";
		public static final String	CONTACTS		= "CONTACTS";
		public static final String	OTHER			= "OTHER";
	}
}
