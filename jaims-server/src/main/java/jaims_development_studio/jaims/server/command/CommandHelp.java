package jaims_development_studio.jaims.server.command;

import java.util.List;

import com.google.common.collect.ImmutableList;

import jaims_development_studio.jaims.server.Server;

public class CommandHelp extends CommandBase {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
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
	public void execute(Server server, ICommandSender sender, String[] args) throws Exception {
		
		List<ICommand> list = server.getCommandManager().getPossibleCommands(sender);
		
		sender.sendMessage("--- Showing help page ---");
		
		for (ICommand cmd : list)
			sender.sendMessage(cmd.getUsage(sender));

	}
	
	@Override
	public List<String> getTabCompletions(Server server, ICommandSender sender, String[] args) {
		if (args.length != 1)
			return super.getTabCompletions(server, sender, args);
		return getListOfStringsMatchingLastWord(args, server.getCommandManager().getCommands().keySet());
	}
	
}
