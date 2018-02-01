package jaims_development_studio.jaims.api.profile;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jaims_development_studio.jaims.api.account.Account;

@Entity(name = "Profile")
@Table(name = "PROFILES")
public class Profile implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "UUID", columnDefinition = "BINARY(16)")
	@Id
	private UUID				uuid;

	@OneToOne(cascade = CascadeType.DETACH)
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
	private byte[]				profilePicture;
	@Column(name = "LAST_UPDATED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdated;

	public Profile(Account account, String nickname, String description, String status, byte[] profilePicture,
			Date lastUpdated) {

		this(null, account, nickname, description, status, profilePicture, lastUpdated);
	}

	private Profile(UUID uuid, Account account, String nickname, String description, String status,
			byte[] profilePicture, Date lastUpdated) {

		this.uuid = uuid;
		this.account = account;
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

	public Account getAccount() {

		return account;
	}

	public void setAccount(Account account) {

		this.account = account;
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

	public byte[] getProfilePicture() {

		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {

		this.profilePicture = profilePicture;
	}

	public Date getLastUpdated() {

		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {

		this.lastUpdated = lastUpdated;
	}

	public Profile copyWithoutAccount() {

		return new Profile(uuid, null, nickname, description, status, profilePicture, lastUpdated);
	}

}
