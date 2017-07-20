package jaims_development_studio.jaims.server.command;

public class PendingCommand {
	
	/** The command string */
	private final String			command;
	/** The command sender */
	private final ICommandSender	sender;

	public PendingCommand(String command, ICommandSender sender) {
		this.command = command;
		this.sender = sender;
	}
	
	/**
	 * @return the command as a raw string
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the command sender
	 */
	public ICommandSender getSender() {
		return sender;
	}

}
