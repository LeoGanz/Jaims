package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import jaims_development_studio.jaims.server.profile.Profile;

@Entity(name = "SendableProfile")
@DiscriminatorValue(value = ESendableType.Values.PROFILE)
public class SendableProfile extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	@OneToOne(cascade = CascadeType.DETACH)
	private final Profile		profile;
	
	public SendableProfile(Profile profile) {
		super(ESendableType.PROFILE);
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}
	
}
