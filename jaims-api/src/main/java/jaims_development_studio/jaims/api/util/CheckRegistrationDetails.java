package jaims_development_studio.jaims.api.util;

/**
 * <code>CheckRegistrationDetails</code> allows both the client and the server
 * to check a users username,password and email on whether they fit the agreed
 * upon criteria. In order to not have to implement these methods in both
 * projects they were added as public static methods to this class in order to
 * be accessible from the whole project without any problem. <br>
 * All methods receive a string, compare it to a <code>RegEx</code> expression
 * and return a boolean. For further information see the methods themselves.
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class CheckRegistrationDetails {

	public static Boolean verifyUsername(String username) {

		return !username.matches("[A-Za-z1-9_.]*");
	}

	public static Boolean verifyPassword(String password) {

		return !password.matches("[A-Za-z1-9!\\?@\\(\\)\\{\\}\\[\\]\\\\/|<>=~$€%&#\\*-\\+.:,;'\"_]*");
	}

	public static Boolean verifyEmail(String email) {

		return !email
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
}
