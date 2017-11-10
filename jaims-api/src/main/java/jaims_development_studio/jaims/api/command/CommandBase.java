package jaims_development_studio.jaims.api.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import jaims_development_studio.jaims.api.IServer;

public abstract class CommandBase implements ICommand {

	private static ICommandListener commandListener;
	
	/**
	 * Return the required permission level for this command.
	 */
	@SuppressWarnings("static-method")
	public int getRequiredPermissionLevel()
	{
		/*
		 * 1: utility commands like /date
		 * 2: moderator commands like banning users
		 * 3: administrator commands like stopping the server
		 */
		return 3;
	}
	
	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}
	
	@Override
	public boolean checkPermission(IServer server, ICommandSender sender) {
		return sender.canUseCommand(getRequiredPermissionLevel(), getName());
	}
	
	@Override
	public List<String> getTabCompletions(IServer server, ICommandSender sender, String[] args) {
		return Collections.emptyList();
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	public static void notifyCommandListener(ICommandSender sender, ICommand command, String message) {
		if (commandListener != null)
			commandListener.notifyListener(sender, command, message);
	}

	/**
	 * Sets the command listener responsable for notifying server operators when asked to by commands
	 */
	public static void setCommandListener(ICommandListener listener) {
		commandListener = listener;
	}
	
	/**
	 * Test if the last element of args can be tab completed to one or more elements of possibleCompletions
	 *
	 * @param args the command's arguments, the last element will be checked for completion possibilities
	 * @param possibleCompletions a collection with all possible completions (usually the command map's key set)
	 * @return a list with all strings that start with the last element of args
	 */
	public static List<String> getListOfStringsMatchingLastWord(String[] args, Collection<?> possibleCompletions) {
		String last = args[args.length - 1];
		List<String> matches = Lists.newArrayList();
		
		if (!possibleCompletions.isEmpty())
			for (String s : Iterables.transform(possibleCompletions, Functions.toStringFunction()))
				if (s.startsWith(last))
					matches.add(s);
		
		return matches;
	}
	
	/**
	 * Compares this command's name to the other command's name lexicographically
	 */
	@Override
	public int compareTo(ICommand otherCommand) {
		return getName().compareTo(otherCommand.getName());
	}

}
