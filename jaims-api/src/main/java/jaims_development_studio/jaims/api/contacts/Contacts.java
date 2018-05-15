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
 * This class represents a list of an user's contacts. Only one Contacts object should exist per user. Contacts are
 * aware of the last time they were updated to allow for checks by the client.<br>
 * </br>
 * This type is an entity managed by Hibernate.
 *
 * @author WilliGross
 * @since 0.1.0
 */
@Entity(name = "Contacts")
@Table(name = "CONTACTS")
public class Contacts extends UpdateTrackingUuidEntity {
	
	private static final long	serialVersionUID	= 1L;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CONTACT_UUIDS", joinColumns = @JoinColumn(name = "CONTACTS_ID"))
	@Column(name = "ELEMENT", columnDefinition = "BINARY(16)")
	private final List<UUID>	contacts;

	/**
	 * This constructor can be used to create a new Contacts object that doesn't have any contacts yet. They can be
	 * added later through the method {@link #addContacts(UUID...)}. <br>
	 * </br>
	 * Furthermore a constructor without arguments is needed for Hibernate.
	 */
	public Contacts() {
		this(new UUID[] {});
	}
	
	/**
	 * This constructor can be used to create a new Contacts object that has some contacts right away. Contacts can be
	 * added later through the method {@link #addContacts(UUID...)} or removed via {@link #removeContacts(UUID...)}.
	 *
	 * @param contacts one or multiple {@link UUID}s that represent contacts
	 */
	public Contacts(UUID... contacts) {
		super(new Date(), null);
		
		this.contacts = new ArrayList<>(contacts.length);
		addContacts(contacts);
	}

	/**
	 * @return the list of contacts represented by {@link UUID}s
	 */
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
