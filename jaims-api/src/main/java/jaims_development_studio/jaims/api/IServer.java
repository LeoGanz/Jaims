package jaims_development_studio.jaims.api;

import java.util.List;
import java.util.UUID;

import jaims_development_studio.jaims.api.command.ICommandManager;

public interface IServer {
	
	public ICommandManager getCommandManager();
	
	public List<String> getAllRegisteredUsernames();

	public UUID getServerUUID();
}
