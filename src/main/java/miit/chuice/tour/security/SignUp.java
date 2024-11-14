package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import lombok.Getter;
import miit.chuice.tour.security.bcrypt.BCrypt;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.services.HumanService;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;


@Component
public class SignUp {

    private static final Logger logger = LoggerFactory.getLogger(SignUp.class.getSimpleName());

    private final HumanService humanService;
    @Getter private final Utils utils;

    @Autowired
    public SignUp(HumanService humanService, Utils utils) {
        this.humanService = humanService;
        this.utils = utils;
    }

    public void signUp(ActionEvent event, String name, String surname, String patronymic, String email, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Human human;

        if ((human = humanService.findHumanByEmail(email)) != null) {
            makeAlert("Пользователь с такой электронной почтой уже зарегистрирован в нашей системе", Alert.AlertType.ERROR);
        } else {
            human = humanService.findHumanByLogin(login);
        }

        if (human != null) {
            logger.error("Пользователь пытается зарегистрироваться, но человек с таким логином уже существует");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Пользователь с таким логином уже существует");
            alert.show();
        } else {
            humanService.save(new Human(name, surname, patronymic, email, login, BCrypt.hashPassword(password)));
        }

        utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "login");
    }

}