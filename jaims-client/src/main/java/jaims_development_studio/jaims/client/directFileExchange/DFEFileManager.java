package jaims_development_studio.jaims.client.directFileExchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.client.directFileExchange.sendables.DFESFileData;
import jaims_development_studio.jaims.client.directFileExchange.sendables.SDFEFileInfo;

public class DFEFileManager {
	
	private static final Logger	LOG						= LoggerFactory.getLogger(DFEFileManager.class);
	private HashMap<UUID, String> openFilesMap = new HashMap<>();
	private HashMap<UUID, String> filesToSend = new HashMap<>();
	
	
	
	public void addFileToMap(UUID fileID, String path) {
		openFilesMap.put(fileID, path);
	}
	
	public void addFileToSendQueue(UUID fileID, String path) {
		filesToSend.put(fileID, path);
	}
	
	public void addDataToFile(DFESFileData sendable) {
		try {
			Files.write(Paths.get(openFilesMap.get(sendable.getFileID())),sendable.getData(),StandardOpenOption.APPEND);
		} catch (IOException e) {
			LOG.error("Error while trying to write data to file", e);
		}
		
	}
	
	public void closeFile(UUID fileID) {
		openFilesMap.remove(fileID);
	}
	
	public File getFileToSend(UUID fileID) {
		return new File(filesToSend.get(fileID));
	}
}
