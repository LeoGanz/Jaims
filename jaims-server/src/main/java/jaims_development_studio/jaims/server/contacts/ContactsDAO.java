package jaims_development_studio.jaims.server.contacts;

import jaims_development_studio.jaims.api.contacts.Contacts;
import jaims_development_studio.jaims.server.util.UpdateTrackingUUIDEntityDAO;

/**
 * This is the DAO (Data Access Object) for Contacts. It provides utility methods for accessing contacts in the
 * database.
 *
 * @author WilliGross
 */
public class ContactsDAO extends UpdateTrackingUUIDEntityDAO<Contacts> {

	public ContactsDAO() {
		super(Contacts.class);
	}

}
