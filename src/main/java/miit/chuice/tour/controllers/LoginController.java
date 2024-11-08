package miit.chuice.tour.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    @Autowired
    public LoginController(Login loginLogic) {
        this.loginLogic = loginLogic;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (SecurityUtils.isPasswordAndLoginCorrect(password.getText(), login.getText())) {
                    loginLogic.login(event, login.getText(), password.getText());
                }
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "/miit/chuice/tour/views/sign-up.fxml", null, null, null);
            }
        });
    }
}