package jaims_development_studio.jaims.server.profile;

import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.server.util.DAO;

/**
 * This is the DAO (Data Access Object) for Profiles. It provides utility methods for accessing profiles in the
 * database.
 *
 * @author WilliGross
 */
public class ProfileDAO extends DAO<Profile> {
	
	public ProfileDAO() {
		super(Profile.class);
	}
	
}
