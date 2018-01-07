package jaims_development_studio.jaims.api.command;

import java.util.List;

import com.google.common.collect.ImmutableList;

import jaims_development_studio.jaims.api.IServer;

public class CommandHelp extends CommandBase {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return CommandBase.PERMISSION_LEVEL_ALL;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/help - shows all possible commands";
	}

	@Override
	public List<String> getAliases() {
		return ImmutableList.of("?");
	}

	@Override
	public void execute(IServer server, ICommandSender sender, String[] args) throws Exception {
		
		List<ICommand> possibleCommands = server.getCommandManager().getPossibleCommands(sender);
		
		if (args.length == 1) {
			for (ICommand cmd : possibleCommands)
				if (cmd.getName().equals(args[0]))
					sender.sendMessage(cmd.getUsage(sender));
		} else {

			sender.sendMessage("--- Showing help page ---");
			
			for (ICommand cmd : possibleCommands)
				sender.sendMessage(cmd.getUsage(sender));
		}
	}
	
	@Override
	public List<String> getTabCompletions(IServer server, ICommandSender sender, String[] args) {
		if (args.length != 1)
			return super.getTabCompletions(server, sender, args);
		return getListOfStringsMatchingLastWord(args, server.getCommandManager().getCommandNames());
	}
	
}
