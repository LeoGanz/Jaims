package jaims_development_studio.jaims.client.directFileExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.directFileExchange.sendables.DFESFileData;
import jaims_development_studio.jaims.client.directFileExchange.sendables.DFESendable;
import jaims_development_studio.jaims.client.directFileExchange.sendables.SDFECloseFile;
import jaims_development_studio.jaims.client.directFileExchange.sendables.SDFEFileInfo;
import jaims_development_studio.jaims.client.directFileExchange.sendables.SDFEStartSending;

public class DFEServer {
	
	private static final Logger	LOG						= LoggerFactory.getLogger(DFEServer.class);
	
	private DFEManager dfeManager;
	private ServerSocket server;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public DFEServer(DFEManager dfeManager) {
		this.dfeManager = dfeManager;
		
	}
	
	public boolean openServerAndAcceptClient() {
		try {
			server = new ServerSocket(dfeManager.getSettings().getDFEPort());
			socket = server.accept();
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread() {
				@Override
				public void run() {
					listenForInput();
				}
			}.start();
			return true;
		} catch (IOException e) {
			LOG.error("Error while trying to initialise DFE connection!", e);
			openServerAndAcceptClient();
		}
		
		return false;
	}
	
	public synchronized void sendSendable(DFESendable dfes) {
		try {
			oos.writeObject(dfes);
			oos.flush();
		} catch (IOException e) {
			LOG.error("Error while trying to send DFESendable!", e);
		}
	}
	
	public void closeConnection() {
		try {
			oos.flush();
			oos.close();
			socket.close();
		} catch (IOException e) {
			LOG.error("Error while trying to close DFE connection!", e);
		}
	
	}
	
	private void listenForInput() {
		while (socket.isConnected()) {
			try {
				handleSendable((DFESendable) ois.readObject());
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				LOG.error("Error while trying to read DFE sendable", e);
			}
		}
	}
	
	private void handleSendable(DFESendable dfes) {
		switch(dfes.getType().getValue()) {
		case "FILE_INFORMATION":
			createNewFile((SDFEFileInfo) dfes);
		case "FILE_DATA":
			dfeManager.addDataToFile((DFESFileData) dfes);
		case "CLOSE_FILE":
			dfeManager.closeFile(((SDFECloseFile) dfes).getFileID());
		case "START_SENDING":
			sendFile(((SDFEStartSending) dfes).getFileID(), ((SDFEStartSending) dfes).getStartValue());
		}
		
	}
	
	private void createNewFile(SDFEFileInfo info) {
		//Select place for saving
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		jfc.showOpenDialog(dfeManager.getCurrentParentPanel());
		
		File directory = jfc.getSelectedFile();
		
		File file = new File(directory.getAbsolutePath() + "/" + info.getFileName());
		dfeManager.addFileToMap(info.getFileID(), file.getAbsolutePath());
		
		//-----------------------------------------
		//---	Add DFE object to window here	---
		//-----------------------------------------
		
		sendSendable(new SDFEStartSending(info.getFileID(), dfeManager.getUserId(), dfeManager.getRecipientID(), 0L));
	}

	public void stopSending() {
		
	}
	
	private void sendFile(UUID fileID, long startValue) {
		new Thread() {
			@Override
			public void run() {
				try {
					FileInputStream fis = new FileInputStream(dfeManager.getFileToSend(fileID));
					byte[] arr = new byte[dfeManager.getSettings().getBufferSize()];
					fis.skip(startValue);
					while(fis.read(arr) > 0) {
						DFESFileData sendable = new DFESFileData(fileID,dfeManager.getUserId(), dfeManager.getRecipientID(), arr);
						sendSendable(sendable);
						
						//Update progress bar
						
						//Let thread sleep
					}
					fis.close();
				} catch (FileNotFoundException e) {
					LOG.error("Couldn't find file!", e);
				} catch (IOException e) {
					LOG.error("Error while sending", e);
				}
			}
		}.start();
	}
}
