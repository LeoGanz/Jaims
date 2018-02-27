package jaims_development_studio.jaims.api.message;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author WilliGross
 */
@Entity(name = "TextMessage")
@DiscriminatorValue(value = EMessageType.Values.TEXT)
public class TextMessage extends Message {
	
	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "MESSAGE", columnDefinition = "LONGVARCHAR")
	private final String		message;
	
	@SuppressWarnings("unused")
	private TextMessage() {
		this(null, null, null);
	}
	
	public TextMessage(UUID sender, UUID recipient, String message) {
		this(sender, recipient, message, EMessageType.TEXT);
	}
	
	protected TextMessage(UUID sender, UUID recipient, String message, EMessageType messageType) {
		super(sender, recipient, messageType);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
