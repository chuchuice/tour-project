package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import miit.chuice.tour.controllers.UserController;
import miit.chuice.tour.security.bcrypt.BCrypt;
import miit.chuice.tour.exceptions.NotCorrectCredentialsError;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.services.HumanService;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Login {

    private final static Logger logger = LoggerFactory.getLogger(Login.class.getSimpleName());

    private final HumanService humanService;
    private final Utils utils;

    @Autowired
    public Login(HumanService humanService, Utils utils) {
        this.humanService = humanService;
        this.utils = utils;
    }

    public void login(ActionEvent event, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Human human = humanService.findHumanByLogin(login);

        if (human == null) {
            logger.error("{} не был найден в базе данных!", login);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(login + " не был найден в базе данных!");
            alert.show();
            throw new NotCorrectCredentialsError("Неправильно введены данные!");
        }

        Human.Role role = human.getRole();
        String userPassword = human.getPassword();

        UserController.setTheUser(human);

        if (BCrypt.equals(password, userPassword)) {
            switch (role) {
                case USER -> utils.changeScene(event, "/miit/chuice/tour/views/user-hotels.fxml", "Пользователь");
                case ADMIN -> utils.changeScene(event, "/miit/chuice/tour/views/admin.fxml", "Администратор");
                case PORTER -> utils.changeScene(event, "/miit/chuice/tour/views/porter.fxml", "Сотрудник отеля");
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