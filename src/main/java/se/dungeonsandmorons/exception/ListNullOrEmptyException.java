package se.dungeonsandmorons.exception;

public class ListNullOrEmptyException extends RuntimeException {
    public ListNullOrEmptyException(String message) {
        super(message);
    }
}
