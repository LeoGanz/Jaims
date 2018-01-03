package jaims_development_studio.jaims.client.logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public class RecordAudio implements Runnable {

	TargetDataLine			line;
	static boolean			recording	= true;
	ByteArrayOutputStream	out			= new ByteArrayOutputStream();
	File					audioFile	= null;
	AudioInputStream		ais;

	/**
	 * Constructor of this class. Initialises only the fields, recording has to be
	 * started by running a thread.
	 *
	 * @param line
	 *            TargetDataLine from which audio data can be read.
	 */
	public RecordAudio(TargetDataLine line) {
		this.line = line;
	}

	/**
	 * Creates a new audio file with the current date as its name.
	 */
	private void createFile() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss"); // date example: 20171231_014523 (yyyy: year; MM:
																		// month; dd: day; HH: hour; mm: minute;
																		// ss: second
		Date dt = new Date(System.currentTimeMillis());
		audioFile = new File("C:/Jaims/VoiceMessages/VoiceMessage" + df.format(dt) + ".wav");

	}

	/**
	 * Creates a new AudioInputStream. Starts the line and writes recorded audio
	 * data to the file.
	 */
	private void record() {
		ais = new AudioInputStream(line);
		line.start();
		try {
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
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
		if (audioFile == null) {
			createFile();
		}
		record();

	}

}
