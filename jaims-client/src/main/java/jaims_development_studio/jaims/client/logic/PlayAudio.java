package jaims_development_studio.jaims.client.logic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.gui.VoiceMessage;

public class PlayAudio implements Runnable {

	private static final Logger	LOG	= LoggerFactory.getLogger(PlayAudio.class);
	String						file;
	Clip						clip;
	JLabel						actualTime;
	SimpleDateFormat			sdf;
	Date						d;
	JSlider						slider;
	JPanel						p;
	VoiceMessage				vm;

	/**
	 * The constructor of this class. Initialises only the fields, playback has to
	 * be started by running a thread.
	 *
	 * @param file
	 *            the absolute path to the audio file which has to be played.
	 * @param actualTime
	 *            JLabel showing the current clip position.
	 * @param slider
	 *            JSlider with length of clip in seconds as units.
	 * @param p
	 *            JPanel containing the slider.
	 * @param vm
	 *            JPanel containing the <code>VoiceMessage</code> Panel.
	 */
	public PlayAudio(String file, JLabel actualTime, JSlider slider, JPanel p, VoiceMessage vm) {

		this.file = file;
		this.actualTime = actualTime;
		this.slider = slider;
		this.p = p;
		this.vm = vm;
	}

	/**
	 * Starts the playback of the given audio file 'file' and runs a thread in which
	 * the sliders value is set to the clip's current audio position
	 */
	private void initPlayback() {

		sdf = new SimpleDateFormat("mm:ss");
		d = new Date();

		try {
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
			clip.open(ais);
			clip.addLineListener(new LineListener() {

				@Override
				public void update(LineEvent event) {

					if (event.getType() == Type.STOP) {
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							LOG.error("Interrupted Sleep", e);
						}
						// sets the slider's value to 0 after the clip has finished playing
						slider.setValue(0);
						d.setTime(0);
						actualTime.setText(sdf.format(d) + " / ");
						actualTime.repaint();

						clip.close();
						vm.setPaused(true);
					}

				}
			});
			clip.start();

			Thread thread = new Thread() {
				@Override
				public void run() {

					while (clip.isOpen()) {
						d.setTime(clip.getMicrosecondPosition() / 1000); // converts microseconds into milliseconds
						actualTime.setText(sdf.format(d) + " / ");
						actualTime.repaint();

						slider.setValue((int) (clip.getMicrosecondPosition() / 1000000)); // converts Microseconds into
																							// seconds
						slider.revalidate();
						p.repaint(); // vm.repaint();
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							LOG.error("Interrupted Sleep", e);
						}
					}

				}
			};
			thread.start();
		} catch (LineUnavailableException e) {
			LOG.error("Line is unavailable - probably used by other program/device", e);
		} catch (UnsupportedAudioFileException e) {
			LOG.error("Audio file is unsupported - propbably unsupported audio format", e);
		} catch (IOException e) {
			LOG.error("Error when reading file", e);
		}

	}

	public void pauseClip() {

		clip.stop();
	}

	public long getClipLength() {

		return clip.getMicrosecondLength();
	}

	@Override
	public void run() {

		initPlayback();
	}

}
