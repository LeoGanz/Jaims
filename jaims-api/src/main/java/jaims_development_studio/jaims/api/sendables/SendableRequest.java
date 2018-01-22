package jaims_development_studio.jaims.api.sendables;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "SendableRequest")
@DiscriminatorValue(value = ESendableType.Values.REQUEST)
public class SendableRequest extends Sendable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "REQUEST_TYPE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private final ERequestType	requestType;

	@Column(name = "UNIVERSAL_DATE", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				universalDate;
	
	@Column(name = "UNIVERSAL_UUID", columnDefinition = "BINARY(16)")
	private UUID				universalUuid;

	@SuppressWarnings("unused")
	private SendableRequest() {
		this(ERequestType.OTHER);
	}

	public SendableRequest(ERequestType requestType) {
		this(requestType, null, null);
	}

	public SendableRequest(ERequestType requestType, Date universalDate) {
		this(requestType, universalDate, null);
	}

	public SendableRequest(ERequestType requestType, UUID universalUuid) {
		this(requestType, null, universalUuid);
	}

	public SendableRequest(ERequestType requestType, Date universalDate, UUID universalUuid) {
		super(ESendableType.REQUEST, 10);
		this.requestType = requestType;
		this.universalDate = universalDate;
		this.universalUuid = universalUuid;
	}

	public ERequestType getRequestType() {
		return requestType;
	}
	
	public Date getUniversalDate() {
		return universalDate;
	}
	
	public void setUniversalDate(Date universalDate) {
		this.universalDate = universalDate;
	}

	public UUID getUniversalUuid() {
		return universalUuid;
	}
	
	public void setUniversalUuid(UUID universalUuid) {
		this.universalUuid = universalUuid;
	}

}
