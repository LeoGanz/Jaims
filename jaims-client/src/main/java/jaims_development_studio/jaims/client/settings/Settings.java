package jaims_development_studio.jaims.client.settings;

import java.awt.Color;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

/**
 * Holds all the fields for customisable user settings.
 *
 * @author Bu88le
 *
 */
public class Settings {

	public static int			arcMessages					= 20;
	public static int			arcContactImage				= 15;
	public static int			arcProfileImage				= 15;

	// Font attributes for User-Message
	public static String		ownFontName					= "Serif";
	public static int			ownFontStyle				= 0;
	public static int			ownFontSize					= 14;

	// Color Attributes for User-Message
	public static Color			colorOwnMessages			= new Color(191, 225, 14);
	public static Color			colorOwnMessageBorder		= Color.GRAY;
	public static Color			colorOwnMessageFont			= Color.BLACK;

	// Font attributes for Contact-Message
	public static String		contactFontName				= "Serif";
	public static int			contactFontStyle			= 0;
	public static int			contactFontSize				= 14;

	// Color attributes for Contact-Message
	public static Color			colorContactMessages		= new Color(191, 225, 14);
	public static Color			colorContactMessageBorder	= Color.GRAY;
	public static Color			colorContactMessageFont		= Color.BLACK;

	// Input for Voice-Messages
	public static AudioFormat	format						= new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F,
			16, 2, 4, 44100.0F, false);
	public static DataLine.Info	info						= new DataLine.Info(TargetDataLine.class, format);

	// Devices
	public static Mixer.Info	inputMixerInfo;
	public static Mixer.Info	outputMixerInfo;
	public static float			inputGain;
	public static float			outputVolume;
	public static Encoding		inputEncoding				= AudioFormat.Encoding.PCM_SIGNED;
	public static float			inputSampleRate				= 44100;
	public static int			inputSampleSize				= 16;
	public static int			inputChannels				= 2;
	public static boolean		inputBigEndian				= false;
	public static int			frameSize					= 4;

	public static AudioFormat getInputFormat() {

		return new AudioFormat(inputEncoding, inputSampleRate, inputSampleSize, inputChannels, frameSize,
				inputSampleRate, inputBigEndian);
	}

}
