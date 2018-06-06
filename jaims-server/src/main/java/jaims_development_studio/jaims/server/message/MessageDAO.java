package jaims_development_studio.jaims.server.message;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jaims_development_studio.jaims.api.message.Message;
import jaims_development_studio.jaims.api.util.HibernateUtil;
import jaims_development_studio.jaims.server.util.DAO;


/**
 * @author WilliGross
 */
public class MessageDAO extends DAO<Message> {
	
	public MessageDAO() {
		super(Message.class);
	}

	@SuppressWarnings("static-method")
	public void save(Message entity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.save(entity);
			transaction.commit();
		}
	}

}
