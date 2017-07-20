package jaims_development_studio.jaims.server.network.sendables;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jaims_development_studio.jaims.server.network.sendables.Sendable.SendableType;

@Entity(name = "Sendable")
@Table(name = "SENDABLES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, columnDefinition = "VARCHAR(64) NOT NULL", length = 64)
@DiscriminatorValue(value = SendableType.Values.OTHER)
public class Sendable implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	@Column(name = "TYPE", columnDefinition = "VARCHAR(64) NOT NULL")
	@Enumerated(EnumType.STRING)
	private final SendableType	type;
	@Column(name = "TS_SENT", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestampSent;
	
	
	public Sendable(SendableType type) {
		this.type = type;
	}
	
	public SendableType getType() {
		return type;
	}

	public Date getTimestampSent() {
		return timestampSent;
	}
	
	public void setTimestampSent() {
		timestampSent = new Date();
	}

	public enum SendableType {
		REGISTRATION(Values.REGISTRATION),
		LOGIN(Values.LOGIN),
		DELETE_ACCOUNT(Values.DELETE_ACCOUNT),
		MESSAGE(Values.MESSAGE),
		MESSAGE_RESPONSE(Values.MESSAGE_RESPONSE),
		EXCEPTION(Values.EXCEPTION),
		COMMAND(Values.COMMAND),
		UUID(Values.UUID),
		PROFILE(Values.PROFILE),
		CONFIRMATION(Values.CONFIRMATION),
		OTHER(Values.OTHER); //maybe friend request? confirmation (after sth like account deletion, login ...)?
		
		private String value;
		
		private SendableType(String value) {
			if (!this.value.equals(value))
				throw new IllegalArgumentException("Incorrect use of SendableType!");
		}

		@SuppressWarnings("hiding")
		public static class Values {
			
			public static final String	REGISTRATION		= "REGISTRATION";
			public static final String	LOGIN				= "LOGIN";
			public static final String	DELETE_ACCOUNT		= "DELETE_ACCOUNT";
			public static final String	MESSAGE				= "MESSAGE";
			public static final String	MESSAGE_RESPONSE	= "MESSAGE_RESPONSE";
			public static final String	EXCEPTION			= "EXCEPTION";
			public static final String	COMMAND				= "COMMAND";
			public static final String	UUID				= "UUID";
			public static final String	PROFILE				= "PROFILE";
			public static final String	CONFIRMATION		= "CONFIRMATION";
			public static final String	OTHER				= "OTHER";
		}
	}
}
