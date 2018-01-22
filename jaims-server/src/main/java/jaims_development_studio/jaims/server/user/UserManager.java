package jaims_development_studio.jaims.server.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.profile.NoProfileAvailableException;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.EMessageType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableTypeException;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.sendables.SendableTextMessage;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.util.ObservableList;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;

public class UserManager implements Serializable {
	
	private final Logger			LOG					= LoggerFactory.getLogger(UserManager.class);
	private static final long		serialVersionUID	= 1L;
	private final AccountManager	accountManager;
	private final ProfileManager	profileManager;
	private final UserDAO			userDAO;
	private final Server			server;
	private final Map<UUID, User>	loadedUsers;
	private final List<User>		onlineUsers;
	
	public UserManager(Server server) {
		this.server = server;
		accountManager = new AccountManager();
		profileManager = new ProfileManager(this);
		userDAO = new UserDAO();
		loadedUsers = new HashMap<>();
		onlineUsers = new ObservableList<>(new LinkedList<>());
		new Thread(() -> userDAO.get(UUID.randomUUID()), "Database Initializer").start();
	}

	public User registerNewUser(SendableRegistration registration) throws UserNameNotAvailableException {
		String username = registration.getUsername();
		String password = registration.getPassword();
		String email = registration.getEmail();
		
		Account account = accountManager.createNewAccount(username, password, email);
		User user = new User(server, account);
		
		save(user);
		loadedUsers.put(user.getAccount().getUuid(), user);
		
		return user;
	}

	public User loginUser(SendableLogin login) throws UserNotFoundException, IncorrectPasswordException {
		UUID uuid = getUuidForUsername(login.getUsername());
		
		User user = get(uuid);
		if (user == null)
			throw new UserNotFoundException("User with UUID " + uuid + " could not be found!");
		user.setServer(server);
		
		Account account = user.getAccount();

		boolean correctPassword = account.validatePassword(login.getPassword());
		if (!correctPassword)
			throw new IncorrectPasswordException("Invalid password for account " + account);
		
		onlineUsers.add(user);
		
		return user;
	}
	
	public void logoutUser(User user) {
		if (user != null)
			onlineUsers.remove(user);
	}

	public void deleteUserAndAccount(UUID uuid) {
		LOG.info("Deleting User " + uuid);
		User user = loadedUsers.remove(uuid);
		onlineUsers.remove(user);
		user = null;
		accountManager.deleteAccount(uuid); //accountDAO in AccountManager will delete both user and account objects
	}

	public void deliverMessage(SendableMessage message) throws UserNotFoundException {
		UUID recipientUuid = message.getRecipient();
		
		if (recipientUuid.equals(server.getServerUUID()) && (message.getMessageType() == EMessageType.TEXT)) {
			UUID senderUuid = message.getSender();
			User sender = get(senderUuid); //TODO first check if user is logged in and user object can be taken from some central user management. Otherwise sendAutoS. will listen on the wrong object
			
			if (sender == null)
				throw new UserNotFoundException("Could not find account of sender with UUID " + senderUuid + "!");
			
			SendableTextMessage sendableTextMessage = (SendableTextMessage) message;

			LOG.info(sender.getName() + ": " + sendableTextMessage.getMessage());

			if (sendableTextMessage.getMessage().startsWith("/"))
				server.addPendingCommand(sendableTextMessage.getMessage(), sender);
			return;
		}
		
		User recipient = get(recipientUuid);

		if (recipient == null)
			throw new UserNotFoundException("Could not deliver message to user " + recipientUuid + "!");

		recipient.enqueueSendable(message);
	}

	public void save(User user) {
		if (user != null) {
			user.updateLastSeen();
			userDAO.saveOrUpdate(user);
		}
	}
	
	public User get(UUID uuid) {
		User result = loadedUsers.get(uuid);
		if (result == null) {
			result = userDAO.get(uuid);
			loadedUsers.put(uuid, result);
		}
		return result;
	}
	
	public UUID getUuidForUsername(String username) {
		return accountManager.getUuidForUsername(username);
	}
	
	public String getUsernameForUuid(UUID uuid) {
		return accountManager.getUsernameForUuid(uuid);
	}
	
	public List<User> getOnlineUsers() {
		return onlineUsers;
	}
	
	public AccountManager getAccountManager() {
		return accountManager;
	}
	
	public Profile getProfile(UUID uuid) {
		return profileManager.get(uuid);
	}

	public void manageRequest(SendableRequest request) throws InvalidSendableTypeException, NoProfileAvailableException {

		switch (request.getRequestType()) {
			case DELETE_ACCOUNT:
				deleteUserAndAccount(request.getUniversalUuid());
				break;
			case PROFILE:
				try {
					profileManager.manageProfileRequest(request);
				} catch (InvalidSendableTypeException e) {
					LOG.error("No InvalidSendableTypeException should have occured in this environment!", e);
				} catch (NoProfileAvailableException e) {
					throw e;
				}
				break;
			default:
				throw new InvalidSendableTypeException("Cannot process SendableRequest of type " + request.getRequestType(), request);
		}

	}
	
	public void manageReceiveProfile(SendableProfile profile) {
		profileManager.saveOrUpdateProfile(profile);
	}
	
}
