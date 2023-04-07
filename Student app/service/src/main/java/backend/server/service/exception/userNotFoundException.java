package backend.server.service.exception;

public class userNotFoundException extends RuntimeException {
    public userNotFoundException(String s) {
        super(s);
    }
}
