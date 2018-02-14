package jaims_development_studio.jaims.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jaims_development_studio.jaims.api.util.UuidEntity;

public class EntityManager<E extends UuidEntity> {
	
	private final Map<UUID, E>	loadedEntities;
	private final DAO<E>		dao;
	
	public EntityManager(DAO<E> dao) {
		this.dao = dao;
		loadedEntities = new HashMap<>();
	}
	
	public void save(E entity) {
		if (entity != null) {
			unload(entity);
			getDao().saveOrUpdate(entity);
			load(entity);
		}
	}

	private void load(E entity) {
		if (entity != null)
			loadedEntities.put(entity.getUuid(), entity);
	}

	public void delete(E entity) {
		if (entity != null)
			delete(entity.getUuid());
	}

	public void delete(UUID uuid) {
		unload(uuid);
		getDao().delete(uuid);
	}
	
	private void unload(E entity) {
		if (entity != null)
			unload(entity.getUuid());
	}

	private void unload(UUID uuid) {
		loadedEntities.remove(uuid);
	}
	
	public E get(UUID uuid) {
		E result = loadedEntities.get(uuid);
		if (result == null) {
			result = getDao().get(uuid);
			if (result != null)
				loadedEntities.put(uuid, result);
		}
		return result;
	}

	public boolean isUuidRegistered(UUID uuid) {
		return getDao().isUuidRegistered(uuid);
	}

	protected DAO<E> getDao() {
		return dao;
	}
}
