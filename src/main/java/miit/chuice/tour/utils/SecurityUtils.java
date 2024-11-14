package miit.chuice.tour.utils;

import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import miit.chuice.tour.exceptions.BadCredentialsError;
import miit.chuice.tour.exceptions.EmptyCredentialsError;


@Slf4j
public class SecurityUtils {

    public static boolean isPasswordAndLoginCorrect(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            log.error("Данные аутентификации или регистрации не были переданы пользователем: {}", EmptyCredentialsError.class);
            makeAlert("Пожалуйста, введите логин и пароль!", Alert.AlertType.ERROR);
            throw new EmptyCredentialsError();
        }

        if (password.length() <= 4 || login.length() <= 4) {
            log.error("Некорректные данные для аутентификации или регистрации");
            makeAlert("Длина логина и пароля должна быть строго больше 4", Alert.AlertType.ERROR);
            throw new BadCredentialsError();
        }

        return true;
    }

    public static boolean isUserDataCorrect(String name, String surname, String patronymic, String email, String login, String password, boolean objectHasPatronymic) {
        if (name.trim().isEmpty()  || surname.trim().isEmpty() || (patronymic.trim().isEmpty() && !objectHasPatronymic) ||
                !isPasswordAndLoginCorrect(login, password) || !isEmailValid(email)) {

            if (!isEmailValid(email))
                makeAlert("Указана некорректная почта!", Alert.AlertType.ERROR);

            log.error("Переданы неверные данные для регистрации {}", BadCredentialsError.class);
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

    public static boolean isEmailValid(String email) {
        return email.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}\\.[a-z]{2,}$");
    }
}
