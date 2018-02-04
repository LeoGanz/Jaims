package jaims_development_studio.jaims.client.settings;

public enum EInputEncodingType {
	ENCODING_ALAW(Values.ENCODING_ALAW), ENCODING_PCM_FLOAT(Values.ENCODING_PCM_FLOAT), ENCODING_PCM_SIGNED(
			Values.ENCODING_PCM_SIGNED), ENCODING_PCM_UNSIGNED(
					Values.ENCODING_PCM_UNSIGNED), ENCODING_ULAW(Values.ENCODING_ULAW);

	private String value;

	private EInputEncodingType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {

		public static final String	ENCODING_ALAW			= "ALAW";
		public static final String	ENCODING_PCM_FLOAT		= "PCM_FLOAT";
		public static final String	ENCODING_PCM_SIGNED		= "PCM_SIGNED";
		public static final String	ENCODING_PCM_UNSIGNED	= "PCM_UNSINGED";
		public static final String	ENCODING_ULAW			= "ULAW";
	}

}
