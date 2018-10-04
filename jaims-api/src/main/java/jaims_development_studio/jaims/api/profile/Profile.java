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

	private static final long	serialVersionUID	= 3L;

	@Column(name = "REGISTRATION_NAME", columnDefinition = "NVARCHAR(256)")
	private String				registrationName;

	@Column(name = "NICKNAME", columnDefinition = "NVARCHAR(256)")
	private String				nickname;

	@Column(name = "FULL_NAME", columnDefinition = "NVARCHAR(256)")
	private String				fullName;

	@Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR(4096)")
	private String				description;

	@Column(name = "STATUS", columnDefinition = "NVARCHAR(256)")
	private String				status;

	@Column(name = "PROFILE_PICTURE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[]				profilePicture;

	@Column(name = "PHONE_NUMBER", columnDefinition = "BIGINT")
	private long				phoneNumber;
	
	public Profile(Account account, String registrationName, String nickname, String fullName, String description, String status, byte[] profilePicture, long phoneNumber, Date lastUpdated) {

		this(null, account, registrationName, nickname, fullName, description, status, profilePicture, phoneNumber, lastUpdated);
	}

	public Profile(UUID uuid, Account account, String registrationName, String nickname, String fullName, String description, String status, byte[] profilePicture, long phoneNumber, Date lastUpdated) {
		super(lastUpdated, account);
		setUuid(uuid);
		this.registrationName = registrationName;
		this.nickname = nickname;
		this.fullName = fullName;
		this.description = description;
		this.status = status;
		this.profilePicture = profilePicture;
		this.phoneNumber = phoneNumber;
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

	public String getFullName() {

		return fullName;
	}

	public void setFullName(String fullName) {

		this.fullName = fullName;
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

	public long getPhoneNumber() {

		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {

		this.phoneNumber = phoneNumber;
	}

	@Override
	public Profile copyWithoutAccount() {

		return new Profile(getUuid(), null, registrationName, nickname, fullName, description, status, profilePicture, phoneNumber, getLastUpdated());
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
				.append(fullName, other.fullName)
				.append(description, other.description)
				.append(status, other.status)
				.append(profilePicture, other.profilePicture)
				.append(phoneNumber, other.phoneNumber)
				.append(getLastUpdated(), other.getLastUpdated())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(getUuid())
				.append(registrationName)
				.append(nickname)
				.append(fullName)
				.append(description)
				.append(status)
				.append(profilePicture)
				.append(phoneNumber)
				.append(getLastUpdated())
				.toHashCode();
	}
	

}
