package jaims_development_studio.jaims.server.user;

import java.io.Serializable;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.account.AccountManager;

public class UserManager implements Serializable {

	private final transient Logger	LOG					= LoggerFactory.getLogger(UserManager.class);
	private static final long		serialVersionUID	= 1L;
	private final AccountManager	accountManager;
	private final UserDAO			userDAO				= new UserDAO();
	private transient Server		server;

	public UserManager(Server server) {
		this.server = server;
		accountManager = new AccountManager();
	}
	
	public UUID getUuidForUsername(String username) {
		return accountManager.getUuidForUsername(username);
	}
	
	public User registerNewUser(SendableRegistration registration) throws UserNameNotAvailableException {
		String username = registration.getUsername();
		String password = registration.getPassword();
		String email = registration.getEmail();

		Account account = accountManager.createNewAccount(username, password, email);
		User user = new User(account);

		userDAO.saveOrUpdate(user);

		//		users.put(account.getUuid(), user);

		return user;
	}
	
	public User loginUser(SendableLogin login) throws UserNotFoundException, IncorrectPasswordException {
		UUID uuid = getUuidForUsername(login.getUsername());

		User user = userDAO.get(uuid);
		//		User user = users.get(uuid);
		if (user == null)
			throw new UserNotFoundException("User with UUID " + uuid + " could not be found!");
		
		Account account = user.getAccount();
		
		boolean correctPassword = account.validatePassword(login.getPassword());
		if (!correctPassword)
			throw new IncorrectPasswordException("Invalid password for account" + account);

		return user;
	}
	
	public void deleteUserAndAccount(UUID uuid) {
		LOG.info("Deleting User " + uuid);
		accountManager.deleteAccount(uuid); //accountDAO in AccountManager will also delete user object
	}
	
	public void save(User user) {
		userDAO.saveOrUpdate(user);
	}
	

	public void deliverMessage(SendableMessage message) throws UserNotFoundException {
		UUID recipientUuid = message.getRecipient();
		User recipient = userDAO.get(recipientUuid);
		
		if (recipient == null) {
			if (recipientUuid.equals(UUID.fromString("SERVER"))) {
				UUID senderUuid = message.getRecipient();
				User sender = userDAO.get(senderUuid);
				server.addPendingCommand(message.getMessage(), sender);
				return;
			}
			throw new UserNotFoundException("Could not deliver message to user " + recipientUuid + "!");
		}
		
		recipient.enqueueSendable(message);
	}
}