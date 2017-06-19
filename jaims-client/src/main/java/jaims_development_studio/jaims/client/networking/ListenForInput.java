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

import jaims_development_studio.jaims.client.gui.MainFrame;


public class ListenForInput implements Runnable {
	
	MainFrame				mf		= null;

	Socket					server	= null;
	//Sendable				basic	= null;
	ObjectInputStream		ois		= null;
	
	//SendableProfile			sp		= null;
	//SendableLogin			sl		= null;
	//SendableRegistration	sr		= null;
	//SendableDeleteAccount	sda		= null;
	//SendableMessage			sm		= null;
	//SendableException		se		= null;

	public ListenForInput(Socket server, MainFrame mf) {
		this.server = server;
		this.mf = mf;
	}
	
	@Override
	public void run() {
		while (true)
			if (server.isClosed() == false) {
				try {
					ois = new ObjectInputStream(server.getInputStream());
					//basic = (Sendable) ois.readObject();
					//switch (basic.getType()) {
					//	case REGISTRATION:
					//	case LOGIN:
					//		successfulLogin();
					//	case DELETE_ACCOUNT:
					//	case MESSAGE:
					//	case MESSAGE_RESPONSE:
					//	case EXCEPTION:
					//	case COMMAND:
					//	case OTHER:
					//	case PROFILE:
					//		sp = (SendableProfile) basic;

					//	default:
					//		break;
					//}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

	}
	
	private void successfulLogin() {
		Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
		DataLine.Info line = new DataLine.Info(Clip.class, null);

		
			try {
				Clip clip = (Clip) mixer.getLine(line);
				AudioInputStream ais = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResourceAsStream("jaims_development_studio.jaims.client/Audio/JAIMS_LOGIN.wav"));
				clip.open(ais);
				clip.start();
				do {
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(clip.isActive());
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	//	mf.loadingDB(SendableLogin.getUsername(), mf);
	}

}
