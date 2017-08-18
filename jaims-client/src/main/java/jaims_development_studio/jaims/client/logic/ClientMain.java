package jaims_development_studio.jaims.client.logic;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.sql.Timestamp;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.database.DatabaseConnection;
import jaims_development_studio.jaims.client.gui.JaimsFrame;
import jaims_development_studio.jaims.client.gui.LoginPanel;
import jaims_development_studio.jaims.client.gui.PanelContactsAndChats;

public class ClientMain {

	private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);
	public static boolean confirmationRecieved = false;
	Point p = new Point(10, 5);
	JPanel panel = new JPanel();
	Timestamp ts;
	Rectangle r = new Rectangle(150, 150, 500, 500);
	
	Thread threadDatabaseManagement, threadPCC;
	DatabaseConnection dc;
	PanelContactsAndChats pcc;
	JaimsFrame jf;
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
	public ClientMain() {
		initProgram();
	}
	
	private void initProgram() {
		jf = new JaimsFrame();
		
		dc = new DatabaseConnection();
		dc.initConnection();
		dc.writeToDatabase();
		
		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			LOG.error("Sleep interrupted");
		}
		
		jf.initGUI();
		
		LoginPanel lp = new LoginPanel(jf, this);
		jf.getContentPane().add(lp, BorderLayout.CENTER);
		jf.getContentPane().revalidate();
		
	}
	
	public void startCreatingChatWindow(String username) {
		
		Thread thread = dc.readFromDatabase(username);
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		threadPCC = new Thread(pcc = new PanelContactsAndChats());
		threadPCC.start();
		
		try {
			threadPCC.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jf.getContentPane().removeAll();
		jf.getContentPane().add(pcc, BorderLayout.CENTER);
		jf.getContentPane().repaint();
	}
}