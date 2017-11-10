package jaims_development_studio.jaims.api.command;


public interface ICommandListener {

	/**
	 * Send an informative message to the server operators
	 */
	void notifyListener(ICommandSender sender, ICommand command, String message);

}
