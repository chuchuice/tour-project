package miit.chuice.tour.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import miit.chuice.tour.security.SignUp;
import miit.chuice.tour.utils.SecurityUtils;
import miit.chuice.tour.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class SignUpController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class.getSimpleName());

    @FXML private TextField name;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;

    private SignUp signUp;

    public SignUpController() {}

    @Autowired
    public SignUpController(SignUp signUp) {
        this.signUp = signUp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpButton.setOnAction(event -> {
            if (SecurityUtils.isPasswordAndLoginAndNameCorrect(name.getText(), login.getText(), password.getText()))
                signUp.signUp(event, name.getText(), login.getText(), password.getText());
        });

        loginButton.setOnAction(event ->
                Utils.changeScene(event, "/miit/chuice/tour/views/login.fxml", null, null, null));
    }

}
