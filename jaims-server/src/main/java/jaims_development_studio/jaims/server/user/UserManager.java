package jaims_development_studio.jaims.server.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.account.Account;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.account.IncorrectPasswordException;
import jaims_development_studio.jaims.server.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.server.network.sendables.SendableLogin;
import jaims_development_studio.jaims.server.network.sendables.SendableMessage;
import jaims_development_studio.jaims.server.network.sendables.SendableRegistration;

public class UserManager implements Serializable {

	private final transient Logger	LOG					= LoggerFactory.getLogger(UserManager.class);
	private static final long		serialVersionUID	= 1L;
	private final AccountManager	accountManager;
	private final Map<UUID, User>	users				= new HashMap<>();
	private transient Server		server;

	public UserManager() {
		accountManager = new AccountManager();
	}
	
	public UUID getUuidForUsername(String username) {
		//TODO implement
		return null;
	}
	
	public User registerNewUser(SendableRegistration registration) throws UserNameNotAvailableException {
		String username = registration.getUsername();
		String password = registration.getPassword();
		String email = registration.getEmail();

		Account account = accountManager.createNewAccount(username, password, email);
		User user = new User(account);

		users.put(account.getUuid(), user);

		return user;
	}
	
	public User loginUser(SendableLogin login) throws UserNotFoundException, IncorrectPasswordException {
		UUID uuid = getUuidForUsername(login.getUsername());

		User user = users.get(uuid);
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
		users.remove(uuid); //are saved users deleted as well if new file is written?
		accountManager.deleteAccount(uuid);
	}
	
	public void save() {
		//TODO saves only parts of users list / account lists to improve performance, sets local (transient) variable to indicate the next batch
	}

	public static UserManager load(Server server) {
		// TODO Load user manager, if none can be loaded return new one
		UserManager userManager = new UserManager();
		userManager.server = server;
		return userManager;
	}
	
	public void saveAll() {
		// TODO saves everything
		save(); //just for now
	}

	public void deliverMessage(SendableMessage message) throws UserNotFoundException {
		UUID recipientUuid = message.getRecipient();
		User recipient = users.get(recipientUuid);
		
		if (recipient == null) {
			if (recipientUuid.equals(UUID.fromString("SERVER"))) {
				server.addPendingCommand(message.getMessage(), users.get(message.getSender()));
				return;
			}
			throw new UserNotFoundException("Could not deliver message to user " + recipientUuid + "!");
		}
		
		recipient.enqueueSendable(message);
	}
}
