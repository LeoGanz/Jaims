package jaims_development_studio.jaims.api.sendables;

public class SendableDFEInitiation extends SendableDirectDelivery {

	private static final long	serialVersionUID	= 1L;

	private int					port;
	private ESendableType		type				= ESendableType.DFE_INITIATION;

	public SendableDFEInitiation(int port) {

		super(ESendableType.DIRECT_DELIVERY);

		this.port = port;
	}

	public int getPort() {

		return port;
	}

	public ESendableType getType() {

		return type;
	}

}
