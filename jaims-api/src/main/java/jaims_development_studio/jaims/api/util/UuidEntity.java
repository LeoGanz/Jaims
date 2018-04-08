package jaims_development_studio.jaims.api.util;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import jaims_development_studio.jaims.api.contacts.Contacts;
import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.EEntityType;
import jaims_development_studio.jaims.api.settings.Settings;

@MappedSuperclass
public class UuidEntity implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "UUID", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Id
	private UUID uuid;
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public EEntityType getEntityType() {
		if (this instanceof Profile)
			return EEntityType.PROFILE;
		if (this instanceof Settings)
			return EEntityType.SETTINGS;
		if (this instanceof Contacts)
			return EEntityType.CONTACTS;
		if (this instanceof Message)
			return EEntityType.MESSAGE;
		return EEntityType.OTHER;
	}
}
