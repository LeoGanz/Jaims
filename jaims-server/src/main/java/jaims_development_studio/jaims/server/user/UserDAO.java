package jaims_development_studio.jaims.server.user;

import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.server.util.DAO;

public class UserDAO extends DAO<User> {
	
	public UserDAO() {
		super(User.class);
	}
	
}
