package miit.chuice.tour.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import miit.chuice.tour.security.Login;
import miit.chuice.tour.utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

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
                Login.login(event, login.getText(), password.getText());
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "/miit/chuice/tour/sign-up.fxml", null, null, null);
            }
        });
    }
}