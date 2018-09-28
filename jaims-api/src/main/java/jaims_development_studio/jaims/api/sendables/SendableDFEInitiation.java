package jaims_development_studio.jaims.api.sendables;

/**
 * This sendable of type ESendableType.DFE_INITATION (see: {@link ESendableType.Values}) serves in two ways: <br>
 * <ol>
 * <li>It's main task is to serve as a request from User A to user B to initiate a direct file exchange (short: DFE)</li>
 * <li>It's second function is holding the port which user A wants to use for the DFE. This though only matters when user B accepts the request.</li>
 * </ul>
 * <br>
 * 
 * @since v0.1.0
 * 
 * @author Bu88le
 *
 */
public class SendableDFEInitiation extends Sendable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int port;

	public SendableDFEInitiation(int port) {

		super(ESendableType.DFE_INITATION);
		
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}

}
