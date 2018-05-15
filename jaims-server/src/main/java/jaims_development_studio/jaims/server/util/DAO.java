package jaims_development_studio.jaims.server.util;

import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jaims_development_studio.jaims.api.util.HibernateUtil;
import jaims_development_studio.jaims.api.util.UuidEntity;

@Transactional
public class DAO<E extends UuidEntity> {

	private final Class<E> clazz;

	public DAO(Class<E> clazz) {
		this.clazz = clazz;
	}

	public void saveOrUpdate(E entity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(entity);
			transaction.commit();
		}
	}

	public void delete(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaDelete<E> criteriaDelete = criteriaBuilder.createCriteriaDelete(getClazz());
			Root<E> root = criteriaDelete.from(getClazz());
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaDelete.where(condition);
			TypedQuery<E> query = session.createQuery(criteriaDelete);
			query.executeUpdate();
		}
	}
	
	public E get(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getClazz());
			Root<E> root = criteriaQuery.from(getClazz());
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<E> query = session.createQuery(criteriaQuery);
			List<E> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
	
	public List<E> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaQuery<E> criteriaQuery = session.getCriteriaBuilder().createQuery(getClazz());
			criteriaQuery.from(getClazz());
			List<E> entities = session.createQuery(criteriaQuery).getResultList();
			return entities;
		}
	}
	
	public boolean isUuidRegistered(UUID uuid) {
		if (uuid == null)
			return false;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<E> root = criteriaQuery.from(getClazz());
			Expression<Long> countExpression = criteriaBuilder.count(root);
			criteriaQuery.select(countExpression);
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<Long> query = session.createQuery(criteriaQuery);
			Long count = query.getSingleResult();
			return count == 0 ? false : true;
		}
	}
	
	public Class<E> getClazz() {
		return clazz;
	}
	
}
