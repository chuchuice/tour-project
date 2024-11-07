package miit.chuice.tour.exceptions;

public class BadCredentialsError extends NotCorrectCredentialsError {

    public BadCredentialsError() {
        super("Некорректные данные для аутентификации или регистрации!" +
                " Длина пароля и логина должна быть строго больше 4!");
    }

    public BadCredentialsError(String s) {
        super(s);
    }

}
