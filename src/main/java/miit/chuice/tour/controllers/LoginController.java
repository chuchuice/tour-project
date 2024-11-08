package miit.chuice.tour.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import miit.chuice.tour.security.Login;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class LoginController implements Initializable {

    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!login.getText().trim().isEmpty() && !password.getText().trim().isEmpty()) {
                    Login.login(event, login.getText(), password.getText());
                }
                else {
                    log.error("Не указана вся информация для регистрации");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Вы не указали всю информацию для регистрации :(");
                    alert.show();
                    SecurityUtils.isPasswordAndLoginCorrect(password.getText(), login.getText());
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