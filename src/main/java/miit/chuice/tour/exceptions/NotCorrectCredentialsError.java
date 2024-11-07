package miit.chuice.tour.exceptions;

public class NotCorrectCredentialsError extends IllegalArgumentException {

    public NotCorrectCredentialsError() {
        super("Неправильные данные для аутентификации!");
    }

    public NotCorrectCredentialsError(String s) {
        super(s);
    }

}