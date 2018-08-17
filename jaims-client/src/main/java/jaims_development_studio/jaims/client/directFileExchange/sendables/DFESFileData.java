package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

public class DFESFileData extends DFESendable{
	
	private UUID fileID;
	private byte[] fileData;

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
