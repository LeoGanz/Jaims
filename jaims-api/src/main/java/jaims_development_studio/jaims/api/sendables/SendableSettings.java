package jaims_development_studio.jaims.api.sendables;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jaims_development_studio.jaims.api.settings.Settings;

/**
 * @author WilliGross
 */
@Entity(name = "SendableSettings")
@DiscriminatorValue(value = ESendableType.Values.SETTINGS)
public class SendableSettings extends Sendable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "SETTINGS", columnDefinition = "BINARY(16)")
	private Settings			settings;

	
	@SuppressWarnings("unused")
	private SendableSettings() {
		this(null);
	}
	
	public SendableSettings(Settings settings) {
		super(ESendableType.SETTINGS);
		if (settings != null)
			this.settings = settings.copyWithoutAccount();
	}
	
	public Settings getSettings() {
		return settings;
	}
	
}
