package jaims_development_studio.jaims.server.settings;

import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.server.user.UserManager;
import jaims_development_studio.jaims.server.util.UuidEntityManager;


/**
 * @author WilliGross
 */
public class SettingsManager extends UuidEntityManager<Settings> {
	
	public SettingsManager(UserManager usermanager) {
		super(new SettingsDAO(), usermanager, EEntityType.SETTINGS);
	}

}
