package jaims_development_studio.jaims.api.sendables;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import jaims_development_studio.jaims.api.user.User;

@Entity(name = "Sendable")
@Table(name = "SENDABLES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SENDABLE_TYPE", discriminatorType = DiscriminatorType.STRING,
columnDefinition = "VARCHAR(64)", length = 64)
@DiscriminatorValue(value = ESendableType.Values.OTHER)
public class Sendable implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "UUID", columnDefinition = "BINARY(16)")
	@Id
	private UUID				uuid;

	@Column(name = "SENDABLE_TYPE", columnDefinition = "VARCHAR(64)", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private final ESendableType	type;

	@Column(name = "PRIORITY")
	private int					priority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_UUID")
	private User				user;

	@Column(name = "TS_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampSent;

	@SuppressWarnings("unused")
	private Sendable() {
		this(null);
	}

	public Sendable(ESendableType type) {
		this.type = type;
	}

	public Sendable(ESendableType type, int priority) {
		this.type = type;
		if (priority < 0)
			throw new IllegalArgumentException("priority must be greater zero");
		this.priority = priority;
	}

	protected UUID getUuid() {
		return uuid;
	}

	public ESendableType getType() {
		return type;
	}

	public int getPriority() {
		return priority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTimestampSent() {
		return timestampSent;
	}

	public void setTimestampSent() {
		timestampSent = new Date();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Sendable other = (Sendable) o;
		return new EqualsBuilder().append(uuid, other.uuid).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(uuid).toHashCode();
	}

}
