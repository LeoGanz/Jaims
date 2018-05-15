package jaims_development_studio.jaims.server.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import jaims_development_studio.jaims.api.util.HibernateUtil;
import jaims_development_studio.jaims.api.util.UpdateTrackingUuidEntity;

public class UpdateTrackingUUIDEntityDAO<E extends UpdateTrackingUuidEntity> extends DAO<E> {

	public UpdateTrackingUUIDEntityDAO(Class<E> clazz) {
		super(clazz);
	}
	
	public Date getLastUpdated(UUID uuid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<Date> criteriaQuery = criteriaBuilder.createQuery(Date.class);
			Root<E> root = criteriaQuery.from(getClazz());
			criteriaQuery.select(root.get("lastUpdated"));
			Predicate condition = criteriaBuilder.equal(root.get("uuid"), uuid);
			criteriaQuery.where(condition);
			TypedQuery<Date> query = session.createQuery(criteriaQuery);
			List<Date> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		}
	}
	
}
