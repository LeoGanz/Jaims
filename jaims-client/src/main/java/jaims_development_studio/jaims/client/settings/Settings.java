package jaims_development_studio.jaims.client.settings;

import java.awt.Color;
import java.io.Serializable;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

/**
 * Holds all the fields for customisable user settings.
 *
 * @author Bu88le
 *
 */
public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID			= 1176941652188927120L;
	private int					arcMessages					= 20;
	private int					arcContactImage				= 15;
	private int					arcProfileImage				= 15;

	// Font attributes for User-Message
	private String				ownFontName					= "Serif";
	private int					ownFontStyle				= 0;
	private int					ownFontSize					= 14;

	// Color Attributes for User-Message
	private int					colorOwnMessages			= new Color(191, 225, 14).getRGB();
	private int					colorOwnMessageBorder		= Color.GRAY.getRGB();
	private int					colorOwnMessageFont			= Color.BLACK.getRGB();

	// Font attributes for Contact-Message
	private String				contactFontName				= "Serif";
	private int					contactFontStyle			= 0;
	private int					contactFontSize				= 14;

	// Color attributes for Contact-Message
	private int					colorContactMessages		= new Color(191, 225, 14).getRGB();
	private int					colorContactMessageBorder	= Color.GRAY.getRGB();
	private int					colorContactMessageFont		= Color.BLACK.getRGB();

	// Input for Voice-Messages

	// Devices
	private String				inputMixerInfoName			= null;
	private String				outputMixerInfoName			= null;
	private float				inputGain					= 1.0F;
	private float				outputVolume				= 1.0F;
	private String				inputEncoding				= "PCM_SIGNED";
	private float				inputSampleRate				= 16000;
	private int					inputSampleSize				= 16;
	private int					inputChannels				= 2;
	private boolean				inputBigEndian				= false;
	private int					frameSize					= 4;
	private String				inputFileFormat				= "WAVE";

	// --------------------------------------------------------------
	// --------------GETTER AND SETTER-------------------------------
	// --------------------------------------------------------------

	public AudioFormat getAudioFormat() {

		switch (inputEncoding) {
		case "ALAW":
			return new AudioFormat(AudioFormat.Encoding.ALAW, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
		case "PCM_FLOAT":
			return new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
		case "PCM_SIGNED":
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
		case "PCM_UNSIGNED":
			return new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
		case "ULAW":
			return new AudioFormat(AudioFormat.Encoding.ULAW, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
		default:
			return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputSampleRate, inputSampleSize, inputChannels,
					frameSize, inputSampleRate, inputBigEndian);
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

		this.colorOwnMessages = rgb;
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

		this.inputMixerInfoName = inputMixerInfo;
	}

	public String getOutputMixerInfoName() {

		return outputMixerInfoName;
	}

	public void setOutputMixerInfo(String outputMixerInfo) {

		this.outputMixerInfoName = outputMixerInfo;
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

	public String getInputEncoding() {

		return inputEncoding;
	}

	public void setInputEncoding(String inputEncoding) {

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

		return inputBigEndian;
	}

	public void setInputBigEndian(boolean inputBigEndian) {

		this.inputBigEndian = inputBigEndian;
	}

	public int getFrameSize() {

		return frameSize;
	}

	public void setFrameSize(int frameSize) {

		this.frameSize = frameSize;
	}

	public AudioFileFormat.Type getInputFileFormat() {

		switch (inputFileFormat) {
		case "AIFC":
			return AudioFileFormat.Type.AIFC;
		case "AIFF":
			return AudioFileFormat.Type.AIFF;
		case "AU":
			return AudioFileFormat.Type.AU;
		case "SND":
			return AudioFileFormat.Type.SND;
		case "WAVE":
			return AudioFileFormat.Type.WAVE;
		default:
			return AudioFileFormat.Type.WAVE;
		}
	}

	public String getInputFileFormatString() {

		return inputFileFormat;
	}

	public void setInputFileFormat(String inputFileFormat) {

		this.inputFileFormat = inputFileFormat;
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

}
