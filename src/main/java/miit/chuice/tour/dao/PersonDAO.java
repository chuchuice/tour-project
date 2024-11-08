package miit.chuice.tour.dao;


import miit.chuice.tour.CreateHibernateSession;
import miit.chuice.tour.models.Human;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonDAO {

    private final static Logger logger = LoggerFactory.getLogger(PersonDAO.class.getSimpleName());

    public static Human show(String login) {
        Session session = CreateHibernateSession.getSessionFactory(Human.class).getCurrentSession();
        session.beginTransaction();
        Human human = session
                .createQuery("FROM Human WHERE login = :login", Human.class)
                .setParameter("login", login)
                .getResultStream()
                .findFirst()
                .orElse(null);
        session.getTransaction().commit();

        return human;
    }

    public static void save(Human human) {
        Session session = CreateHibernateSession.getSessionFactory(Human.class).getCurrentSession();
        session.beginTransaction();
        session.persist(human);
        session.getTransaction().commit();
    }

}