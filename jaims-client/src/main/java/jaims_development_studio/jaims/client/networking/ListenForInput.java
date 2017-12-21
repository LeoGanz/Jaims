package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;

public class ListenForInput implements Runnable{
	
	private static final Logger LOG = LoggerFactory.getLogger(ListenForInput.class);
	
	Socket so;
	ObjectInputStream ois;
	Sendable s;
	
	public ListenForInput (Socket so) {
		this.so = so;
	}

	private void readConnection() {
		try {
			ois = new ObjectInputStream(so.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true) {
			try {
				s = (Sendable) ois.readObject();
				handleSendable();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void handleSendable() {
		switch (s.getType().getValue()) {
			case "MESSAGE_RESPONSE":
				SendableMessageResponse smr = (SendableMessageResponse) s;
				LOG.info(smr.getType().toString());
			case "EXCEPTION":
				SendableException se = (SendableException) s;
				LOG.debug(se.getException().getClass().toString());
				LOG.debug(se.getException().getMessage());
			case "STORED_UUID":
				
			case "PROFILE":
				
			case "CONFIRMATION":
				
			case "OTHER":
				
			default:
				break;
			
		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Ready");
		readConnection();
		
	}

}
