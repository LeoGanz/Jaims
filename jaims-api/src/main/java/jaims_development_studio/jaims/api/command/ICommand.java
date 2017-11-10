package jaims_development_studio.jaims.api.command;

import java.util.List;

import jaims_development_studio.jaims.api.IServer;

public interface ICommand extends Comparable<ICommand> {

	/**
	 * Gets the name of the command
	 */
	String getName();

	/**
	 * Gets the usage string for the command.
	 *
	 * @param sender The ICommandSender who is requesting usage details
	 */
	String getUsage(ICommandSender sender);

	/**
	 * Get a list of all possible aliases for this command.
	 */
	List<String> getAliases();

	/**
	 * Callback for when the command is executed
	 *
	 * @param server The server instance
	 * @param sender The sender who executed the command
	 * @param args The arguments that were passed
	 */
	void execute(IServer server, ICommandSender sender, String[] args) throws Exception; //TODO specify exception
	
	/**
	 * Check if the given ICommandSender has permission to execute this command
	 *
	 * @param server The server instance
	 * @param sender The ICommandSender to check permissions on
	 */
	boolean checkPermission(IServer server, ICommandSender sender);
	
	/**
	 * Get a list of all possible tab completions for this command.
	 */
	List<String> getTabCompletions(IServer server, ICommandSender sender, String[] args);
	
	/**
	 * Return whether the specified command parameter index is a username parameter.
	 *
	 * @param args The arguments of the command invocation
	 * @param index The index
	 */
	boolean isUsernameIndex(String[] args, int index);
	
}
