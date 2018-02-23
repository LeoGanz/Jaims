package jaims_development_studio.jaims.api.util;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.NotImplementedException;

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

	@SuppressWarnings("static-method")
	public AccountUuidEntity copyWithoutAccount() {
		throw new NotImplementedException("Method copyWithoutAccount() not implemented!");
	}
	
	public Account getAccount() {

		return account;
	}

	public void setAccount(Account account) {

		this.account = account;
	}

}
