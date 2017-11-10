package jaims_development_studio.jaims.api.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import jaims_development_studio.jaims.api.IServer;

public abstract class CommandHandler implements ICommandManager {

	private static final Logger			LOG			= LoggerFactory.getLogger(CommandHandler.class);
	private final Map<String, ICommand>	commandMap	= Maps.newHashMap();
	private final Set<ICommand>			commandSet	= Sets.newHashSet();
	
	@Override
	public boolean executeCommand(ICommandSender sender, String rawCommand) {

		String commandString = rawCommand.trim();

		if (commandString.startsWith("/"))
			commandString = commandString.substring(1);

		String[] splitCommand = commandString.split(" ");
		String baseCommand = splitCommand[0];

		String[] arguments = new String[splitCommand.length - 1];
		System.arraycopy(splitCommand, 1, arguments, 0, arguments.length);
		
		ICommand command = commandMap.get(baseCommand);

		if (command == null)
			sender.sendMessage("Command not found! Try /help");
		else if (command.checkPermission(getServer(), sender)) {
			int j = getUsernameIndex(command, arguments);
			
			if (j > -1) {
				//TODO find player(s) and replace arguments[j] with users id
			}
			
			return tryExecute(sender, arguments, command, commandString);
		}
		
		return false;
	}

	private boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input) {
		try {
			command.execute(getServer(), sender, args);
			return true;
		} catch (Exception e) {
			//TODO split in WrongUsageException, CommandException, Throwable
			LOG.warn("Couldn't execute command by " + sender + "!", e);
			sender.sendMessage("Couldn't execute command!");
		} catch (Throwable t) {
			sender.sendMessage("Unexpected problems occured when processing command!");
			LOG.warn("Couldn't process command: '" + input + "'", t);
		}
		
		return false;
		
	}

	protected abstract IServer getServer();
	
	/**
	 * Adds the command and any aliases it has to the internal map of available commands
	 */
	public ICommand registerCommand(ICommand command) {
		commandMap.put(command.getName(), command);
		commandSet.add(command);
		
		for (String alias : command.getAliases()) {
			ICommand icommand = commandMap.get(alias);
			
			if ((icommand == null) || !icommand.getName().equals(alias))
				commandMap.put(alias, command);
		}
		
		return command;
	}

	@Override
	public List<String> getTabCompletions(ICommandSender sender, String input) {
		
		String[] splitInput = input.split(" ");
		String start = splitInput[0];

		if (splitInput.length == 1) {
			List<String> list = Lists.newArrayList();
			for (Entry<String, ICommand> entry : commandMap.entrySet())
				if (entry.getKey().startsWith(start))
					list.add(entry.getKey());
			return list;
		} else if (splitInput.length > 1) {
			ICommand command = commandMap.get(start);
			if ((command != null) && command.checkPermission(getServer(), sender)) {
				String[] args = new String[splitInput.length - 1];
				System.arraycopy(splitInput, 1, args, 0, args.length);
				return command.getTabCompletions(getServer(), sender, args);
			}
		}

		return Collections.emptyList();
	}

	@Override
	public List<ICommand> getPossibleCommands(ICommandSender sender) {
		List<ICommand> possibleCommands = Lists.newArrayList();
		
		for (ICommand cmd : commandSet)
			if (cmd.checkPermission(getServer(), sender))
				possibleCommands.add(cmd);

		return possibleCommands;
	}

	@Override
	public Map<String, ICommand> getCommands() {
		return commandMap;
	}
	
	/**
	 * Return a command's first parameter index containing a valid username.
	 */
	@SuppressWarnings("static-method")
	private int getUsernameIndex(ICommand command, String[] args) {
		if (command == null)
			return -1;
		for (int i = 0; i < args.length; ++i)
			if (command.isUsernameIndex(args, i) && true) //TODO second part needs to check if player can be found
				return i;

		return -1;
	}
	
}
