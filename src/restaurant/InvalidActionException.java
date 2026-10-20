package restaurant;

/**
 * Exception thrown when an invalid player action or staff command is attempted.
 * Preserves the reason for failure so the manager can be informed and retry
 * without consuming the staff action for the turn.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(String message) {
        super(message);
    }
}
