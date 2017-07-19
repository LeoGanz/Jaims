package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.logic.ClientMain;
import jaims_development_studio.jaims.client.logic.DatabaseManagement;


public class ListenForInput implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ListenForInput.class);

	Socket					server	= null;
	//Sendable				basic	= null;
	ObjectInputStream		ois		= null;
	DatabaseManagement dm;
	
	//SendableProfile			sp		= null;
	//SendableLogin			sl		= null;
	//SendableRegistration	sr		= null;
	//SendableDeleteAccount	sda		= null;
	//SendableMessage			sm		= null;
	//SendableException		se		= null;

	public ListenForInput(Socket server, DatabaseManagement dm) {
		this.server = server;
		this.dm = dm;
	}
	
	@Override
	public void run() {
		ClientMain.confirmationRecieved = true;
		/*while (true)
			if (server.isClosed() == false) {
				try {
					ois = new ObjectInputStream(server.getInputStream());
					basic = (Sendable) ois.readObject();
					switch (basic.getType()) {
						case REGISTRATION:
						case LOGIN:
							successfulLogin();
						case DELETE_ACCOUNT:
						case MESSAGE:
							Thread thread = new Thread(){
								@Override
								public void run() {
									for (int i = 0; i< dm.getProfileList().size(); i++) {
										
									}
								}
							}; 
						case MESSAGE_RESPONSE:
						case EXCEPTION:
						case COMMAND:
						case OTHER:
						case PROFILE:
							sp = (SendableProfile) basic;

						default:
							break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}*/
			

	}
	
	private void successfulLogin() {
		//Loads the systems standard mixer(output)
		Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
		//Opens a line on the mixer
		DataLine.Info line = new DataLine.Info(Clip.class, null);
		try {
			Clip clip = (Clip) mixer.getLine(line);
			//loads the audio file as a stream which is then opened in the clip
			AudioInputStream ais = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResourceAsStream("jaims_development_studio.jaims.client/Audio/JAIMS_LOGIN.wav"));
			clip.open(ais);
			clip.start(); //start playing the clip
			do {
				try {
					//loops at least one time => clip can start playing
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					LOG.error("Sleep interrupted!", e);
				}
			}while(clip.isActive());
			
		} catch (UnsupportedAudioFileException e) {
			LOG.error("AudioFile is not supported!", e);
		} catch (IOException e) {
			LOG.error("Error while loading file!", e);
		} catch (LineUnavailableException e) {
			LOG.error("Line is unavailable!", e);
		}
	}

}
