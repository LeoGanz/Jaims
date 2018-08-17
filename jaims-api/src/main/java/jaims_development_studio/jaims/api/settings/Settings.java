package jaims_development_studio.jaims.api.settings;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;

/**
 * Holds all the fields for customisable user settings.
 *
 * @author Bu88le
 * @author WilliGross
 *
 */
@Entity(name = "Settings")
@Table(name = "SETTINGS")
public class Settings extends UpdateTrackingUuidEntity {

	private static final long	serialVersionUID									= 1176941652188927120L;
	public static final int		RESIZE_ALONG_WIDTH									= 0;
	public static final int		RESIZE_ALONG_HEIGHT									= 1;
	public static final int		RESIZE_FILL_SCREEN									= 2;
	@Column(name = "ARC_MESSAGE", columnDefinition = "INTEGER")
	private int					arcMessages											= 20;
	@Column(name = "ARC_CONTACT_IMAGE", columnDefinition = "INTEGER")
	private int					arcContactImage										= 15;
	@Column(name = "ARC_PROFILE_IMMAGE", columnDefinition = "INTEGER")
	private int					arcProfileImage										= 15;
	@Column(name = "CHAT_BACKGROUND", columnDefinition = "INTEGER")
	private int					chatBackground										= Color.DARK_GRAY.getRGB();

	// Font attributes for User-Message
	@Column(name = "OWN_FONT_NAME", columnDefinition = "VARCHAR(128)")
	private String				ownFontName											= "Serif";
	@Column(name = "OWN_FONT_STYLE", columnDefinition = "INTEGER")
	private int					ownFontStyle										= 0;
	@Column(name = "OWN_FONT_SIZE", columnDefinition = "INTEGER")
	private int					ownFontSize											= 14;

	// Color Attributes for User-Message
	@Column(name = "COLOR_OWN_MESSAGE", columnDefinition = "INTEGER")
	private int					colorOwnMessages									= new Color(191, 225, 14).getRGB();
	@Column(name = "COLOR_OWN_MESSAGE_BORDER", columnDefinition = "INTEGER")
	private int					colorOwnMessageBorder								= Color.GRAY.getRGB();
	@Column(name = "COLOR_OWN_MESSAGE_FONT", columnDefinition = "INTEGER")
	private int					colorOwnMessageFont									= Color.BLACK.getRGB();

	// Font attributes for Contact-Message
	@Column(name = "CONTACT_FONT_NAME", columnDefinition = "VARCHAR(128)")
	private String				contactFontName										= "Serif";
	@Column(name = "CONTACT_FONT_STYLE", columnDefinition = "INTEGER")
	private int					contactFontStyle									= 0;
	@Column(name = "CONTACT_FONT_SIZE", columnDefinition = "INTEGER")
	private int					contactFontSize										= 14;

	// Color attributes for Contact-Message
	@Column(name = "COLOR_CONTACT_MESSAGE", columnDefinition = "INTEGER")
	private int					colorContactMessages								= new Color(255, 250, 240).getRGB();
	@Column(name = "COLOR_CONTACT_MESSAGE_BORDER", columnDefinition = "INTEGER")
	private int					colorContactMessageBorder							= Color.GRAY.getRGB();
	@Column(name = "COLOR_CONTACT_MESSAGE_FONT", columnDefinition = "INTEGER")
	private int					colorContactMessageFont								= Color.BLACK.getRGB();

