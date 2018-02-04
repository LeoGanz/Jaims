package jaims_development_studio.jaims.api.settings;

public enum EFileFormatType {
	FORMAT_WAVE(Values.FORMAT_WAVE), FORMAT_AIFC(Values.FORMAT_AIFC), FORMAT_AIFF(Values.FORMAT_AIFF), FORMAT_AU(
			Values.FORMAT_AU), FORMAT_SND(Values.FORMAT_SND);

	private String value;

	private EFileFormatType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	@SuppressWarnings("hiding")
	public static class Values {

		public static final String	FORMAT_WAVE	= "WAVE";
		public static final String	FORMAT_AIFC	= "AIFC";
		public static final String	FORMAT_AIFF	= "AIFF";
		public static final String	FORMAT_AU	= "AU";
		public static final String	FORMAT_SND	= "SND";
	}
}
