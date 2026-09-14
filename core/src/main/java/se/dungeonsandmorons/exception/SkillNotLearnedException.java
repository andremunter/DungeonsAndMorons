package se.dungeonsandmorons.exception;

public class SkillNotLearnedException extends RuntimeException {
    public SkillNotLearnedException(String message) {
        super(message);
    }
}
