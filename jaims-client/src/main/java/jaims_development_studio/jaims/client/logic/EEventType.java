package jaims_development_studio.jaims.client.logic;

public enum EEventType {
	FRIEND_REQUEST(Values.FRIEND_REQUEST), UPDATE(Values.UPDATE);

	private String value;

	private EEventType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static class Values {

		public static final String	FRIEND_REQUEST	= "FRIEND_REQUEST";
		public static final String	UPDATE			= "UPDATE";

	}

}
