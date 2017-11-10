package jaims_development_studio.jaims.server.user;

import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jaims_development_studio.jaims.api.account.Account;
import jaims_development_studio.jaims.api.user.User;
import jaims_development_studio.jaims.api.util.HibernateUtil;

@Transactional
public class UserDAO {
	
	@SuppressWarnings("static-method")
	public void saveOrUpdate(User user) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			transaction.commit();
		}
	}

	@SuppressWarnings("static-method")
	public void delete(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaDelete<User> criteriaDelete = criteriaBuilder.createCriteriaDelete(User.class);
			Root<User> root = criteriaDelete.from(User.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaDelete.where(condition);
			TypedQuery<Account> query = session.createQuery(criteriaDelete);
			query.executeUpdate();
		}
	}

	//	public void delete(String username) {
	//		Account account = (new AccountDAO()).get(username);
	//		delete(account.getUuid());
	//	}

	@SuppressWarnings("static-method")
	public List<User> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
			criteriaQuery.from(User.class);
			List<User> users = session.createQuery(criteriaQuery).getResultList();
			return users;
		}
	}

	@SuppressWarnings("static-method")
	public User get(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
			Root<User> root = criteriaQuery.from(User.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<User> query = session.createQuery(criteriaQuery);
			List<User> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
}
