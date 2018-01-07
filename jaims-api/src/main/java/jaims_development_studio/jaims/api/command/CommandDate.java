package jaims_development_studio.jaims.api.command;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.ImmutableList;

import jaims_development_studio.jaims.api.IServer;

public class CommandDate extends CommandBase {

	private static final Collection<String>	arguments	= ImmutableList.of("short", "medium", "long", "full");
	private static final DateFormat			SHORT		= DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	private static final DateFormat			MEDIUM		= DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
	private static final DateFormat			LONG		= DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	private static final DateFormat			FULL		= DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());

	@Override
	public String getName() {
		return "date";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return CommandBase.PERMISSION_LEVEL_UTILITY;
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/date [short | medium | long | full] - returns the current date in the specified formatting";
	}

	@Override
	public void execute(IServer server, ICommandSender sender, String[] args) throws Exception {
		if (executeHelp(sender, args))
			return;

		String date;
		if (args.length > 0)
			switch (args[0].toUpperCase()) {
				case "SHORT":
					date = SHORT.format(new Date());
					break;
				case "MEDIUM":
					date = MEDIUM.format(new Date());
					break;
				case "LONG":
					date = LONG.format(new Date());
					break;
				case "FULL":
					date = FULL.format(new Date());
					break;
				default:
					date = "Invalid Format! See /date help for info";
					break;
			}
		else
			date = MEDIUM.format(new Date());

		sender.sendMessage(date);
	}

	@Override
	public List<String> getTabCompletions(IServer server, ICommandSender sender, String[] args) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, arguments) : Collections.EMPTY_LIST;
	}

}
