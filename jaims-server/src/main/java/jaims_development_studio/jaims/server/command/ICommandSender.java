package jaims_development_studio.jaims.server.command;


public interface ICommandSender {

	/**
	 * Get the name of this object. For clients this returns their username
	 */
	String getName();
	
	/**
	 * Send a chat message to the CommandSender
	 */
	void sendMessage(String msg);
	
	/**
	 * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
	 */
	boolean canUseCommand(int permLevel, String commandName);
	
	/**
	 * Returns true if the command sender should be sent feedback about executed commands
	 */
	boolean sendCommandFeedback();
}
