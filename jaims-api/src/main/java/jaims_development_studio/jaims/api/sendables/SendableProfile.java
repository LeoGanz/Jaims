package jaims_development_studio.jaims.api.sendables;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jaims_development_studio.jaims.api.profile.Profile;

@Entity(name = "SendableProfile")
@DiscriminatorValue(value = ESendableType.Values.PROFILE)
public class SendableProfile extends Sendable {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "PROFILE"/* , insertable = false, updatable = false */)
	private Profile				profile;

	@SuppressWarnings("unused")
	private SendableProfile() {
		this(null);
	}
	
	public SendableProfile(Profile profile) {
		super(ESendableType.PROFILE, 2);
		if (profile != null)
			this.profile = profile.copyWithoutAccount();
	}
	
	public Profile getProfile() {
		return profile;
	}

}
