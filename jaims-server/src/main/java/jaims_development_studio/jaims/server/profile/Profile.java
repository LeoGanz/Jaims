package jaims_development_studio.jaims.server.profile;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jaims_development_studio.jaims.server.account.Account;

@Entity(name = "Profile")
@Table(name = "PROFILES")
public class Profile implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	@Column(name = "UUID", columnDefinition = "BINARY(16)")
	@Id
	private UUID				uuid;
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	private Account				account;
	@Column(name = "NICKNAME", columnDefinition = "NVARCHAR(256)")
	private String				nickname;
	@Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR(4096)")
	private String				description;
	@Column(name = "STATUS", columnDefinition = "NVARCHAR(256)")
	private String				status;
	@Column(name = "PROFILE_PICTURE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
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
