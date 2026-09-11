package se.dungeonsandmorons.exception;

public class AttackingDeadTargetException extends RuntimeException {
    public AttackingDeadTargetException() {
        super("Target is already dead");
    }
}
