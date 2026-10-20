package restaurant;

/**
 * Base abstract class representing a restaurant staff member.
 * Manages turn-based action accounting, ensuring staff complete at most
 * one designated task per turn.
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
     * Resets action flag at the start of a new turn.
     */
    public void startNewTurn() {
        this.actedThisTurn = false;
    }

    /**
     * Returns whether this staff member has already acted in the current turn.
     */
    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    /**
     * Marks that the staff member has acted in the current turn.
     */
    public void markActed() {
        this.actedThisTurn = true;
    }

    /**
     * Validates whether the staff member can perform an action in this turn.
     * Throws an InvalidActionException if the staff member has already acted.
     *
     * @throws InvalidActionException if already acted this turn
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
