package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import miit.chuice.tour.CreateHibernateSession;
import miit.chuice.tour.bcrypt.BCrypt;
import miit.chuice.tour.dao.PersonDAO;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.utils.SecurityUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static miit.chuice.tour.utils.Utils.changeScene;

public class SignUp {

    private static final Logger logger = LoggerFactory.getLogger(SignUp.class.getSimpleName());

    public static void signUp(ActionEvent event, String name, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Session session = CreateHibernateSession.getSessionFactory(Human.class).getCurrentSession();
        session.beginTransaction();

        Human human = PersonDAO.show(login);

        if (human == null) {
            logger.error("Пользователь пытается зарегистрироваться, но человек с таким логином уже существует");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Пользователь с таким логином уже существует");
            alert.show();
        } else {
            PersonDAO.save(new Human(name, login, BCrypt.hash(password)));
        }

        session.getTransaction().commit();
        changeScene(event, "/miit/chuice/tour/login.fxml", null, null, null);
    }

}