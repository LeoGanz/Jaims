package jaims_development_studio.jaims.server.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager implements Serializable {

	private static final long			serialVersionUID	= 1L;
	private final ArrayList<String>		takenUserNames		= new ArrayList<>();
	private final Map<UUID, Account>	accounts			= new HashMap<>();

	public boolean isUsernameAvailable(String userName) {
		for (String s : takenUserNames)
			if (s.equalsIgnoreCase(userName))
				return false;
		return true;
	}

	public Account createNewAccount(String userName, String password, String email) throws UserNameNotAvailableException {
		if (!isUsernameAvailable(userName))
			throw new UserNameNotAvailableException("Username '" + userName + "' is not available!");

		//validate password criteria

		takenUserNames.add(userName);

		Account account = new Account(userName, password, email);
		
		accounts.put(account.getUuid(), account);
		return account;
	}

	public void deleteAccount(UUID uuid) {
		takenUserNames.remove(accounts.get(uuid).getUsername());
		accounts.remove(uuid);
	}

}
