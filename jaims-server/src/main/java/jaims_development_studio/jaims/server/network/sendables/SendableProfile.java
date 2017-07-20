package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;
import jaims_development_studio.jaims.server.profile.Profile;

@Entity(name = "SendableProfile")
@DiscriminatorValue(value = SendableType.Values.PROFILE)
public class SendableProfile extends Sendable {

	private static final long	serialVersionUID	= 1L;
	private final Profile		profile;

	public SendableProfile(Profile profile) {
		super(SendableType.PROFILE);
		this.profile = profile;
	}
	
	public Profile getProfile() {
		return profile;
	}

}
