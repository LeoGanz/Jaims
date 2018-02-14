package jaims_development_studio.jaims.server.message;

import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.server.util.DAO;


/**
 * @author WilliGross
 */
public class MessageDAO extends DAO<Message> {

	public MessageDAO() {
		super(Message.class);
	}
	
}
