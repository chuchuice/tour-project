package miit.chuice.tour;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class MakeHibernateSession {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Class<?> clazz) {
        try {
            sessionFactory = new Configuration().addAnnotatedClass(clazz).buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed");
           throw new ExceptionInInitializerError(ex);
        }
        return sessionFactory;
    }

}