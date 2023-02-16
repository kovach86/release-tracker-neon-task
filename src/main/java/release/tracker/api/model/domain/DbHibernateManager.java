package release.tracker.api.model.domain;

import release.tracker.api.utilities.PropertiesLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DbHibernateManager {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            if (sessionFactory == null) {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                var dbProperties = PropertiesLoader.getDbConfiguration();
                registryBuilder.applySettings(dbProperties);
                StandardServiceRegistry standardServiceRegistry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(standardServiceRegistry)
                        .addAnnotatedClass(Release.class);
                Metadata metaData = sources.getMetadataBuilder().build();

                sessionFactory = metaData.getSessionFactoryBuilder().build();
            }
            return sessionFactory;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
