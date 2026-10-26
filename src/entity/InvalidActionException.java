package entity;

/**
 * Thrown when the manager picks an action the rules don't allow
 * (e.g. "Table 2 is occupied"). Checked, so callers must handle it.
 * The staff member has NOT used their task when this is thrown.
 */
public class InvalidActionException extends Exception {
    public InvalidActionException(String reason) {
        super(reason);
    }
}
