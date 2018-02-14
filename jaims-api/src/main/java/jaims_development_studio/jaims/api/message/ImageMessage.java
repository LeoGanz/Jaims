package jaims_development_studio.jaims.api.message;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

/**
 * @author WilliGross
 */
@Entity(name = "ImageMessage")
@DiscriminatorValue(value = EMessageType.Values.IMAGE)
public class ImageMessage extends TextMessage {
	
	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "IMAGE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private final byte[]		image;

	public ImageMessage() {
		this(null, null, null, null);
	}

	public ImageMessage(UUID sender, UUID recipient, byte[] image) {
		this(sender, recipient, null, image);
	}
	
	public ImageMessage(UUID sender, UUID recipient, String message, byte[] image) {
		super(sender, recipient, message, EMessageType.IMAGE);
		this.image = image;
	}
	
	public byte[] getImage() {
		return image;
	}
}
