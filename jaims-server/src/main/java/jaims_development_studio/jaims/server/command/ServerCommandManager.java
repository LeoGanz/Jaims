package jaims_development_studio.jaims.server.command;

import jaims_development_studio.jaims.server.Server;

public class ServerCommandManager extends CommandHandler implements ICommandListener {
	
	private final Server server;

	public ServerCommandManager(Server server) {
		this.server = server;
		
		//register all commands by creating an instance of each command class
		registerCommand(new CommandHelp());
		
		CommandBase.setCommandListener(this);
	}
	
	@Override
	public void notifyListener(ICommandSender sender, ICommand command, String message) {
		if ((sender != server) && sender.sendCommandFeedback())
			sender.sendMessage("Your command was executed succesfully!");

		server.sendMessage(sender.getName() + " executed command " + command.getName() + " : " + message);
	}
	
	@Override
	protected Server getServer() {
		return server;
	}
	
}
