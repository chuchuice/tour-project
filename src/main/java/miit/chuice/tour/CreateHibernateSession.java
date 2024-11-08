package miit.chuice.tour;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class CreateHibernateSession {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Class<?> clazz) {
        try {
            Configuration configuration = new Configuration();
            configuration.addProperties(loadProperties("/hibernate.properties"));

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(clazz)
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                    .build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed");
            throw new ExceptionInInitializerError(ex);
        }
        return sessionFactory;
    }

    private static Properties loadProperties(String path) {
        Properties properties = new Properties();
        try (InputStream inputStream = CreateHibernateSession.class.getResourceAsStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("Error loading properties file", e);
        }
        return properties;
    }
}
