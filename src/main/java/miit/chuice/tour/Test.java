package miit.chuice.tour;

import miit.chuice.tour.dao.PersonDAO;
import miit.chuice.tour.models.Human;
import org.hibernate.Session;

public class Test {
    public static void main(String[] args) {
        Session session = CreateHibernateSession
                .getSessionFactory(Human.class)
                .getCurrentSession();

        session.beginTransaction();

        System.out.println(PersonDAO.show("pavel").toString());

        session.getTransaction().commit();
    }
}
