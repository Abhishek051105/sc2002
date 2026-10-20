package entity;

/**
 * Custom checked exception carrying failure rationale when an invalid
 * manager action or staff command is attempted.
 * Thrown before staff action flags or domain states are modified,
 * allowing the controller to re-prompt the manager without penalty.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(String message) {
        super(message);
    }
}
