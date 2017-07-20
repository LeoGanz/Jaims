package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

/**
 * This is basically a marker.
 * Account deletion can only be performed when a user is logged in
 */
@Entity(name = "SendableDeleteAccount")
@DiscriminatorValue(value = SendableType.Values.DELETE_ACCOUNT)
public class SendableDeleteAccount extends Sendable {
	
	private static final long	serialVersionUID	= 1L;

	public SendableDeleteAccount() {
		super(SendableType.DELETE_ACCOUNT);
	}

}
