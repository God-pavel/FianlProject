package springBoot.exception;

public class NotEnoughProductsException extends RuntimeException {
    public NotEnoughProductsException(String message) {
        super(message);
    }
}
