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
@Entity(name = "VoiceMessage")
@DiscriminatorValue(value = EMessageType.Values.VOICE)
public class VoiceMessage extends Message {
	
	private static final long	serialVersionUID	= 1L;

	@Column(name = "VOICE_MESSAGE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private final byte[]		voiceMessage;

	public VoiceMessage() {
		this(null, null, null);
	}
	
	public VoiceMessage(UUID sender, UUID recipient, byte[] voiceMessage) {
		super(sender, recipient, EMessageType.VOICE);
		this.voiceMessage = voiceMessage;
	}

	public byte[] getVoiceMessage() {
		return voiceMessage;
	}
}
