package jaims_development_studio.jaims.server.profile;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;

public class Profile implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private String				nickname;
	private String				description;
	private String				status;
	private Image				profilePicture;
	private Date				lastUpdated;
	
	public Profile(String nickname, String description, String status, Image profilePicture, Date lastUpdated) {
		this.nickname = nickname;
		this.description = description;
		this.status = status;
		this.profilePicture = profilePicture;
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
	
	public Image getProfilePicture() {
		return profilePicture;
	}
	
	public void setProfilePicture(Image profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
