package jaims_development_studio.jaims.api.sendables;


public class SendableDFEInitiation extends SendableDirectDelivery {
	
	private static final long	serialVersionUID	= 1L;
	
	private int					port;
	
	public SendableDFEInitiation(int port) {
		
		super(ESendableType.DFE_INITATION);

		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
}
