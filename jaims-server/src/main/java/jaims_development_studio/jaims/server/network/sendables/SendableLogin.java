package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SendableLogin")
@DiscriminatorValue(value = ESendableType.Values.LOGIN)
public class SendableLogin extends Sendable {

	private static final long serialVersionUID = 1L;
	@Column(name = "USERNAME", columnDefinition = "VARCHAR(256)")
	private final String		username;
	@Column(name = "PASSWORD", columnDefinition = "VARCHAR(256)")
	private final String		password;
	
	public SendableLogin(String username, String password) {
		super(ESendableType.LOGIN, 15);
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
