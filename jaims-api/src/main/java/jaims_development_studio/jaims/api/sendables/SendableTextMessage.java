package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "SendableTextMessage")
@DiscriminatorValue(value = ESendableType.Values.TEXT_MESSAGE)
public class SendableTextMessage extends SendableMessage {

	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "MESSAGE", columnDefinition = "LONGVARCHAR")
	private final String message;
	
	public SendableTextMessage() {
		this(null, null, null);
	}
	
	public SendableTextMessage(UUID sender, UUID recipient, String message) {
		super(sender, recipient, EMessageType.TEXT, 6);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
