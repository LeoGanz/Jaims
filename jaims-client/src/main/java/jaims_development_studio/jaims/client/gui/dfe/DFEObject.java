package jaims_development_studio.jaims.client.gui.dfe;

import java.util.Date;
import java.util.UUID;

import jaims_development_studio.jaims.client.directFileExchange.fileUtil.EFileType;

public class DFEObject {

	private UUID		fileID, sender, recipient;
	private String		filename, path, extension;
	private EFileType	fileType;
	private Date		dateSent;

	public DFEObject(UUID fileID, UUID sender, UUID recipient, String filename, String path, String extension,
			EFileType fileType, Date dateSent) {

		this.fileID = fileID;
		this.sender = sender;
		this.recipient = recipient;
		this.filename = filename;
		this.path = path;
		this.extension = extension;
		this.fileType = fileType;
		this.dateSent = dateSent;
	}

	public UUID getFileID() {

		return fileID;
	}

	public UUID getSender() {

		return sender;
	}

	public UUID getRecipient() {

		return recipient;
	}

	public String getFilename() {

		return filename;
	}

	public String getPath() {

		return path;
	}

	public String getExtension() {

		return extension;
	}

	public EFileType getFileType() {

		return fileType;
	}

	public Date getDateSent() {

		return dateSent;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

}
