package jaims_development_studio.jaims.client.logic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;

public class PlayAudio implements Runnable{

	String file;
	Clip clip;
	JLabel actualTime;
	SimpleDateFormat sdf;
	Date d;
	
	public PlayAudio(String file, JLabel actualTime) {
		this.file = file;
		this.actualTime = actualTime;
	}
	
	private void initPlayback( ) {
		sdf = new SimpleDateFormat("mm:ss");
		d = new Date();
		
		try {
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
			clip.open(ais);
			clip.start();
			Thread thread = new Thread() {
				@Override
				public void run() {
					d.setTime(clip.getMicrosecondPosition()*100);
					actualTime.setText(sdf.format(d) + " / ");
					actualTime.repaint();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			thread.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		initPlayback();
	}

}
