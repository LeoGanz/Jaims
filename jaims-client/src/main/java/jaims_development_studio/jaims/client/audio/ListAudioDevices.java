package jaims_development_studio.jaims.client.audio;

import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class ListAudioDevices {

	private ArrayList<Mixer.Info>	outputDevices	= new ArrayList<>();
	private ArrayList<Mixer.Info>	inputDevices	= new ArrayList<>();
	private GUIMain					guiMain;

	public ListAudioDevices(GUIMain guiMain) {

		this.guiMain = guiMain;
		listAudioDevices();
	}

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

	public ArrayList<Mixer.Info> getInputDevices() {

		return inputDevices;
	}

	public ArrayList<Mixer.Info> getOutputDevices() {

		return outputDevices;
	}

	public Mixer getSystemOutputMixer() {

		for (Mixer.Info outputDevice : outputDevices) {
			if (outputDevice.getName().equals(guiMain.getSettings().getOutputMixerInfoName()))
				return AudioSystem.getMixer(outputDevice);
		}

		return AudioSystem.getMixer(outputDevices.get(0));
	}

	public Mixer.Info getSystemInputMixerInfo() {

		for (Mixer.Info inputdevice : inputDevices) {
			if (inputdevice.getName().equals(guiMain.getSettings().getInputMixerInfoName()))
				return inputdevice;
		}

		return inputDevices.get(0);
	}

}
