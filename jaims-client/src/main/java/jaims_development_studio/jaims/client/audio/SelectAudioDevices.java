package jaims_development_studio.jaims.client.audio;

import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import jaims_development_studio.jaims.client.logic.ClientMain;

public class SelectAudioDevices {

	private ArrayList<Mixer.Info>	outputDevices	= new ArrayList<>();
	private ArrayList<Mixer.Info>	inputDevices	= new ArrayList<>();
	private ClientMain				cm;

	public SelectAudioDevices(ClientMain cm) {

		this.cm = cm;
		searchForDevices();
	}

	private void searchForDevices() {

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

	public ArrayList<Mixer.Info> getOutputDevices() {

		return outputDevices;
	}

	public ArrayList<Mixer.Info> getInputDevices() {

		return inputDevices;
	}

}
