package jaims_development_studio.jaims.server.user;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jaims_development_studio.jaims.server.account.Account;
import jaims_development_studio.jaims.server.command.ICommandSender;
import jaims_development_studio.jaims.server.network.sendables.Sendable;
import jaims_development_studio.jaims.server.network.sendables.SendableMessage;

@Entity(name = "User")
@Table(name = "USERS")
public class User implements Serializable, ICommandSender {
	
	private static final long				serialVersionUID	= 1L;
	@OneToOne
	@PrimaryKeyJoinColumn(name = "ACCOUNT_UUID", columnDefinition = "BINARY(16)")
	//	@JoinColumn(name = "ACCOUNT_UUID", columnDefinition = "BINARY(16)")
	private final Account					account;
	@Column(name = "LAST_SEEN", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date							lastSeen;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "SENDABLE_UUID", columnDefinition = "BINARY(16)")
	private final BlockingDeque<Sendable>	sendables			= new LinkedBlockingDeque<>();	//(with a deque) it will be possible to delete messages that aren't yet delivered
	
	public User(Account account) {
		this.account = account;
	}
	
	public synchronized void enqueueSendable(Sendable sendable) {
		sendables.addLast(sendable);
		notify();
	}

	public synchronized void enqueueAsFirstElement(Sendable sendable) {
		sendables.addFirst(sendable);
		notify();
	}
	
	public synchronized Sendable getSendable() {
		try {
			return sendables.takeFirst();
		} catch (@SuppressWarnings("unused") InterruptedException e) {
			return null;
		}
	}
	
	public Account getAccount() {
		return account;
	}
	
	public Date getLastSeen() {
		return lastSeen;
	}
	
	public void setLastSeen() {
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
				.append(sendables, other.sendables)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
				.append(account)
				.append(lastSeen)
				.append(sendables)
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
