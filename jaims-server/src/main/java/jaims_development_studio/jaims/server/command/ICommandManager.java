package jaims_development_studio.jaims.server.command;

import java.util.List;
import java.util.Map;

public interface ICommandManager {

	/**
	 * Attempt to execute a command. This method should return if the execution was successful.
	 *
	 * @param sender The source of the command invocation
	 * @param rawCommand The raw string that was typed
	 *
	 * @return false if command doesn't exist or sender doesn't have permission, otherwise true
	 */
	boolean executeCommand(ICommandSender sender, String rawCommand);
	
	List<String> getTabCompletions(ICommandSender sender, String input);
	
	List<ICommand> getPossibleCommands(ICommandSender sender);
	
	Map<String, ICommand> getCommands();

}
