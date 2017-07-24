package jaims_development_studio.jaims.server.account;

import java.util.List;
import java.util.UUID;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;

import jaims_development_studio.jaims.server.util.HibernateUtil;

@SuppressWarnings("static-method")
@Transactional
public class AccountDAO {

	public void saveOrUpdate(Account account) throws UserNameNotAvailableException {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.saveOrUpdate(account);
			session.getTransaction().commit();
		} catch (@SuppressWarnings("unused") PersistenceException e) {
			throw new UserNameNotAvailableException(account.getUsername());
		}
	}

	public void delete(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaDelete<Account> criteriaDelete = criteriaBuilder.createCriteriaDelete(Account.class);
			Root<Account> root = criteriaDelete.from(Account.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaDelete.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaDelete);
			query.executeUpdate();
		}
	}

	public void delete(String username) {
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
	
	public List<Account> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaQuery<Account> criteriaQuery = session.getCriteriaBuilder().createQuery(Account.class);
			criteriaQuery.from(Account.class);
			List<Account> accounts = session.createQuery(criteriaQuery).getResultList();
			return accounts;
		}
	}
	
	public Account get(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaQuery);
			List<Account> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}

	public Account get(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			criteriaQuery.select(root);
			Predicate condition = criteriaBuilder.equal(root.<Path<String>> get("username"), username);
			criteriaQuery.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaQuery);
			List<Account> result = query.getResultList();
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
	
	public boolean isUuidRegistered(UUID uuid) {
		if (uuid == null) {
			System.out.println("isUuidRegistered: uuid is null");
			return false;
		}
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<Account> root = criteriaQuery.from(Account.class);
			Expression<Long> countExpression = criteriaBuilder.count(root);
			criteriaQuery.select(countExpression);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<Long> query = session.createQuery(criteriaQuery);
			Long count = query.getSingleResult();
			return count == 0 ? false : true;
		}
	}
	
}
