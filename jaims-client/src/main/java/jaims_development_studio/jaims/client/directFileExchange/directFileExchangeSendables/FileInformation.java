package jaims_development_studio.jaims.client.directFileExchange.directFileExchangeSendables;

import java.util.UUID;

import jaims_development_studio.jaims.client.directFileExchange.EFileExchangeType;
import jaims_development_studio.jaims.client.directFileExchange.EFileType;

public class FileInformation extends DFESendable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1659562456932423980L;
	private String[]			filenames, extensions;
	private long[]				lengths;
	private UUID[]				uuids;
	private EFileType[]			types;

	public FileInformation(UUID sender, UUID recipient, String[] filenames, String[] extensions, long[] lengths,
			UUID[] uuids, EFileType[] types) {

		super(EFileExchangeType.TYPE_FILEINFO, sender, recipient);
		this.filenames = filenames;
		this.lengths = lengths;
		this.uuids = uuids;
		this.types = types;
	}

	public String[] getFilenames() {

		return filenames;
	}

	public int getNumberOfFiles() {

		return filenames.length;
	}

	public String getFilenameAtPosition(int position) {

		return filenames[position];
	}

	public long[] getLengths() {

		return lengths;
	}

	public long getFilelengthAtPosition(int position) {

		return lengths[position];
	}

	public UUID[] getUUIDs() {

		return uuids;
	}

	public UUID getUUIDAtPosition(int position) {

		return uuids[position];
	}

	public String[] getExtensions() {

		return extensions;
	}

	public String getExtensionAtPosition(int position) {

		return extensions[position];
	}

	public EFileType[] getTypes() {

		return types;
	}

	public EFileType getTypeAtPosition(int position) {

		return types[position];
	}

}
