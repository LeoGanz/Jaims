package jaims_development_studio.jaims.client.directFileExchange;

public enum EFileExchangeType {
	TYPE_FILEINFO(Values.TYPE_FILEINFO), 
	TYPE_STARTSENDING(Values.TYPE_STARTSENDING), 
	TYPE_FINISHEDRECEIVING(Values.TYPE_FINISHEDRECEIVING),
	TYPE_CANCELSENDING(Values.TYPE_CANCELSENDING),
	TYPE_CANCELRECEIVING(Values.TYPE_CANCELRECEIVING);

	private String value;

	private EFileExchangeType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static class Values {
		public static final String	TYPE_FILEINFO			= "FILE_INFORMATION";
		public static final String	TYPE_STARTSENDING		= "START_SENDING";
		public static final String	TYPE_FINISHEDSENDING	= "FINISHED_SENDING";
		public static final String	TYPE_FINISHEDRECEIVING	= "FINISHED_RECEIVING";
		public static final String	TYPE_CANCELSENDING		= "CANCEL_SENDING";
		public static final String	TYPE_CANCELRECEIVING	= "CANCEL_RECEIVING";

	}

}
