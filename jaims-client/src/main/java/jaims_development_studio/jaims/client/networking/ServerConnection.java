package jaims_development_studio.jaims.client.networking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jaims_development_studio.jaims.client.gui.MainFrame;

//import serverConnection.ListenForInput;

public class ServerConnection implements Runnable{
	
	private Socket server;
	private File errorFile;
	private BufferedWriter bw;
	private DateFormat df;
	private Date today;
	private String reportDate;
	private ObjectOutputStream oos;
	private MainFrame mf;
	private ListenForInput lfi;
	private Thread listenForInput;
	
	public ServerConnection(MainFrame mf) {
		this.mf = mf;
	}
	
	@Override
	public void run() {
		initConnection();
		
	}
	
	
	private void initConnection() {
		try {
			System.out.println("hi");
			server = new Socket("localhost", 6000);
			System.out.println("tschau");
			lfi = new ListenForInput(server, mf);
			listenForInput = new Thread(lfi);
			listenForInput.start();
		} catch (UnknownHostException e) {
			try {
				errorFile = new File(getClass().getClassLoader().getResource("errorfile.err").toURI());
				bw = new BufferedWriter(new FileWriter(errorFile));
				df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				today = Calendar.getInstance().getTime();
				reportDate = df.format(today);
				bw.write("\n" + reportDate + " " + e.getMessage() + "\n");
				bw.flush();
			} catch (URISyntaxException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
			if (bw != null) {
					try {
						bw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				errorFile = new File(getClass().getClassLoader().getResource("errorfile.err").toURI());
				bw = new BufferedWriter(new FileWriter(errorFile));
				df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				today = Calendar.getInstance().getTime();
				reportDate = df.format(today);
				System.out.println("\n" + reportDate + " " + e.getMessage() + "\n");
				bw.write("\n" + reportDate + " " + e.getMessage() + "\n");
				bw.flush();
			} catch (URISyntaxException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}finally {
			
		}
		
	}
	
	public void disconnect() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendSendable() {
		try {
			oos = new ObjectOutputStream(server.getOutputStream());
			//oos.writeObject();
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}




}
