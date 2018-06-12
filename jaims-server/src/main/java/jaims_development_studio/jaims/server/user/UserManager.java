package jaims_development_studio.jaims.server.user;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.IncorrectPasswordException;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.contacts.Contacts;
import jaims_development_studio.jaims.api.message.MessageAlreadyExistsException;
import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.sendables.ESendableType;
import jaims_development_studio.jaims.api.sendables.InvalidSendableException;
import jaims_development_studio.jaims.api.sendables.SendableFriendRequestResponse;
import jaims_development_studio.jaims.api.sendables.SendableLogin;
import jaims_development_studio.jaims.api.sendables.SendableMessage;
import jaims_development_studio.jaims.api.sendables.SendableMessageResponse;
import jaims_development_studio.jaims.api.sendables.SendableProfile;
import jaims_development_studio.jaims.api.sendables.SendableRegistration;
import jaims_development_studio.jaims.api.sendables.SendableRequest;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.user.UserNotFoundException;
import jaims_development_studio.jaims.api.util.NoEntityAvailableException;
import jaims_development_studio.jaims.api.util.ObservableList;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.account.AccountManager;
import jaims_development_studio.jaims.server.contacts.ContactsManager;
import jaims_development_studio.jaims.server.message.MessageManager;
import jaims_development_studio.jaims.server.profile.ProfileManager;
import jaims_development_studio.jaims.server.settings.SettingsManager;
import jaims_development_studio.jaims.server.util.EntityManager;

/**
 * @author WilliGross
 */
public class UserManager extends EntityManager<User> {
	
	private final Logger			LOG					= LoggerFactory.getLogger(UserManager.class);
	private final AccountManager	accountManager;
	private final ProfileManager	profileManager;
	private final SettingsManager	settingsManager;
	private final ContactsManager	contactsManager;
	private final MessageManager	messageManager;
	private final Server			server;
	private final List<User>		onlineUsers;
	
	public UserManager(Server server) {
		super(new UserDAO());
		this.server = server;
		profileManager = new ProfileManager(this);
		settingsManager = new SettingsManager(this);
		contactsManager = new ContactsManager(this);
		messageManager = new MessageManager(this);
		accountManager = new AccountManager(this, profileManager, settingsManager, contactsManager);
		onlineUsers = new ObservableList<>(new LinkedList<>());
	}

	public User registerNewUser(SendableRegistration registration) throws UserNameNotAvailableException {
		String username = registration.getUsername();
		String password = registration.getPassword();
		String email = registration.getEmail();
		
		Account account = accountManager.createNewAccount(username, password, email);
		User user = new User(server, account);
		
		save(user);

		initContactsForUser(account);
		
		return user;
	}

	public Contacts initContactsForUser(UUID userUUID) {
		Account account = accountManager.get(userUUID);
		return initContactsForUser(account);
	}
	
	public Contacts initContactsForUser(Account account) {
		Contacts contacts = new Contacts();
		contacts.setAccount(account);
		contacts.updateLastUpdated();
		contactsManager.saveOrUpdateEntity(contacts);
		return contacts;
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
		User user = get(uuid);
		onlineUsers.remove(user);
		user = null;
		accountManager.delete(uuid); //AccountManager will delete both user and account objects
	}

	public void deliverMessage(SendableMessage message) throws UserNotFoundException, InvalidSendableException, MessageAlreadyExistsException {
		messageManager.deliverMessage(message, server);
	}

	//	@Override
	//	public void save(User user) {
	//		if (user != null) {
	//			user.updateLastSeen();
	//			super.save(user);
	//		}
	//	}
	
	public UUID getUuidForUsername(String username) {
		return accountManager.getUuidForUsername(username);
	}

	public List<UUID> getUuidsForUsername(String username) {
		return accountManager.getUuidsForUsername(username);
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

	public void manageRequest(SendableRequest request, UUID requester)
			throws NoEntityAvailableException, InvalidSendableException {

		try {
			switch (request.getRequestType()) {
				case DELETE_ACCOUNT:
					deleteUserAndAccount(request.getUniversalUuid());
					break;
				case PROFILE:
					profileManager.manageRequest(request, requester);
					break;
				case SETTINGS:
					settingsManager.manageRequest(request, requester);
					break;
				case CONTACTS:
					contactsManager.manageRequest(request, requester);
					break;
				default:
					throw new InvalidSendableException("Cannot process SendableRequest of type " + request.getRequestType(), request);
			}
		} catch (InvalidSendableException e) {
			LOG.error("No InvalidSendableException should have occured in this environment!", e);
			throw e;
		} catch (NoEntityAvailableException e) {
			throw e;
		}

	}
	
	public void manageReceiveProfile(SendableProfile sendableProfile) {
		profileManager.saveOrUpdateEntity(sendableProfile.getProfile());
	}
	
	public void manageResponse(SendableMessageResponse messageResponse) throws InvalidSendableException, UserNotFoundException {
		UUID recipientUUID = messageResponse.getRecipient();
		if (recipientUUID == null)
			throw new InvalidSendableException("Response does not contain a recipient UUID", messageResponse);

		User recipient = get(recipientUUID);
		if (recipient == null)
			throw new UserNotFoundException("User with UUID " + recipientUUID + "not found");

		//only for FRIEND_REQUEST_RESPONSE
		if (messageResponse.getType() == ESendableType.FRIEND_REQUEST_RESPONSE) {
			SendableFriendRequestResponse friendRequestResponse = (SendableFriendRequestResponse) messageResponse;

			if (friendRequestResponse.isAccepted()) {
				//save new contacts for both users

				UUID senderUUID = friendRequestResponse.getSender();
				if (senderUUID == null)
					throw new InvalidSendableException("Response does not contain a sender UUID", messageResponse);

				Contacts recipientContacts = contactsManager.get(recipientUUID);
				if (recipientContacts == null)
					recipientContacts = initContactsForUser(recipientUUID);
				recipientContacts.addContacts(senderUUID);
				recipientContacts.updateLastUpdated();
				contactsManager.saveOrUpdateEntity(recipientContacts);

				Contacts senderContacts = contactsManager.get(senderUUID);
				if (senderContacts == null)
					senderContacts = initContactsForUser(senderUUID);
				senderContacts.addContacts(recipientUUID);
				senderContacts.updateLastUpdated();
				contactsManager.saveOrUpdateEntity(senderContacts);

			}
		}

		recipient.enqueueSendable(messageResponse);
		save(recipient);
	}
	
	/**
	 * @return the profileManager
	 */
	public ProfileManager getProfileManager() {
		return profileManager;
	}
	
	/**
	 * @return the settingsManager
	 */
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	/**
	 * @return the contactsManager
	 */
	public ContactsManager getContactsManager() {
		return contactsManager;
	}
	
	/**
	 * @return the messageManager
	 */
	public MessageManager getMessageManager() {
		return messageManager;
	}
	
}
