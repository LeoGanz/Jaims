package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.UUID;

public class SDFEStartSending extends DFESendable{

	private UUID fileID;
	private long startValue;
	
	public SDFEStartSending(UUID fileID, UUID user, UUID recipient, long startValue) {

		super(UUID.randomUUID(), user, recipient, EDFESendableType.TYPE_STARTSENDING);

		this.fileID = fileID;
		this.startValue = startValue;
	}
	
	public UUID getFileID() {
		return fileID;
	}
	
	public long getStartValue() {
		return startValue;
	}

}
