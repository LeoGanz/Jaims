package jaims_development_studio.jaims.client.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {

	private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);
	public static boolean confirmationRecieved = false;
	
	Thread threadDatabaseManagement;
	DatabaseManagement dm;
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
	public ClientMain() {
		initProgram();
	}
	
	private void initProgram() {
		//Starts a thread which manages the Connection and Loading to and from the Database
		threadDatabaseManagement = new Thread(dm = new DatabaseManagement("Bu88le", ""));	
		threadDatabaseManagement.start();
		
		
		
		//Loops while the thread is alive OR the server hasn't responded
		while (threadDatabaseManagement.isAlive() == true || confirmationRecieved == false) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				LOG.error("Sleep interrupted!", e);
			}
		}
	}
}