	// Devices
	@Column(name = "INPUT_MIXER_INFO_NAME", columnDefinition = "VARCHAR(128)")
	private String				inputMixerInfoName									= null;
	@Column(name = "OUTPUT_MIXER_INFO_NAME", columnDefinition = "VARCHAR(128)")
	private String				outputMixerInfoName									= null;
	@Column(name = "INPUT_GAIN", columnDefinition = "DOUBLE")
	private float				inputGain											= 1.0F;
	@Column(name = "OUTPUT_VOLUME", columnDefinition = "DOUBLE")
	private float				outputVolume										= 1.0F;
	@Column(name = "INPUT_ENCODING", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private EInputEncodingType	inputEncoding										= EInputEncodingType.ENCODING_PCM_SIGNED;
	@Column(name = "INPUT_SAMPLE_RATE", columnDefinition = "DOUBLE")
	private float				inputSampleRate										= 16000;
	@Column(name = "INPUT_SAMPLE_SIZE", columnDefinition = "INTEGER")
	private int					inputSampleSize										= 16;
	@Column(name = "INPUT_CHANNELS", columnDefinition = "INTEGER")
	private int					inputChannels										= 2;
	@Column(name = "ENDIANNESS", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private EEndianness			endianness											= EEndianness.LITTLE_ENDIAN;
	@Column(name = "FRAME_SIZE", columnDefinition = "INTEGER")
	private int					frameSize											= 4;
	@Column(name = "INPUT_FORMAT_TYPE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private EFileFormatType		inputFormatType										= EFileFormatType.FORMAT_WAVE;
	@Column(name = "IMAGE_RESIZE_HINT", columnDefinition = "INTEGER")
	private int					imageResizeHint										= RESIZE_FILL_SCREEN;

	// Direct file exchange
	@Column(name = "PORT_DFE", columnDefinition = "INTEGER")
	private int					portSending											= 6050;
	@Column(name = "ALWAYS_SELECT_DESTINATION_WHEN_RECEIVING_MULTIPLE_FILES", columnDefinition = "BOOLEAN")
	private boolean				alwaysSelectDestinationWhenReceivingMultipleFiles	= false;
	@Column(name = "BUFFER_SIZE", columnDefinition = "INTEGER")
	private int bufferSize = 4096;

	public Settings() {

		this(null);
	}

	public Settings(UUID uuid) {

		super(new Date(), null);
		setUuid(uuid);
	}

	public Settings(Date lastUpdated, Account account, UUID uuid, int arcMessages, int arcContactImage,
			int arcProfileImage, String ownFontName, int ownFontStyle, int ownFontSize, int colorOwnMessages,
			int colorOwnMessageBorder, int colorOwnMessageFont, String contactFontName, int contactFontStyle,
			int contactFontSize, int colorContactMessages, int colorContactMessageBorder, int colorContactMessageFont,
			String inputMixerInfoName, String outputMixerInfoName, float inputGain, float outputVolume,
			EInputEncodingType inputEncoding, float inputSampleRate, int inputSampleSize, int inputChannels,
			EEndianness endianness, int frameSize, EFileFormatType inputFormatType, int imageResizeHint) {

		super(lastUpdated, account);
		setUuid(uuid);
		this.arcMessages = arcMessages;
		this.arcContactImage = arcContactImage;
		this.arcProfileImage = arcProfileImage;
		this.ownFontName = ownFontName;
		this.ownFontStyle = ownFontStyle;
		this.ownFontSize = ownFontSize;
		this.colorOwnMessages = colorOwnMessages;
		this.colorOwnMessageBorder = colorOwnMessageBorder;
		this.colorOwnMessageFont = colorOwnMessageFont;
		this.contactFontName = contactFontName;
		this.contactFontStyle = contactFontStyle;
		this.contactFontSize = contactFontSize;
		this.colorContactMessages = colorContactMessages;
		this.colorContactMessageBorder = colorContactMessageBorder;
		this.colorContactMessageFont = colorContactMessageFont;
		this.inputMixerInfoName = inputMixerInfoName;
		this.outputMixerInfoName = outputMixerInfoName;
		this.inputGain = inputGain;
		this.outputVolume = outputVolume;
		this.inputEncoding = inputEncoding;
		this.inputSampleRate = inputSampleRate;
		this.inputSampleSize = inputSampleSize;
		this.inputChannels = inputChannels;
		this.endianness = endianness;
		this.frameSize = frameSize;
		this.inputFormatType = inputFormatType;
		this.imageResizeHint = imageResizeHint;
	}

	@Override
	public Settings copyWithoutAccount() {

		return new Settings(getLastUpdated(), null, getUuid(), arcMessages, arcContactImage, arcProfileImage,
				ownFontName, ownFontStyle, ownFontSize, colorOwnMessages, colorOwnMessageBorder, colorOwnMessageFont,
				contactFontName, contactFontStyle, contactFontSize, colorContactMessages, colorContactMessageBorder,
				colorContactMessageFont, inputMixerInfoName, outputMixerInfoName, inputGain, outputVolume,
				inputEncoding, inputSampleRate, inputSampleSize, inputChannels, endianness, frameSize, inputFormatType,
				imageResizeHint);
	}

	@Override
	public String toString() {

		Account account = getAccount();

		if (account != null)
			return "Settings of account " + account.toStringName();
		return "Settings of account " + getUuid();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Settings other = (Settings) o;
		return new EqualsBuilder().append(getUuid(), other.getUuid()).append(getLastUpdated(), other.getLastUpdated())
				.append(arcMessages, other.arcMessages).append(arcContactImage, other.arcContactImage)
				.append(arcProfileImage, other.arcProfileImage).append(ownFontName, other.ownFontName)
				.append(ownFontStyle, other.ownFontStyle).append(ownFontSize, other.ownFontSize)
				.append(colorOwnMessages, other.colorOwnMessages)
				.append(colorOwnMessageBorder, other.colorOwnMessageBorder)
				.append(colorOwnMessageFont, other.colorOwnMessageFont).append(contactFontName, other.contactFontName)
				.append(contactFontStyle, other.contactFontStyle).append(contactFontSize, other.contactFontSize)
				.append(colorContactMessages, other.colorContactMessages)
				.append(colorContactMessageBorder, other.colorContactMessageBorder)
				.append(colorContactMessageFont, other.colorContactMessageFont)
				.append(inputMixerInfoName, other.inputMixerInfoName)
				.append(outputMixerInfoName, other.outputMixerInfoName).append(inputGain, other.inputGain)
				.append(outputVolume, other.outputVolume).append(inputEncoding, other.inputEncoding)
				.append(inputSampleRate, other.inputSampleRate).append(inputSampleSize, other.inputSampleSize)
				.append(inputChannels, other.inputChannels).append(endianness, other.endianness)
				.append(frameSize, other.frameSize).append(inputFormatType, other.inputFormatType)
				.append(imageResizeHint, other.imageResizeHint).isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(42, 321).append(getUuid()).append(getLastUpdated()).append(arcMessages)
				.append(arcContactImage).append(arcProfileImage).append(ownFontName).append(ownFontStyle)
				.append(ownFontSize).append(colorOwnMessages).append(colorOwnMessageBorder).append(colorOwnMessageFont)
				.append(contactFontName).append(contactFontStyle).append(contactFontSize).append(colorContactMessages)
				.append(colorContactMessageBorder).append(colorContactMessageFont).append(inputMixerInfoName)
				.append(outputMixerInfoName).append(inputGain).append(outputVolume).append(inputEncoding)
				.append(inputSampleRate).append(inputSampleSize).append(inputChannels).append(endianness)
				.append(frameSize).append(inputFormatType).append(imageResizeHint).toHashCode();
	}

	// --------------------------------------------------------------
	// --------------GETTER AND SETTER-------------------------------
	// --------------------------------------------------------------

	public AudioFormat getAudioFormat() {

		switch (inputEncoding) {
		case ENCODING_ALAW:
			return new AudioFormat(AudioFormat.Encoding.ALAW, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		case ENCODING_PCM_FLOAT:
			return new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		case ENCODING_PCM_SIGNED:
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		case ENCODING_PCM_UNSIGNED:
			return new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		case ENCODING_ULAW:
			return new AudioFormat(AudioFormat.Encoding.ULAW, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		default:
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, endianness.getValue());
		}
	}

	public String getOwnFontName() {

		return ownFontName;
	}

	public void setOwnFontName(String ownFontName) {

		this.ownFontName = ownFontName;
	}

	public int getOwnFontStyle() {

		return ownFontStyle;
	}

	public void setOwnFontStyle(int ownFontStyle) {

		this.ownFontStyle = ownFontStyle;
	}

	public int getOwnFontSize() {

		return ownFontSize;
	}

	public void setOwnFontSize(int ownFontSize) {

		this.ownFontSize = ownFontSize;
	}

	public int getColorOwnMessages() {

		return colorOwnMessages;
	}

	public void setColorOwnMessages(int rgb) {

		colorOwnMessages = rgb;
	}

	public int getColorOwnMessageBorder() {

		return colorOwnMessageBorder;
	}

	public void setColorOwnMessageBorder(int colorOwnMessageBorder) {

		this.colorOwnMessageBorder = colorOwnMessageBorder;
	}

	public int getColorOwnMessageFont() {

		return colorOwnMessageFont;
	}

	public void setColorOwnMessageFont(int colorOwnMessageFont) {

		this.colorOwnMessageFont = colorOwnMessageFont;
	}

	public String getContactFontName() {

		return contactFontName;
	}

	public void setContactFontName(String contactFontName) {

		this.contactFontName = contactFontName;
	}

	public int getContactFontStyle() {

		return contactFontStyle;
	}

	public void setContactFontStyle(int contactFontStyle) {

		this.contactFontStyle = contactFontStyle;
	}

	public int getContactFontSize() {

		return contactFontSize;
	}

	public void setContactFontSize(int contactFontSize) {

		this.contactFontSize = contactFontSize;
	}

	public int getColorContactMessages() {

		return colorContactMessages;
	}

	public void setColorContactMessages(int colorContactMessages) {

		this.colorContactMessages = colorContactMessages;
	}

	public int getColorContactMessageBorder() {

		return colorContactMessageBorder;
	}

	public void setColorContactMessageBorder(int colorContactMessageBorder) {

		this.colorContactMessageBorder = colorContactMessageBorder;
	}

	public int getColorContactMessageFont() {

		return colorContactMessageFont;
	}

	public void setColorContactMessageFont(int colorContactMessageFont) {

		this.colorContactMessageFont = colorContactMessageFont;
	}

	public String getInputMixerInfoName() {

		return inputMixerInfoName;
	}

	public void setInputMixerInfo(String inputMixerInfo) {

		inputMixerInfoName = inputMixerInfo;
	}

	public String getOutputMixerInfoName() {

		return outputMixerInfoName;
	}

	public void setOutputMixerInfo(String outputMixerInfo) {

		outputMixerInfoName = outputMixerInfo;
	}

	public float getInputGain() {

		return inputGain;
	}

	public void setInputGain(float inputGain) {

		this.inputGain = inputGain;
	}

	public float getOutputVolume() {

		return outputVolume;
	}

	public void setOutputVolume(float outputVolume) {

		this.outputVolume = outputVolume;
	}

	public EInputEncodingType getInputEncoding() {

		return inputEncoding;
	}

	public void setInputEncoding(EInputEncodingType inputEncoding) {

		this.inputEncoding = inputEncoding;
	}

	public float getInputSampleRate() {

		return inputSampleRate;
	}

	public void setInputSampleRate(float inputSampleRate) {

		this.inputSampleRate = inputSampleRate;
	}

	public int getInputSampleSize() {

		return inputSampleSize;
	}

	public void setInputSampleSize(int inputSampleSize) {

		this.inputSampleSize = inputSampleSize;
	}

	public int getInputChannels() {

		return inputChannels;
	}

	public void setInputChannels(int inputChannels) {

		this.inputChannels = inputChannels;
	}

	public boolean isInputBigEndian() {

		return endianness.getValue();
	}

	public void setInputBigEndian(EEndianness inputBigEndian) {

		endianness = inputBigEndian;
	}

	public int getFrameSize() {

		return frameSize;
	}

	public void setFrameSize(int frameSize) {

		this.frameSize = frameSize;
	}

	public AudioFileFormat.Type getInputFileFormat() {

		switch (inputFormatType) {
		case FORMAT_AIFC:
			return AudioFileFormat.Type.AIFC;
		case FORMAT_AIFF:
			return AudioFileFormat.Type.AIFF;
		case FORMAT_AU:
			return AudioFileFormat.Type.AU;
		case FORMAT_SND:
			return AudioFileFormat.Type.SND;
		case FORMAT_WAVE:
			return AudioFileFormat.Type.WAVE;
		default:
			return AudioFileFormat.Type.WAVE;
		}
	}

	public EFileFormatType getInputFileFormatString() {

		return inputFormatType;
	}

	public void setInputFileFormat(EFileFormatType inputFileFormat) {

		inputFormatType = inputFileFormat;
	}

	public int getArcMessages() {

		return arcMessages;
	}

	public int getArcContactImage() {

		return arcContactImage;
	}

	public int getArcProfileImage() {

		return arcProfileImage;
	}

	public int getChatBackground() {

		return chatBackground;
	}

	public void setChatBackground(int chatBackground) {

		this.chatBackground = chatBackground;
	}

	public Font getOwnFont() {

		return new Font(ownFontName, ownFontStyle, ownFontSize);
	}

	public Font getContactFont() {

		return new Font(contactFontName, contactFontStyle, contactFontSize);
	}

	public int getImageResizeHint() {

		return imageResizeHint;
	}

	public void setImageResizeHint(int imageResizeHint) {

		this.imageResizeHint = imageResizeHint;
	}

	public int getDFEPort() {

		return portSending;
	}


	public boolean selectDestinationForEachFile() {

		return alwaysSelectDestinationWhenReceivingMultipleFiles;
	}
	
	public void setBufferSize(int i) {
		bufferSize = i;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

}
