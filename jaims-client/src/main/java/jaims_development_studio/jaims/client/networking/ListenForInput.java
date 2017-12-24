package jaims_development_studio.jaims.client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.sendables.Sendable;
import jaims_development_studio.jaims.api.sendables.SendableConfirmation;
import jaims_development_studio.jaims.api.sendables.SendableException;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableUUID;

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
			while (so.isConnected()) {
				try {
					s = (Sendable) ois.readObject();
					handleSendable();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException npe) {
					LOG.error("Socket isn't initialised", npe);
				} catch (SocketException se) {
					LOG.error("Socket isn't connected", se);
				}catch (IOException ie) {
					LOG.error("IO Exception", ie);
				}
				
			}
		} catch(NullPointerException npe) {
			LOG.error("Socket isn't initialised!", npe);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Socket isn't connected", e);
		} 
		
		
		
	}
	
	private void handleSendable() {
		try {
			switch (s.getType().getValue()) {
			case "MESSAGE_RESPONSE":
				SendableMessageResponse smr = (SendableMessageResponse) s;
				LOG.info(smr.getType().toString());
				break;
			case "EXCEPTION":
				SendableException se = (SendableException) s;
				LOG.debug(se.getException().getClass().toString());
				LOG.debug(se.getException().getMessage());
				break;
			case "STORED_UUID":
				SendableUUID su = (SendableUUID) s;
				LOG.info(su.getType().toString());
				LOG.info(su.getUuid().toString());
				break;
			case "PROFILE":
				break;
			case "CONFIRMATION":
				SendableConfirmation sc = (SendableConfirmation) s;
				LOG.info(sc.getConfirmationType().toString());
				break;
			case "OTHER":
				break;
			default:
				break;
			
		}
		}catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
	@Override
	public void run() {
		readConnection();
		
	}

}
