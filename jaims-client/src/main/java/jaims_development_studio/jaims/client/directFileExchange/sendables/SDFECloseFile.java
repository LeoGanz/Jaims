package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

public class SDFECloseFile extends DFESendable{
	
	private UUID fileID;
	
	public SDFECloseFile(UUID fileID, UUID user, UUID recipient) {

		super(UUID.randomUUID(), user, recipient, EDFESendableType.TYPE_CLOSEFILE);
		
		this.fileID = fileID;
	}

	
	public UUID getFileID() {
		return fileID;
	}
	

}
