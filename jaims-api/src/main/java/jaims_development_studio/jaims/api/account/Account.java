package jaims_development_studio.jaims.api.account;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Account")
@Table(name = "ACCOUNTS")
public class Account implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "UUID", columnDefinition = "BINARY(16)")
	@Id
	private UUID				uuid;
	@Column(name = "USERNAME", columnDefinition = "VARCHAR(256) NOT NULL UNIQUE")
	private String				username;
	@Column(name = "PASSWORD", columnDefinition = "VARCHAR(256) NOT NULL")
	private String				password;
	@Column(name = "EMAIL", columnDefinition = "VARCHAR(256) NOT NULL")
	private String				email;
	@Column(name = "REGISTRATION_DATE", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationDate;
	
	public Account() {

	}

	public Account(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}


	public String getPassword() {
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}
	

	public Date getRegistrationDate() {
		return registrationDate;
	}
	

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	

	public void setEmail(String email) {
		this.email = email;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}
	
	public boolean validatePassword(@SuppressWarnings("hiding") String password) {
		return this.password.equals(password);
	}
	
	@Override
	public String toString() {
		return toStringUuid();
	}

	public String toStringName() {
		return username;
	}
	
	public String toStringUuid() {
		return uuid.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Account other = (Account) o;
		return new EqualsBuilder()
				.append(uuid, other.uuid)
				.append(username, other.username)
				.append(password, other.password)
				.append(email, other.email)
				.append(registrationDate, other.registrationDate)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(uuid)
				.append(username)
				.append(password)
				.append(email)
				.append(registrationDate)
				.toHashCode();
	}
	
}
