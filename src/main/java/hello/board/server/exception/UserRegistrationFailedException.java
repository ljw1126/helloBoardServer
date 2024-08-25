package hello.board.server.exception;

public class UserRegistrationFailedException extends RuntimeException {

    public UserRegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
