package jaims_development_studio.jaims.server.account;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;

public class AccountManager implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private final AccountDAO	accountDAO;
	private final Map<UUID, Account>	loadedAccounts;
	
	public AccountManager() {
		accountDAO = new AccountDAO();
		loadedAccounts = new HashMap<>();
	}

	public boolean isUsernameAvailable(String userName) {
		List<String> takenUserNames = accountDAO.getAllUsernames();
		for (String s : takenUserNames)
			if (s.equalsIgnoreCase(userName))
				return false;
		return true;
	}
	
	public Account createNewAccount(String userName, String password, String email)
			throws UserNameNotAvailableException {
		if (!isUsernameAvailable(userName))
			throw new UserNameNotAvailableException("Username '" + userName + "' is not available!");
		
		//validate password criteria
		
		Account account = new Account(userName, password, email);
		account.setRegistrationDate(new Date());
		
		accountDAO.saveOrUpdate(account);
		loadedAccounts.put(account.getUuid(), account);
		
		return account;
	}
	
	public void deleteAccount(UUID uuid) {
		accountDAO.delete(uuid);
		loadedAccounts.remove(uuid);
	}
	
	public UUID getUuidForUsername(String username) {
		return accountDAO.getUUID(username);
	}
	
	public String getUsernameForUuid(UUID uuid) {
		return accountDAO.getUsername(uuid);
	}

	public List<String> getAllRegisteredUsernames() {
		return accountDAO.getAllUsernames();
	}

	public Account get(UUID uuid) {
		Account result = loadedAccounts.get(uuid);
		if (result == null) {
			result = accountDAO.get(uuid);
			loadedAccounts.put(uuid, result);
		}
		return result;
	}
	
}
