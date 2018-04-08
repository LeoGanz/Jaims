package jaims_development_studio.jaims.server.settings;

import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.UpdateTrackingUuidEntityManager;


/**
 * @author WilliGross
 */
public class SettingsManager extends UpdateTrackingUuidEntityManager<Settings> {
	
	public SettingsManager(UserManager usermanager) {
		super(new SettingsDAO(), usermanager, EEntityType.SETTINGS);
	}

}
