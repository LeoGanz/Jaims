package jaims_development_studio.jaims.api.util;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import jaims_development_studio.jaims.api.account.Account;

/**
 * @author WilliGross
 */
@MappedSuperclass
public abstract class AccountUuidEntity extends UuidEntity {

	private static final long	serialVersionUID	= 1L;

	@OneToOne(cascade = CascadeType.DETACH)
	@MapsId
	private Account				account;

	public AccountUuidEntity(Account account) {
		this.account = account;
	}

	public abstract AccountUuidEntity copyWithoutAccount();
	
	public Account getAccount() {

		return account;
	}

	public void setAccount(Account account) {

		this.account = account;
	}

}
