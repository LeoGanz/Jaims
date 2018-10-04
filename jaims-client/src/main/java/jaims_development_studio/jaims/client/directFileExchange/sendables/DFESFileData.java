package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

/**
 * This sendable is, as the name suggests, responsible for taking in a chunk of
 * data from the file and send it to user B. It also saves the file's ID from
 * which the data is from in order to let user B be able to attach it to the
 * right file.
 * 
 * @since v0.1.0
 * 
 * @author Bu88le
 *
 */
public class DFESFileData extends DFESendable {

	private UUID	fileID;
	private byte[]	fileData;

	public DFESFileData(UUID fileID, UUID user, UUID recipient, byte[] data) {

		super(UUID.randomUUID(), user, recipient, EDFESendableType.TYPE_FILEDATA);
		this.fileID = fileID;
		fileData = data;

	}

	public UUID getFileID() {

		return fileID;
	}

	public byte[] getData() {

		return fileData;
	}

}
