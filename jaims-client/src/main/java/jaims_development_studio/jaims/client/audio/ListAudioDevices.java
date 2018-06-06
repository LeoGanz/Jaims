package jaims_development_studio.jaims.client.audio;

import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import jaims_development_studio.jaims.client.gui.GUIMain;
import jaims_development_studio.jaims.client.logic.ClientMain;

/**
 * This class is responsible for listing, holding and returning all information
 * about the system's audio devices, such as input as well as output devices.
 * For further information on how the program decides between input and output
 * devices, see the following methods:
 * <ul>
 * <li>{@link #listAudioDevices()}</li>
 * <li>{@link #getOutputDevices()}</li>
 * <li>{@link #getInputDevices()}</li>
 * </ul>
 * 
 * @author Bu88le
 * 
 * @since v0.1.0
 *
 */
public class ListAudioDevices {

	private ArrayList<Mixer.Info>	outputDevices	= new ArrayList<>();
	private ArrayList<Mixer.Info>	inputDevices	= new ArrayList<>();
	private GUIMain					guiMain;

	public ListAudioDevices(GUIMain guiMain) {

		this.guiMain = guiMain;
		listAudioDevices();
	}

	/**
	 * This method loops through all available audio devices the operating system
	 * offers the program and then assigns the mixer to one of the array lists
	 * containing input and output devices. If the mixer neither fits the criteria
	 * for an output device nor those of an input device it simply will be skipped
	 * an neglected. The process of deciding whether the at hand mixer is a input or
	 * output device happens based on whether the mixer supports a certain
	 * <code>DataLine</code>, in this case with <code>Clip.class</code> as the class
	 * of the <code>DataLine</code>. Because the <code>Clip</code> interface
	 * represents a data line used for playing back audio, the mixer's method
	 * {@link Mixer#isLineSupported(javax.sound.sampled.Line.Info)} will return only
	 * true if the mixer can be used for outputting sound which at the same time
	 * excludes recording audio. That means that the mixer can be added to the array
	 * list for output devices without any doubt. If the
	 * {@link Mixer#isLineSupported(javax.sound.sampled.Line.Info)} method returns
	 * false the mixer can in reverse conclusion be added to the array list holding
	 * all mixers used for input devices.
	 * 
	 * @see Mixer
	 * @see Mixer.Info
	 * @see DataLine
	 * @see Clip
	 * @see TargetDataLine
	 */
	private void listAudioDevices() {

		outputDevices.clear();
		inputDevices.clear();

		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		DataLine.Info dli2 = new DataLine.Info(Clip.class, null);
		DataLine.Info dli3 = new DataLine.Info(TargetDataLine.class, null);
		for (Mixer.Info mi : mixerInfo) {
			Mixer mixer = AudioSystem.getMixer(mi);
			if (mixer.isLineSupported(dli2)) {
				outputDevices.add(mi);
			} else if (mixer.isLineSupported(dli3)) {
				inputDevices.add(mi);
			}
		}
	}

	/**
	 * 
	 * @return ArrayList filled with <code>Mixer.Info</code> objects for all input
	 *         devices
	 * 
	 * @see Mixer.Info
	 * @see ArrayList
	 */
	public ArrayList<Mixer.Info> getInputDevices() {

		return inputDevices;
	}

	/**
	 * 
	 * @return ArrayList filled with <code>Mixer.Info</code> objects for all output
	 *         devices
	 * 
	 * @see Mixer.Info
	 * @see ArrayList
	 */
	public ArrayList<Mixer.Info> getOutputDevices() {

		return outputDevices;
	}

	/**
	 * This method is called when the program needs to get its output device. This
	 * happens by looping through all available output devices and checking whether
	 * the output devices contains the user's preferred one which is stored in the
	 * settings object accessible via the <code>ClientMain</code> object. If there
	 * is a accordance between the settings and the output devices the according
	 * <code>Mixer</code> object is returned, otherwise the system's standard output
	 * device will be returned.
	 * 
	 * @return the programs output device
	 * 
	 * @see Settings
	 * @see ClientMain
	 * @see Mixer
	 * @see Mixer.Info
	 * @see ArrayList
	 */
	public Mixer getSystemOutputMixer() {

		for (Mixer.Info outputDevice : outputDevices) {
			if (outputDevice.getName().equals(guiMain.getSettings().getOutputMixerInfoName()))
				return AudioSystem.getMixer(outputDevice);
		}

		return AudioSystem.getMixer(outputDevices.get(0));
	}

	/**
	 * This method is called when the program needs to get its input device. This
	 * happens by looping through all available input devices and checking whether
	 * the input devices contain the user's preferred one which is stored in the
	 * settings object accessible via the <code>ClientMain</code> object. If there
	 * is a accordance between the settings and the input devices the according
	 * <code>Mixer</code> object is returned, otherwise the system's standard input
	 * device will be returned.
	 * 
	 * @return the programs input device
	 * 
	 * @see Settings
	 * @see ClientMain
	 * @see Mixer
	 * @see Mixer.Info
	 * @see ArrayList
	 */
	public Mixer.Info getSystemInputMixerInfo() {

		for (Mixer.Info inputdevice : inputDevices) {
			if (inputdevice.getName().equals(guiMain.getSettings().getInputMixerInfoName()))
				return inputdevice;
		}

		return inputDevices.get(0);
	}

}
