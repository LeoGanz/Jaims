package jaims_development_studio.jaims.server.account;

import java.util.List;
import java.util.UUID;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.account.UserNameNotAvailableException;
import jaims_development_studio.jaims.api.util.HibernateUtil;
import jaims_development_studio.jaims.server.util.DAO;
import jaims_development_studio.jaims.server.util.EntityManager;

/**
 * @author WilliGross
 */
@SuppressWarnings("static-method")
public class AccountDAO extends DAO<Account> {
	
	private final Logger				LOG	= LoggerFactory.getLogger(AccountDAO.class);
	private final EntityManager<?>[]	managers;
	
	public AccountDAO(EntityManager<?>... managers) {
		super(Account.class);
		this.managers = managers;
	}
	
	@Override
	public void saveOrUpdate(Account account) throws UserNameNotAvailableException {
		try {
			super.saveOrUpdate(account);
		} catch (Exception e) {
			if (e instanceof PersistenceException)
				throw new UserNameNotAvailableException(account.getUsername());
			LOG.error("Unexpected exception: ", e);
		}
	}
	
	private void cascadeDelete(UUID uuid) {
		for (EntityManager<?> manager : managers)
			manager.delete(uuid);
	}
	
	@Override
	public void delete(UUID uuid) {
		cascadeDelete(uuid);
		super.delete(uuid);
	}
	
	public void delete(String username) {
		cascadeDelete(getUUID(username));
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaDelete<Account> criteriaDelete = criteriaBuilder.createCriteriaDelete(Account.class);
			Root<Account> root = criteriaDelete.from(Account.class);
			Predicate condition = criteriaBuilder.equal(root.get("username"), username);
			criteriaDelete.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaDelete);
			query.executeUpdate();
		}
	}
	
	public List<String> getAllUsernames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root.get("username"));
			TypedQuery<String> query = session.createQuery(criteriaQuery);
			List<String> result = query.getResultList();
			return result;
			
		}
	}
	
	public Account get(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root);
			Predicate condition = criteriaBuilder.equal(root.get("username"), username); //<Path<String>> in front of get?
			criteriaQuery.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaQuery);
			List<Account> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
	
	public UUID getUUID(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<UUID> criteriaQuery = criteriaBuilder.createQuery(UUID.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root.get("uuid"));
			Predicate condition = criteriaBuilder.equal(root.get("username"), username);
			criteriaQuery.where(condition);
			TypedQuery<UUID> query = session.createQuery(criteriaQuery);
			List<UUID> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
	
	public String getUsername(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root.get("username"));
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<String> query = session.createQuery(criteriaQuery);
			List<String> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
	
	public boolean isUsernameAvailable(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			Expression<Long> countExpression = criteriaBuilder.count(root);
			criteriaQuery.select(countExpression);
			Predicate condition = criteriaBuilder.like(root.get("username"), username);
			criteriaQuery.where(condition);
			TypedQuery<Long> query = session.createQuery(criteriaQuery);
			Long count = query.getSingleResult();
			return count == 0 ? true : false;
		}
	}
	
}
