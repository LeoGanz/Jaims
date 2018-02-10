package jaims_development_studio.jaims.api.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jaims_development_studio.jaims.api.account.Account;

/**
 * @author WilliGross
 */
@MappedSuperclass
public abstract class UpdateTrackingUuidEntity extends AccountUuidEntity {
	
	private static final long	serialVersionUID	= 1L;
	
	@Column(name = "LAST_UPDATED", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdated;
	
	public UpdateTrackingUuidEntity(Date lastUpdated, Account account) {
		super(account);
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
