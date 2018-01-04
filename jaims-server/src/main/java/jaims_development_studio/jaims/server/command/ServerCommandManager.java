package jaims_development_studio.jaims.server.command;

import jaims_development_studio.jaims.api.IServer;
import jaims_development_studio.jaims.api.command.CommandBase;
import jaims_development_studio.jaims.api.command.CommandDate;
import jaims_development_studio.jaims.api.command.CommandHandler;
import jaims_development_studio.jaims.api.command.CommandHelp;
import jaims_development_studio.jaims.api.command.ICommand;
import jaims_development_studio.jaims.api.command.ICommandListener;
import jaims_development_studio.jaims.api.command.ICommandSender;
import jaims_development_studio.jaims.server.Server;

public class ServerCommandManager extends CommandHandler implements ICommandListener {
	
	private final Server server;

	public ServerCommandManager(Server server) {
		this.server = server;
		
		//register all commands by creating an instance of each command class
		registerCommand(new CommandHelp());
		registerCommand(new CommandDate());
		
		CommandBase.setCommandListener(this);
	}
	
	@Override
	public void notifyListener(ICommandSender sender, ICommand command, String message) {
		if ((sender != server) && sender.sendCommandFeedback())
			sender.sendMessage("Your command was executed succesfully!");

		server.sendMessage(sender.getName() + " executed command " + command.getName() + " : " + message);
	}
	
	@Override
	protected IServer getServer() {
		return server;
	}
	
}
