package jaims_development_studio.jaims.client.chatObjects;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


public class Profile implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private UUID				uuid;
	private String				nickname;
	private String				description;
	private String				status;
	private String				profilePicture;
	private Date				lastUpdated;
	
	public Profile(UUID uuid, String nickname, String description, String status, String profilePicture, Date lastUpdated) {
		this.uuid = uuid;
		this.nickname = nickname;
		this.description = description;
		this.status = status;
		this.profilePicture = profilePicture;
		this.lastUpdated = lastUpdated;
	}
	
	public Profile() {
		this(null, null, null, null, null, null);
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
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
	
	public String getProfilePicture() {
		return profilePicture;
	}
	
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
