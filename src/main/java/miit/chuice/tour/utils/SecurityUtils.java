package miit.chuice.tour.utils;

import miit.chuice.tour.exceptions.BadCredentialsError;
import miit.chuice.tour.exceptions.EmptyCredentialsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class.getSimpleName());

    public static boolean isPasswordAndLoginCorrect(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            logger.error("Данные аутентификации или регистрации не были переданы пользователем: {}", EmptyCredentialsError.class);
            throw new EmptyCredentialsError();
        }

        if (password.length() < 4 || login.length() < 4) {
            logger.error("Некорректные данные для аутентификации или регистрации");
            throw new BadCredentialsError();
        }

        return true;
    }
}
