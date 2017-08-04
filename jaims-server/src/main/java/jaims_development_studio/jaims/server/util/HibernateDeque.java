package jaims_development_studio.jaims.server.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import org.hibernate.HibernateException;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

public class HibernateDeque implements UserCollectionType {

	public HibernateDeque() {

	}
	
	@Override
	public PersistentCollection instantiate(SharedSessionContractImplementor session, CollectionPersister persister) throws HibernateException {
		return new PersistentBag(session);
	}
	
	@Override
	public PersistentCollection wrap(SharedSessionContractImplementor session, Object collection) {
		return new PersistentBag(session, (List<?>) collection);
	}
	
	@Override
	public Iterator getElementsIterator(Object collection) {
		return ((Collection<?>) collection).iterator();
	}
	
	@Override
	public boolean contains(Object collection, Object entity) {
		return ((Collection<?>) collection).contains(entity);
	}
	
	@Override
	public Object indexOf(Object collection, Object entity) {
		return ((List<?>) collection).indexOf(entity);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner, Map copyCache, SharedSessionContractImplementor session) throws HibernateException {
		Collection originalList = (Collection) original;
		Collection targetList = (Collection) target;
		targetList.clear();
		targetList.addAll(originalList);
		return targetList;
	}
	
	@Override
	public Object instantiate(int anticipatedSize) {
		return new LinkedBlockingDeque<>();
	}

}
