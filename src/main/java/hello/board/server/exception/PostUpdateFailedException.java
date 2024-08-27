package hello.board.server.exception;

public class PostUpdateFailedException extends RuntimeException {

    public PostUpdateFailedException(String message) {
        super(message);
    }
}
