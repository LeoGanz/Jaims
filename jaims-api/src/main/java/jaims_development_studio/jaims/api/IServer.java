package jaims_development_studio.jaims.api;

import jaims_development_studio.jaims.api.command.ICommandManager;

public interface IServer {

	public ICommandManager getCommandManager();
}
