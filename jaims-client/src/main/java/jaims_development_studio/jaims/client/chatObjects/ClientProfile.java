package jaims_development_studio.jaims.client.chatObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import jaims_development_studio.jaims.api.profile.Profile;

public class ClientProfile extends Profile implements Serializable {

	private static final long	serialVersionUID	= 1L;
	private UUID				uuid;
	private String				profilePicture;

	public ClientProfile(UUID uuid, String nickname, String description, String status, String profilePicture,
			Date lastUpdated) {

		super(null, nickname, description, status, null, lastUpdated);
		this.uuid = uuid;
	}

	public ClientProfile() {

		this(null, null, null, null, null, null);
	}

	public void setUUID(UUID uuid) {

		this.uuid = uuid;
	}

	public UUID getUuid() {

		return uuid;
	}

	public String getProfilePic() {

		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {

		this.profilePicture = profilePicture;
	}

}
