package miit.chuice.tour.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import miit.chuice.tour.security.SignUp;
import miit.chuice.tour.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class.getSimpleName());

    @FXML private TextField name;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!login.getText().trim().isEmpty() &&!password.getText().trim().isEmpty()){
                    SignUp.signUp(event, name.getText(), login.getText(), password.getText());
                } else{
                    logger.error("Не указана вся информация для регистрации");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Вы не указали всю информацию для регистрации :(");
                    alert.show();
                }
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "/miit/chuice/tour/login.fxml", null, null, null);
            }
        });
    }
}
