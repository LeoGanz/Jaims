package jaims_development_studio.jaims.client.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.ImageIcon;

public class Profile implements Serializable {
	
	/**
	 *
	 */
	private static final long	serialVersionUID	= 1623924841221148301L;
	
	private String				nickname;
	private String				description;
	private String				status;
	private Date				lastUpdated;
	private UUID				uuid;
	private byte[]				byteImage;
	private ArrayList<MessageObject> messages = new ArrayList<>();
	
	public Profile(String nickname, String description, String status, byte[] byteImage, Date lastUpdated) {
		this.nickname = nickname;
		this.description = description;
		this.status = status;
		this.byteImage = byteImage;
		this.lastUpdated = lastUpdated;
	}
	
	public Profile() {

	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public byte[] getByteImage() {
		return byteImage;
	}
	
	public void setByteImage(byte[] byteImage) {
		this.byteImage = byteImage;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void addMessage(MessageObject mo) {
		messages.add(mo);
	}
	
	public void removeMessage(int index) {
		messages.remove(index);
	}
	
	public void removeObject(MessageObject mo) {
		messages.remove(mo);
	}
	
	public ArrayList<MessageObject> getMessageArray() {
		return messages;
	}

}
