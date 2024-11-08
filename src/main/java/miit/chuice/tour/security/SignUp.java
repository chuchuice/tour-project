package miit.chuice.tour.security;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import miit.chuice.tour.bcrypt.BCrypt;
import miit.chuice.tour.bcrypt.BCryptAPI;
import miit.chuice.tour.models.Human;
import miit.chuice.tour.services.HumanService;
import miit.chuice.tour.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static miit.chuice.tour.utils.Utils.changeScene;

@Component
public class SignUp {

    private static final Logger logger = LoggerFactory.getLogger(SignUp.class.getSimpleName());

    private final HumanService humanService;
    private final BCryptAPI bCrypt;

    @Autowired
    public SignUp(HumanService humanService, @Qualifier(value = "BCrypt") BCryptAPI bCrypt) {
        this.humanService = humanService;
        this.bCrypt = bCrypt;
    }

    public void signUp(ActionEvent event, String name, String login, String password) {

        SecurityUtils.isPasswordAndLoginCorrect(login, password);

        Human human = humanService.findHumanByLogin(login);

        if (human != null) {
            logger.error("Пользователь пытается зарегистрироваться, но человек с таким логином уже существует");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Пользователь с таким логином уже существует");
            alert.show();
        } else {
            humanService.save(new Human(name, login, bCrypt.hashPassword(password)));
        }

        changeScene(event, "/miit/chuice/tour/login.fxml", null, null, null);
    }

}