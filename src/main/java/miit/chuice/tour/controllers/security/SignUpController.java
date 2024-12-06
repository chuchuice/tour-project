package miit.chuice.tour.controllers.security;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;
import miit.chuice.tour.security.SignUp;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static miit.chuice.tour.utils.SecurityUtils.makeAlert;

@Slf4j
@Component
public class SignUpController implements Initializable {

    @FXML public CheckBox noPatronymic;
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField patronymic;
    @FXML private TextField email;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML public PasswordField repeatPassword;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    private final SignUp signUp;
    private final Utils utils;

    @Autowired
    public SignUpController(SignUp signUp) {
        this.signUp = signUp;
        this.utils = signUp.getUtils();
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        signUpButton.setOnAction(event -> {

            if (!password.getText().equals(repeatPassword.getText())) {
                makeAlert("Пароль введён неверно", Alert.AlertType.ERROR);
            }

            if (SecurityUtils.isUserDataCorrect(name.getText(), surname.getText(), patronymic.getText(), email.getText(),
                    login.getText(), password.getText(), noPatronymic.isSelected()))

                signUp.signUp(event, name.getText(), surname.getText(), patronymic.getText(), email.getText(), login.getText(), password.getText());
        });

        loginButton.setOnAction(event ->
                utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", "login"));
    }

}