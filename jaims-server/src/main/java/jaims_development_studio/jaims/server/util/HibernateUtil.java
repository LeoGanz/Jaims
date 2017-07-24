package jaims_development_studio.jaims.server.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	private static final SessionFactory SESSION_FACTORY;

	static {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		Metadata metadata = new MetadataSources(registry).buildMetadata();
		SESSION_FACTORY = metadata.buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}
	
}
