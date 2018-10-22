package jaims_development_studio.jaims.client.directFileExchange;

import java.io.File;
import java.util.UUID;

import jaims_development_studio.jaims.api.settings.Settings;
import jaims_development_studio.jaims.client.directFileExchange.sendables.DFESFileData;
import jaims_development_studio.jaims.client.gui.customGUIComponents.ParentPanel;
import jaims_development_studio.jaims.client.gui.dfe.DFEWindow;
import jaims_development_studio.jaims.client.logic.ClientMain;

public class DFEManager {

	private ClientMain		cm;
	private DFEWindow		window;
	private DFEFileManager	fileManager;
	private DFEServer		server;
	private boolean			runsAsServer;

	public DFEManager(ClientMain cm) {

		this.cm = cm;

		fileManager = new DFEFileManager();
	}

	public boolean initiateServer() {

		server = new DFEServer(this);
		return server.openServerAndAcceptClient();
	}

	public void initiateClient() {

	}

	public void addFileToMap(UUID fileID, String path) {

		fileManager.addFileToMap(fileID, path);
	}

	public void addDataToFile(DFESFileData dfes) {

		fileManager.addDataToFile(dfes);
	}

	public void closeFile(UUID fileID) {

		fileManager.closeFile(fileID);
	}

	public ParentPanel getCurrentParentPanel() {

		return cm.getCurrentParentPanel();
	}

	public Settings getSettings() {

		return cm.getSettings();
	}

	public File getFileToSend(UUID fileID) {

		return fileManager.getFileToSend(fileID);
	}

	public void sendFiles(File[] filesToSend) {

	}

	public void stopDFESending() {

		if (runsAsServer)
			server.stopSending();
	}

	public UUID getUserId() {

		return cm.getUserUUID();
	}

	public UUID getRecipientID() {

		return window.getRecipientID();
	}
}
