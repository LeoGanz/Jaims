package jaims_development_studio.jaims.api.user;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.command.ICommandSender;
import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableMessage;

@Entity(name = "User")
@Table(name = "USERS")
public class User implements Serializable, ICommandSender {
	
	private static final long				serialVersionUID	= 1L;
	@Column(name = "ACCOUNT_UUID", columnDefinition = "BINARY(16)")
	@Id
	private UUID							uuid;
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	private Account							account;
	@Column(name = "LAST_SEEN", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date							lastSeen;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
	//	@JoinColumn(name = "SENDABLE_UUID", columnDefinition = "BINARY(16)")
	//	@Transient
	//	private final Collection<Sendable>	sendables			= new LinkedBlockingDeque<>();	//(with a deque) it will be possible to delete messages that aren't yet delivered
	private final List<Sendable>	sendables			= new LinkedList<>();

	public User() {

	}

	public User(Account account) {
		this.account = account;

		//		sendables.sort((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));
	}
	
	public synchronized void enqueueSendable(Sendable sendable) {
		sendables.add(sendable);
		//		((LinkedBlockingDeque<Sendable>) sendables).addLast(sendable);
		//		sendables.stream().filter(s -> s.getPriority() == sendable.getPriority()).reduce((first, second) -> second).get();
		sendable.setUser(this);
		notify();
	}

	public synchronized void enqueueAsFirstElement(Sendable sendable) {
		enqueueSendable(sendable);
		//		((LinkedBlockingDeque<Sendable>) sendables).addFirst(sendable);
		//		sendable.setUser(this);
		//		notify();
	}
	
	public synchronized Sendable takeSendable() {
		Sendable sendable = sendables.stream().sorted((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority())).findFirst().get();
		//			Sendable sendable = ((LinkedBlockingDeque<Sendable>) sendables).takeFirst();
		//		if (sendable != null) {
		sendables.remove(sendable);
		sendable.setUser(null);
		//		}
		return sendable;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public Date getLastSeen() {
		return lastSeen;
	}
	
	public void updateLastSeen() {
		lastSeen = new Date();
	}
	
	@Override
	public String toString() {
		return account.toStringName();
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
				.append(account, other.account)
				.append(lastSeen, other.lastSeen)
				//				.append(sendables, other.sendables) TODO uncomment when sendable saving is implmented
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(account)
				.append(lastSeen)
				//				.append(sendables)
				.toHashCode();
	}

	public boolean noSendableQueued() {
		return sendables.isEmpty();
	}
	
	@Override
	public String getName() {
		return account.getUsername();
	}
	
	@Override
	public void sendMessage(String msg) {
		SendableMessage sendableMessage = new SendableMessage(UUID.fromString("SERVER"), account.getUuid(), msg);
		enqueueSendable(sendableMessage);
	}
	
	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		// TODO different users can get different command levels
		return permLevel <= 1;
	}
	
	@Override
	public boolean sendCommandFeedback() {
		// TODO User setting
		return true; //for now
	}
	
}
