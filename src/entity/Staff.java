package entity;

/**
 * Base abstract entity representing a restaurant staff member.
 * Enforces the shared turn-action constraint: a staff member completes
 * at most one task per turn.
 */
public abstract class Staff {
    protected final String name;
    protected final String role;
    protected boolean actedThisTurn;

    public Staff(String name, String role) {
        this.name = name;
        this.role = role != null ? role : "Staff";
        this.actedThisTurn = false;
    }

    public Staff(String role) {
        this(role, role);
    }

    /**
     * Resets action quota at the start of a new turn.
     */
    public void startNewTurn() {
        this.actedThisTurn = false;
    }

    /**
     * Checks if the staff member has already completed a task this turn.
     */
    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    /**
     * Marks the staff member's turn action as completed.
     */
    public void markActed() {
        this.actedThisTurn = true;
    }

    /**
     * Validates that the staff member is available to act.
     *
     * @throws InvalidActionException if the staff member has already acted this turn
     */
    public void checkCanAct() throws InvalidActionException {
        if (actedThisTurn) {
            throw new InvalidActionException(role + " has already acted this turn");
        }
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
