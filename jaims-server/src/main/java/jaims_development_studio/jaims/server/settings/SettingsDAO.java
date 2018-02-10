package jaims_development_studio.jaims.server.settings;

import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.server.util.DAO;


/**
 * @author WilliGross
 */
public class SettingsDAO extends DAO<Settings> {

	public SettingsDAO() {
		super(Settings.class);
	}
	
}
