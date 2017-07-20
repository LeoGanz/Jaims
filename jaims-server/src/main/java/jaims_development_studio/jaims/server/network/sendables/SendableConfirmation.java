package jaims_development_studio.jaims.server.network.sendables;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "SendableConfirmation")
@DiscriminatorValue(value = SendableType.Values.CONFIRMATION)
public class SendableConfirmation {
	
	private ConfirmationType type;

	public SendableConfirmation() {

	}

	public SendableConfirmation(ConfirmationType type) {
		this.type = type;
	}

	public ConfirmationType getType() {
		return type;
	}

	public void setType(ConfirmationType type) {
		this.type = type;
	}

	public enum ConfirmationType {
		LOGIN_SUCCESSFUL(Values.LOGIN_SUCCESSFUL),
		REGISTRATION_SUCCESSFUL(Values.REGISTRATION_SUCCESSFUL);
		
		private String value;
		
		private ConfirmationType(String value) {
			if (!this.value.equals(value))
				throw new IllegalArgumentException("Incorrect use of ConfirmationType!");
		}

		@SuppressWarnings("hiding")
		public static class Values {
			public static final String REGISTRATION_SUCCESSFUL = "REGISTRATION_SUCCESSFUL";
			public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
		}
	}
}
