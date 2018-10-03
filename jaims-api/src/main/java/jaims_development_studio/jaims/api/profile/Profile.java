package jaims_development_studio.jaims.api.profile;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;

@Entity(name = "Profile")
@Table(name = "PROFILES")
public class Profile extends UpdateTrackingUuidEntity {
	
	private static final long	serialVersionUID	= 2L;
	
	@Column(name = "NICKNAME", columnDefinition = "NVARCHAR(256)")
	private String				nickname;

	@Column(name = "REGISTRATION_NAME", columnDefinition = "NVARCHAR(256)")
	private String				registrationName;
	
	@Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR(4096)")
	private String				description;
	
	@Column(name = "STATUS", columnDefinition = "NVARCHAR(256)")
	private String				status;
	
	@Column(name = "PROFILE_PICTURE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[]				profilePicture;
	
	public Profile(Account account, String registrationName, String nickname, String description, String status, byte[] profilePicture, Date lastUpdated) {
		
		this(null, account, nickname, registrationName, description, status, profilePicture, lastUpdated);
	}
	
	public Profile(UUID uuid, Account account, String registrationName, String nickname, String description, String status, byte[] profilePicture, Date lastUpdated) {
		super(lastUpdated, account);
		setUuid(uuid);
		this.registrationName = registrationName;
		this.nickname = nickname;
		this.description = description;
		this.status = status;
		this.profilePicture = profilePicture;
	}
	
	private Profile() {
		super(null, null);
	}

	public String getRegistrationName() {

		return registrationName;
	}

	public void setRegistrationName(String registrationName) {

		this.registrationName = registrationName;
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
	
	
	@Override
	public Profile copyWithoutAccount() {
		
		return new Profile(getUuid(), null, registrationName, nickname, description, status, profilePicture, getLastUpdated());
	}
	
	@Override
	public String toString() {
		Account account = getAccount();

		if (account != null)
			return "Profile of " + account.toStringName();
		return "Profile of " + registrationName + " Nickname: " + nickname;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Profile other = (Profile) o;
		return new EqualsBuilder()
				.append(getUuid(), other.getUuid())
				.append(registrationName, other.registrationName)
				.append(nickname, other.nickname)
				.append(description, other.description)
				.append(status, other.status)
				.append(profilePicture, other.profilePicture)
				.append(getLastUpdated(), other.getLastUpdated())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(getUuid())
				.append(registrationName)
				.append(nickname)
				.append(description)
				.append(status)
				.append(profilePicture)
				.append(getLastUpdated())
				.toHashCode();
	}

	
}
