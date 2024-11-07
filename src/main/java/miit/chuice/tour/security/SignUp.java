package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import miit.chuice.tour.MakeHibernateSession;
import miit.chuice.tour.dao.PersonDAO;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.utils.SecurityUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUp {

    private static final Logger logger = LoggerFactory.getLogger(SignUp.class.getSimpleName());

    public static void signUp(ActionEvent event, String name, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Session session = MakeHibernateSession.getSessionFactory(SignUp.class).getCurrentSession();

        session.beginTransaction();

        PersonDAO dao = new PersonDAO();

        Human human = dao.show(login);

        if (human == null) {
            logger.error("Пользователь пытается зарегистрироваться, но человек с таким логином уже существует");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Пользователь с таким логином уже существует");
            alert.show();
        } else {
            Human newHuman = new Human();

            dao.save(newHuman);
        }

        session.getTransaction().commit();
    }

}