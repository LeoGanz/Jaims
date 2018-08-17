package jaims_development_studio.jaims.client.directFileExchange.sendables;

import java.util.Date;
import java.util.UUID;

import jaims_development_studio.jaims.client.directFileExchange.fileUtil.EFileType;

public class SDFEFileInfo extends DFESendable{
	
	private UUID fileID;
	private String filename, extension;
	private EFileType fileType;
	private long fileLength;
	private Date dateSent;

	public SDFEFileInfo(UUID fileID, UUID user, UUID recipient, String filename, String extension, EFileType fileType, long fileLength) {

		super(UUID.randomUUID(),user, recipient, EDFESendableType.TYPE_FILEINFO);
		
		this.fileID = fileID;
		this.filename = filename;
		this.extension = extension;
		this.fileType = fileType;
		this.fileLength = fileLength;
		
		dateSent = new Date(System.currentTimeMillis());
	}
	
	public UUID getFileID() {
		return fileID;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public EFileType getFileType() {
		return fileType;
	}

	public long getFileLength() {
		return fileLength;
	}
	
	public Date getDateSent() {
		return dateSent;
	}
	
}
