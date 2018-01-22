package jaims_development_studio.jaims.server;

/**
 * Every type that implements the interface {@link ITickable} has to override the {@link #tick()} method through which
 * it can specify code that is to be run in regular intervals by the {@link Server}.
 *
 * @author WilliGross
 */
public interface ITickable {
	
	/**
	 * This method will be called in regular intervals by the server's ticker thread to signal the component that it
	 * should update itself
	 */
	public void tick();
	
}
