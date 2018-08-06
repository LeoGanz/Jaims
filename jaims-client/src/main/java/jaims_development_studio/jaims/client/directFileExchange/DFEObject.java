package jaims_development_studio.jaims.client.directFileExchange;

import java.util.Date;
import java.util.UUID;

//DFE = Direct File Exchange
public class DFEObject {

	private UUID		objectUUID, sender, recipient;
	private String		filename, path, extension;
	private EFileType	fileType;
	private Date		dateSent;

	public DFEObject(UUID objectUUID, UUID sender, UUID recipient, String filename, String path, String extension,
			EFileType fileType, Date dateSent) {

		this.objectUUID = objectUUID;
		this.sender = sender;
		this.recipient = recipient;
		this.filename = filename;
		this.path = path;
		this.extension = extension;
		this.fileType = fileType;
		this.dateSent = dateSent;
	}

	public UUID getObjectUUID() {

		return objectUUID;
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

}
