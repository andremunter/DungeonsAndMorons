package se.dungeonsandmorons.exception;

public class CorruptedSaveException extends RuntimeException {
    public CorruptedSaveException(String message) {
        super(message);
    }

    public CorruptedSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
