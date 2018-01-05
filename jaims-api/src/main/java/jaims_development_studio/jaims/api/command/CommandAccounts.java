package jaims_development_studio.jaims.api.command;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import jaims_development_studio.jaims.api.IServer;


public class CommandAccounts extends CommandBase {

	@Override
	public String getName() {
		return "accounts";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return CommandBase.PERMISSION_LEVEL_MODERATION;
	}
	
	@Override
	public List<String> getAliases() {
		return ImmutableList.of("accs");
	}
	
	@Override
	public List<String> getTabCompletions(IServer server, ICommandSender sender, String[] args) {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/accounts - displays all registered accounts";
	}

	@Override
	public void execute(IServer server, ICommandSender sender, String[] args) throws Exception {
		List<String> usernames = server.getAllRegisteredUsernames();
		for (String s : usernames)
			sender.sendMessage(s);
	}

}
