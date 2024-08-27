package hello.board.server.exception;

public class PostDeleteFailedException extends RuntimeException {
    public PostDeleteFailedException(String message) {
        super(message);
    }
}
