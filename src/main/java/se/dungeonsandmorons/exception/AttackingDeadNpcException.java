package se.dungeonsandmorons.exception;

public class AttackingDeadNpcException extends RuntimeException {
    public AttackingDeadNpcException() {
        super("Target is already dead");
    }
}
