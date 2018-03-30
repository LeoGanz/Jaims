package jaims_development_studio.jaims.api.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;

/**
 * @author WilliGross
 */
public class Contacts extends UpdateTrackingUuidEntity {
	
	private static final long	serialVersionUID	= 1L;
	
	private final List<UUID>	contacts;

	public Contacts() {
		this(new UUID[] {});
	}
	
	public Contacts(UUID... contacts) {
		super(new Date(), null);
		
		this.contacts = new ArrayList<>(contacts.length);
		addContacts(contacts);
	}

	public List<UUID> getContacts() {
		return contacts;
	}
	
	public void addContacts(UUID... newContacts) {
		contacts.addAll(Arrays.asList(newContacts).stream().filter((c) -> c != null).collect(Collectors.toList()));
	}
	
}
