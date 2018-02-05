package jaims_development_studio.jaims.api.settings;

public enum EEndianness {
	BIG_ENDIAN(Values.BIG_ENDIAN), LITTLE_ENDIAN(Values.LITTLE_ENDIAN);

	private boolean value;

	private EEndianness(boolean value) {

		this.value = value;
	}

	public boolean getValue() {

		return value;
	}

	public static class Values {

		public static final boolean	BIG_ENDIAN		= true;
		public static final boolean	LITTLE_ENDIAN	= false;

	}

}
