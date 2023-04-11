package backend.server.service.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String s) {
        super(s);
    }
}
