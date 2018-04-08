package jaims_development_studio.jaims.api.user;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jaims_development_studio.jaims.api.IServer;
import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.command.CommandBase;
import jaims_development_studio.jaims.api.command.ICommandSender;
import jaims_development_studio.jaims.api.message.TextMessage;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.util.AccountUuidEntity;

/**
 * @author WilliGross
 */
@Entity(name = "User")
@Table(name = "USERS")
public class User extends AccountUuidEntity implements ICommandSender {

	private static final long		serialVersionUID	= 1L;

	@Transient
	private IServer					server;

	@Column(name = "LAST_SEEN", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date					lastSeen;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
	private final List<Sendable>	sendables			= new LinkedList<>();
	
	//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
	//	private final Contacts				contacts;

	public User() {
		this(null);
	}

	public User(Account account) {
		this(null, account);
	}

	public User(IServer server, Account account) {
		super(account);
		this.server = server;
		//		contacts = new Contacts();
		//		contacts.setAccount(account);
	}

	public synchronized void enqueueSendable(Sendable sendable) {
		sendables.add(sendable);
		sendable.setUser(this);
		notifyAll();
	}

	public synchronized Sendable takeSendable() {
		Sendable sendable = sendables.stream().sorted(Comparator.comparingInt(Sendable::getPriority).reversed()).findFirst().get();
		if (sendable != null) {
			sendables.remove(sendable);
			sendable.setUser(null);
		}
		return sendable;
	}

	public synchronized int numberOfQueuedSendables() {
		return sendables.size();
	}

	public void setServer(IServer server) {
		this.server = server;
	}
	

	public Date getLastSeen() {
		return lastSeen;
	}

	public void updateLastSeen() {
		lastSeen = new Date();
	}

	@Override
	public String toString() {
		return getAccount().toStringName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		User other = (User) o;
		return new EqualsBuilder()
				.append(getUuid(), other.getUuid())
				.append(lastSeen, other.lastSeen)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(32, 645)
				.append(getUuid())
				.append(lastSeen)
				.toHashCode();
	}

	public synchronized boolean noSendableQueued() {
		return sendables.isEmpty();
	}

	//	/**
	//	 * @return the contacts
	//	 */
	//	public Contacts getContacts() {
	//		return contacts;
	//	}
	//
	//	public void addContacts(UUID... uuids) {
	//		contacts.addContacts(uuids);
	//	}

	@Override
	public String getName() {
		return getAccount().getUsername();
	}

	@Override
	public void sendMessage(String msg) {
		synchronized (this) {
			UUID senderUUID = null;
			if (server != null)
				senderUUID = server.getServerUUID();
			TextMessage message = new TextMessage(senderUUID, getAccount().getUuid(), msg);
			SendableMessage sendableMessage = new SendableMessage(message);
			enqueueSendable(sendableMessage);
			notifyAll();
		}
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		// TODO different users can get different command levels
		return permLevel <= CommandBase.PERMISSION_LEVEL_UTILITY;
	}

	@Override
	public boolean sendCommandFeedback() {
		// TODO User setting
		return true; //for now
	}

}
