package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import miit.chuice.tour.bcrypt.BCrypt;
import miit.chuice.tour.dao.PersonDAO;
import miit.chuice.tour.enums.Role;
import miit.chuice.tour.exceptions.NotCorrectCredentialsError;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static miit.chuice.tour.utils.Utils.changeScene;

public class Login {

    private final static Logger logger = LoggerFactory.getLogger(PersonDAO.class.getSimpleName());

    public static void login(ActionEvent event, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Human human = new PersonDAO().show(login);

        if (human == null) {
            logger.error("{} не был найден в базе данных!", login);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(login + " не был найден в базе данных!");
            alert.show();
            throw new NotCorrectCredentialsError("Неправильно введены данные!");
        }

        Role role = human.getRole();
        String pswrd = human.getPassword();

        if (BCrypt.equals(password, pswrd)) {
            switch (role) {
                case USER -> changeScene(event, "/miit/chuice/tour/user-logged-in.fxml", "user", login, role);
                case ADMIN -> changeScene(event, "/miit/chuice/tour/admin-logged-in.fxml", "admin", login, role);
                case PORTER -> changeScene(event, "/miit/chuice/tour/porter-logged-in.fxml", "porter", login, role);
                case null -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("У вас нет права пользоваться программой :(");
                    alert.show();
                }
            }
        } else {
            logger.error("Введён неправильный пароль!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Неправильный пароль!");
            alert.show();
        }

    }

}