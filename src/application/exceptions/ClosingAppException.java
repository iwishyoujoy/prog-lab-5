package application.exceptions;

public class ClosingAppException extends RuntimeException{
    public ClosingAppException() {
        super("Application has been closed!");
    }

    public ClosingAppException(String cause) {
        super(String.format("Application has been closed! Cause: %s", cause));
    }
}
