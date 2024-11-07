package miit.chuice.tour.exceptions;

public class EmptyCredentialsError extends NotCorrectCredentialsError {

    public EmptyCredentialsError() {
        super("Пустые данные для аутентификации :(");
    }

    public EmptyCredentialsError(String s) {
        super(s);
    }

}