package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SendableRegistration")
@DiscriminatorValue(value = ESendableType.Values.REGISTRATION)
public class SendableRegistration extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	@Column(name = "USERNAME", columnDefinition = "VARCHAR(256)")
	private final String		username;
	@Column(name = "PASSWORD", columnDefinition = "VARCHAR(256)")
	private final String		password;
	@Column(name = "EMAIL", columnDefinition = "VARCHAR(256)")
	private final String		email;
	
	public SendableRegistration(String username, String password, String email) {
		super(ESendableType.REGISTRATION, 20);
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
