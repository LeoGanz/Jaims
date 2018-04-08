package jaims_development_studio.jaims.api.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;

/**
 * @author WilliGross
 */
@Entity(name = "Contacts")
@Table(name = "CONTACTS")
public class Contacts extends UpdateTrackingUuidEntity {
	
	private static final long	serialVersionUID	= 1L;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CONTACT_UUIDS", joinColumns = @JoinColumn(name = "CONTACTS_ID"))
	@Column(name = "ELEMENT", columnDefinition = "BINARY(16)")
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
		updateLastUpdated();
	}

	public void removeContacts(UUID... contactsToRemove) {
		contacts.removeAll(Arrays.asList(contactsToRemove).stream().filter((c) -> c != null).collect(Collectors.toList()));
		updateLastUpdated();
	}
	
}
