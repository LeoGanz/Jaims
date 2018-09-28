package jaims_development_studio.jaims.client.directFileExchange.sendables;

/**
 * 
 * 
 * @since v0.1.0
 * 
 * @author Bu88le
 *
 */
public enum EDFESendableType {
	TYPE_FILEINFO(Values.TYPE_FILEINFO), 
	TYPE_STARTSENDING(Values.TYPE_STARTSENDING), 
	TYPE_CLOSEFILE(Values.TYPE_CLOSEFILE),
	TYPE_SENDLAST(Values.TYPE_SENDLAST),
	TYPE_CLOSECONNECTION(Values.TYPE_CLOSECONNECTION),
	TYPE_FILEDATA(Values.TYPE_FILEDATA),
	TYPE_PAUSESENDING(Values.TYPE_PAUSESENDING);

	private String value;

	private EDFESendableType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static class Values {
		public static final String	TYPE_FILEINFO			= "FILE_INFORMATION";
		public static final String	TYPE_STARTSENDING		= "START_SENDING";
		public static final String	TYPE_CLOSEFILE			= "CLOSE_FILE";
		public static final String	TYPE_SENDLAST			= "SEND_LAST";
		public static final String	TYPE_CLOSECONNECTION	= "CLOSE_CONNECTION";
		public static final String 	TYPE_FILEDATA 			= "FILE_DATA";
		public static final String 	TYPE_PAUSESENDING		= "PAUSE_SENDING";

	}

}
