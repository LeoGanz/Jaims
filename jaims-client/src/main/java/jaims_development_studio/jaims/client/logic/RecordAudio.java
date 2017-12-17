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

public class RecordAudio implements Runnable{

	TargetDataLine line;
	static boolean recording = true;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	File f = null;
	AudioInputStream ais;
	
	public RecordAudio(TargetDataLine line) {
		this.line = line;
	}
	
	private void createFile() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date dt = new Date(System.currentTimeMillis());
		f = new File("C:/Users/Programming/Documents/VoiceMessage"+df.format(dt) + ".wav");
		ais = new AudioInputStream(line);
		
	}
	
	private void record() {
		line.start();
		try {
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopRecording() {
		recording = false;
		line.stop();
		
		f.delete();
	}
	
	public void pauseRecording() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		line.stop();
	}
	
	public void restartRecording() {
		this.run();
	}
	
	public void sendRecording() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		line.stop();
	}
	
	public String getPath() {
		return f.getAbsolutePath();
	}
	
	@Override
	public void run() {
		if (f == null) {
			createFile();
		}
		record();
		
	}

}
