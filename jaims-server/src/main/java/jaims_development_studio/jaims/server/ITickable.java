package jaims_development_studio.jaims.server;

public interface ITickable {

	/**
	 * This method will be called in regular intervals by the server's main thread to signal the component that it
	 * should update itself
	 */
	public void tick();

}
