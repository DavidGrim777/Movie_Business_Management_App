package movie.business.app.exception;

public class DateTimeParseException extends Exception{
    public DateTimeParseException() {
    }

    public DateTimeParseException(String message) {
        super(message);
    }

    public DateTimeParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateTimeParseException(Throwable cause) {
        super(cause);
    }

    public DateTimeParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
