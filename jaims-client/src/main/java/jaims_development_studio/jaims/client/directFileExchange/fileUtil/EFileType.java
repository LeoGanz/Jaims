package jaims_development_studio.jaims.client.directFileExchange.fileUtil;

import jaims_development_studio.jaims.client.gui.dfe.PanelDFEObject;

/**
 * 
 * This enum serves as a way for the program to apply a file image to the
 * {@link PanelDFEObject}, with the image corresponding to the file
 * type/extension.<br>
 * Because checking for all possible file extensions and then selecting a image
 * each time is too much work, multiple file types (e.g. ".wav", ".aac", ".mp3",
 * etc...) are merged together into a single {@code EFileTType} (in this case
 * EFileType.FILETYPE_AUDIO) to select the image in an easier way.
 * 
 * @since v0.1.0
 * 
 * @author Bu88le
 *
 */
public enum EFileType {
	FILETYPE_AUDIO(Values.FILETYPE_AUDIO),
	FILETYPE_PDF(Values.FILETYPE_PDF), 
	FILETYPE_VIDEO(Values.FILETYPE_VIDEO),
	FILETYPE_TEXT(Values.FILETYPE_TEXT), 
	FILETYPE_WORD(Values.FILETYPE_WORD), 
	FILETYPE_EXCEL(Values.FILETYPE_EXCEL),
	FILETYPE_ODT(Values.FILETYPE_ODT), 
	FILETYPE_ZIP(Values.FILETYPE_ZIP), 
	FILETYPE_PPT(Values.FILETYPE_PPT),
	FILETYPE_IMAGE(Values.FILETYPE_IMAGE), 
	FILETYPE_OTHER(Values.FILETYPE_OTHER), 
	FILETYPE_JAVA(Values.FILETYPE_JAVA);

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
		public static final String	FILETYPE_OTHER	= "FILETYPE_OTHER";
		public static final String	FILETYPE_JAVA	= "FILETYPE_JAVA";
	}

}