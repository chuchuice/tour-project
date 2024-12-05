package miit.chuice.tour.controllers.security;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import miit.chuice.tour.security.Login;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@Component
public class LoginController implements Initializable {

    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    private final Login loginLogic;
    private final Utils utils;

    @Autowired
    public LoginController(Login loginLogic, Utils utils) {
        this.loginLogic = loginLogic;
        this.utils = utils;
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(loginButton);
        System.out.println(signUpButton);

        loginButton.setOnAction(event -> {
            if (SecurityUtils.isPasswordAndLoginCorrect(password.getText(), login.getText())) {
                loginLogic.login(event, login.getText(), password.getText());
            }
        });

        signUpButton.setOnAction(event -> utils.changeScene(event, "/miit/chuice/tour/views/sign-up.fxml", "sign up"));
    }
}