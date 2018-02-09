package jaims_development_studio.jaims.server.account;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.util.EntityManager;

/**
 * @author WilliGross
 */
public class AccountManager extends EntityManager<Account> {

	private final AccountDAO dao;
	
	public AccountManager(EntityManager<?>... managers) {
		super(new AccountDAO(managers));
		dao = (AccountDAO) getDao();
		new Thread(() -> dao.get(UUID.randomUUID()), "Database Initializer").start();
	}
	
	public boolean isUsernameAvailable(String userName) {
		for (String s : getAllRegisteredUsernames())
			if (s.equalsIgnoreCase(userName))
				return false;
		return true;
	}

	public Account createNewAccount(String userName, String password, String email) throws UserNameNotAvailableException {
		if (!isUsernameAvailable(userName))
			throw new UserNameNotAvailableException("Username '" + userName + "' is not available!");

		//validate password criteria

		Account account = new Account(userName, password, email);
		account.setRegistrationDate(new Date());

		save(account);
		return account;
	}


	public UUID getUuidForUsername(String username) {
		return dao.getUUID(username);
	}

	public String getUsernameForUuid(UUID uuid) {
		return dao.getUsername(uuid);
	}
	
	public List<String> getAllRegisteredUsernames() {
		return dao.getAllUsernames();
	}

	public void delete(String username) {
		dao.delete(getUuidForUsername(username));
	}
	
	public Account get(String username) {
		return get(getUuidForUsername(username));
	}
	
}
