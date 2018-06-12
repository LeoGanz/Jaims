package jaims_development_studio.jaims.client.logic;

public enum EFileType {
	FILETYPE_AUDIO(Values.FILETYPE_AUDIO), FILETYPE_PDF(Values.FILETYPE_PDF), FILETYPE_VIDEO(
			Values.FILETYPE_VIDEO), FILETYPE_TEXT(Values.FILETYPE_TEXT), FILETYPE_WORD(
					Values.FILETYPE_WORD), FILETYPE_EXCEL(Values.FILETYPE_EXCEL), FILETYPE_OPT(
							Values.FILETYPE_ODT), FILETYPE_ZIP(Values.FILETYPE_ZIP), FILETYPE_PPT(
									Values.FILETYPE_PPT), FILETYPE_IMAGE(Values.FILETYPE_IMAGE);

	private String value;

	private EFileType(String value) {

		// if (!this.value.equals(value))
		// throw new IllegalArgumentException("Incorrect use of ConfirmationType!");
		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static class Values {

		public static final String	FILETYPE_AUDIO	= "FILETYPE_AUDIO";
		public static final String	FILETYPE_PDF	= "FILETYPE_PDF";
		public static final String	FILETYPE_VIDEO	= "FILETYPE_VIDEO";
		public static final String	FILETYPE_TEXT	= "FILETYPE_TEXT";
		public static final String	FILETYPE_WORD	= "FILETYPE_WORD";
		public static final String	FILETYPE_EXCEL	= "FILETYPE_EXCEL";
		public static final String	FILETYPE_ODT	= "FILETYPE_ODT";
		public static final String	FILETYPE_ZIP	= "FILETYPE_ZIP";
		public static final String	FILETYPE_PPT	= "FILETYPE_PPT";
		public static final String	FILETYPE_IMAGE	= "FILETYPE_IMAGE";
	}

}
