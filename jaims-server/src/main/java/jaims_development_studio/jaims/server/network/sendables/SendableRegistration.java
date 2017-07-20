package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "SendableRegistration")
@DiscriminatorValue(value = SendableType.Values.REGISTRATION)
public class SendableRegistration extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	private final String		username;
	private final String		password;
	private final String		email;
	
	public SendableRegistration(String username, String password, String email) {
		super(SendableType.REGISTRATION);
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
}
