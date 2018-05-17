package jaims_development_studio.jaims.api.message;

/**
 * @author WilliGross
 */
public enum EMessageType {
	TEXT(Values.TEXT), 
	IMAGE(Values.IMAGE), 
	VOICE(Values.VOICE), 
	FILE(Values.FILE), 
	LOCATION(Values.LOCATION), 
	CONTACT(Values.CONTACT), 
	CALENDARENTRY(Values.CALENDARENTRY), 
	OTHER(Values.OTHER);

	private String value;

	private EMessageType(String value) {

		// if (!this.value.equals(value))
		// throw new IllegalArgumentException("Incorrect use of EMessageType!");
		this.value = value;
	}

	public String getValue() {

		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {

		public static final String	TEXT			= "TEXT";
		public static final String	IMAGE			= "IMAGE";
		public static final String	VOICE			= "VOICE";
		public static final String	FILE			= "FILE";
		public static final String	LOCATION		= "LOCATION";
		public static final String	CONTACT			= "CONTACT";
		public static final String	CALENDARENTRY	= "CALENDARENTRY";
		public static final String	OTHER			= "OTHER";
	}
}
