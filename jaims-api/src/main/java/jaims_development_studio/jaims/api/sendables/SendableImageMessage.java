package jaims_development_studio.jaims.api.sendables;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity(name = "SendableImageMessage")
@DiscriminatorValue(value = ESendableType.Values.IMAGE_MESSAGE)
public class SendableImageMessage extends SendableMessage {
	
	private static final long	serialVersionUID	= 1L;

	@Column(name = "IMAGE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private final byte[]		image;
	
	public SendableImageMessage() {
		this(null, null, null);
	}
	
	public SendableImageMessage(UUID sender, UUID recipient, byte[] image) {
		super(sender, recipient, EMessageType.IMAGE);
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

}
