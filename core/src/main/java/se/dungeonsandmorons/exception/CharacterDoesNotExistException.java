package se.dungeonsandmorons.exception;

public class CharacterDoesNotExistException extends RuntimeException {
    public CharacterDoesNotExistException(String message) {
        super(message);
    }
}
