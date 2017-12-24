package jaims_development_studio.jaims.server.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.util.ListChangeEvent;
import jaims_development_studio.jaims.api.util.ListChangeListener;
import jaims_development_studio.jaims.api.util.ObservableList;
import jaims_development_studio.jaims.server.Server;
import jaims_development_studio.jaims.server.network.ClientManager;
import jaims_development_studio.jaims.server.network.NetworkSystem;
import jaims_development_studio.jaims.server.user.UserManager;

public class UserListComponent extends JList<String> {

	private static final long				serialVersionUID	= 1L;
	private static Logger					LOG					= LoggerFactory.getLogger(UserListComponent.class);
	private final DefaultListModel<String>	listModel;
	private ObservableList<User>			onlineUsers;

	@SuppressWarnings("resource")
	public UserListComponent(Server server) {
		LOG.debug("Initializing UserListComponent");
		listModel = new DefaultListModel<>();
		setModel(listModel);

		synchronized (server) {
			try {
				while (server.isServerStarting())
					server.wait();
			} catch (InterruptedException e) {
				LOG.error("Interruption during UserList initialization", e);
			}
		}

		NetworkSystem networkSystem = server.getNetworkSystem();
		ClientManager clientManager = networkSystem.getClientManager();

		if (clientManager == null)
			return;
		
		UserManager userManager = clientManager.getUserManager();

		List<User> users = userManager.getOnlineUsers();
		if (users.getClass() == ObservableList.class) {
			onlineUsers = (ObservableList<User>) users;
			onlineUsers.addListChangeListener(new ListChangeListener() {

				@Override
				public void emlementAdded(ListChangeEvent event) {
					LOG.debug("element added to user list");
					Object source = event.getSource();
					if (source.getClass() == User.class) {
						User element = (User) source;
						listModel.addElement(element.getName());
					} else
						LOG.warn("Cannot add element to user list: Invalid ListChangeEvent");
				}

				@Override
				public void elementRemoved(ListChangeEvent event) {
					LOG.debug("element removed from user list");
					Object source = event.getSource();
					if (source.getClass() == User.class) {
						User element = (User) source;
						listModel.removeElement(element.getName());
					} else
						LOG.warn("Cannot remove element from user list: Invalid ListChangeEvent");
				}
			});
		} else
			LOG.error("Client list is no observable list!");
	}

	public void updateList() {
		for (User u : onlineUsers)
			listModel.addElement(u.getName());
	}

}
