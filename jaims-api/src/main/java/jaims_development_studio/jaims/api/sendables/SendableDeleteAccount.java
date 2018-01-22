package jaims_development_studio.jaims.api.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * This is basically a marker. Account deletion can only be performed when a user is logged in
 *
 * @deprecated Use {@link SendableRequest} with RequestType DELETE_ACCOUNT instead
 * @see SendableRequest
 * @see ERequestType
 */
@Deprecated
@Entity(name = "SendableDeleteAccount")
@DiscriminatorValue(value = ESendableType.Values.DELETE_ACCOUNT)
public class SendableDeleteAccount extends Sendable {

	private static final long serialVersionUID = 1L;
	
	public SendableDeleteAccount() {
		super(ESendableType.DELETE_ACCOUNT, 10);
	}
	
}
