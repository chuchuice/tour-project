package miit.chuice.tour.utils;

import javafx.scene.control.Alert;
import miit.chuice.tour.exceptions.BadCredentialsError;
import miit.chuice.tour.exceptions.EmptyCredentialsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class.getSimpleName());

    public static boolean isPasswordAndLoginCorrect(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            logger.error("Данные аутентификации или регистрации не были переданы пользователем: {}", EmptyCredentialsError.class);
            makeAlert("Пожалуйста, введите логин и пароль!", Alert.AlertType.ERROR);
            throw new EmptyCredentialsError();
        }

        if (password.length() < 4 || login.length() < 4) {
            logger.error("Некорректные данные для аутентификации или регистрации");
            makeAlert("Длина логина и пароля должна быть строго больше 4", Alert.AlertType.ERROR);
            throw new BadCredentialsError();
        }

        return true;
    }

    public static boolean isPasswordAndLoginAndNameCorrect(String name, String login, String password) {
        if (name.trim().isEmpty() || !isPasswordAndLoginCorrect(login, password)) {
            logger.error("Переданы неверные данные для регистрации");
            makeAlert("Вы не указали всю информацию для регистрации :(", Alert.AlertType.ERROR);
            throw new BadCredentialsError();
        }

        return true;
    }

    public static void makeAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
