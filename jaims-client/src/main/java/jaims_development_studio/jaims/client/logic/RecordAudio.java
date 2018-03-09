package jaims_development_studio.jaims.client.logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import jaims_development_studio.jaims.client.gui.GUIMain;

public class RecordAudio implements Runnable {

	TargetDataLine			line;
	static boolean			recording	= true;
	ByteArrayOutputStream	out			= new ByteArrayOutputStream();
	File					audioFile	= null;
	AudioInputStream		ais;
	private GUIMain			guiMain;

	/**
	 * Constructor of this class. Initialises only the fields, recording has to be
	 * started by running a thread.
	 *
	 * @param line
	 *            TargetDataLine from which audio data can be read.
	 * @param cm
	 *            A ClientMain object the class need to access the settings.
	 * 
	 */
	public RecordAudio(GUIMain guiMain) {

		try {
			line = AudioSystem.getTargetDataLine(guiMain.getSettings().getAudioFormat(),
					guiMain.getListAudioDevices().getSystemInputMixerInfo());
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.guiMain = guiMain;
	}

	/**
	 * Creates a new audio file with the current date as its name.
	 */
	private void createFile() {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss"); // date example: 20171231_014523 (yyyy: year; MM:
																		// month; dd: day; HH: hour; mm: minute;
																		// ss: second
		Date dt = new Date(System.currentTimeMillis());
		File f = new File(System.getProperty("user.home").replace("\\", "/") + "/Jaims/" + guiMain.getUsername()
				+ "/VoiceMessages");
		if (f.exists())
			audioFile = new File(System.getProperty("user.home").replace("\\", "/") + "/Jaims/" + guiMain.getUsername()
					+ "/VoiceMessages/" + guiMain.getUserContact().getContactNickname() + "_JaimsVm_" + df.format(dt)
					+ "." + guiMain.getSettings().getInputFileFormat().getExtension());
		else {
			f.mkdirs();
			audioFile = new File(System.getProperty("user.home").replace("\\", "/") + "/Jaims/" + guiMain.getUsername()
					+ "/VoiceMessages/" + guiMain.getUserContact().getContactNickname() + "_JaimsVm_" + df.format(dt)
					+ "." + guiMain.getSettings().getInputFileFormat().getExtension());
		}

	}

	/**
	 * Creates a new AudioInputStream. Starts the line and writes recorded audio
	 * data to the file.
	 */
	private void record() {

		ais = new AudioInputStream(line);
		line.start();
		try {
			AudioSystem.write(ais, guiMain.getSettings().getInputFileFormat(), audioFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Stops the recording and deletes the already created file.
	 */
	public void stopRecording() {

		recording = false;
		line.stop();

		audioFile.delete();
	}

	/**
	 * Stops line after sleeping for 200 ms to also record the audio data still in
	 * the line. Keeps the file to be able to restart recording.
	 */
	public void pauseRecording() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		line.stop();
	}

	/**
	 * Restarts the recording.
	 */
	public void restartRecording() {

		this.run();
	}

	/**
	 * Stops the recording after the send button in <code>RecordingFrame</code> is
	 * pressed. Sleeps for 200 ms before stopping line in order to record the audio
	 * data still in the line.
	 */
	public void sendRecording() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		line.stop();
	}

	/**
	 *
	 * @return the absolut path of the audio file
	 */
	public String getPath() {

		return audioFile.getAbsolutePath();
	}

	/**
	 * Creates a audioFile if it is not existing. <br>
	 * Starts the recording.
	 */
	@Override
	public void run() {

		createFile();
		record();

	}

}
