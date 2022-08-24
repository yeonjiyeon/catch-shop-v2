package springboot.catchshop.exception;

public class CannotConvertNestedStructureException extends RuntimeException {
    public CannotConvertNestedStructureException(String message) {
        super(message);
    }
}
