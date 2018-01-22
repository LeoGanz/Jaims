package jaims_development_studio.jaims.server.profile;

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

import jaims_development_studio.jaims.api.profile.Profile;
import jaims_development_studio.jaims.api.util.HibernateUtil;

/**
 * This is the DAO (Data Access Object) for Profiles. It provides utility methods for accessing profiles in the
 * database.
 *
 * @author WilliGross
 */
@Transactional
public class ProfileDAO {

	@SuppressWarnings("static-method")
	public void saveOrUpdate(Profile profile) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(profile);
			transaction.commit();
		}
	}

	@SuppressWarnings("static-method")
	public void delete(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaDelete<Profile> criteriaDelete = criteriaBuilder.createCriteriaDelete(Profile.class);
			Root<Profile> root = criteriaDelete.from(Profile.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaDelete.where(condition);
			TypedQuery<Profile> query = session.createQuery(criteriaDelete);
			query.executeUpdate();
		}
	}
	
	@SuppressWarnings("static-method")
	public List<Profile> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaQuery<Profile> criteriaQuery = session.getCriteriaBuilder().createQuery(Profile.class);
			criteriaQuery.from(Profile.class);
			List<Profile> users = session.createQuery(criteriaQuery).getResultList();
			return users;
		}
	}

	@SuppressWarnings("static-method")
	public Profile get(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Profile> criteriaQuery = criteriaBuilder.createQuery(Profile.class);
			Root<Profile> root = criteriaQuery.from(Profile.class);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<Profile> query = session.createQuery(criteriaQuery);
			List<Profile> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}

}
