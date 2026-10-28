package entity;

/**
 * Shared "one task per turn" rule. Each role checks its own rules first
 * and calls markActed() only when the action succeeds.
 */
public abstract class Staff {
    private final String roleName;
    private boolean actedThisTurn;

    protected Staff(String roleName) {
        this.roleName = roleName;
        this.actedThisTurn = false;
    }

    public void startNewTurn() {
        actedThisTurn = false;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    public String getRoleName() {
        return roleName;
    }

    /** Throws "Waiter has already acted this turn" etc. */
    protected void checkCanAct() throws InvalidActionException {
        if (actedThisTurn) {
            throw new InvalidActionException(roleName + " has already acted this turn");
        }
    }

    protected void markActed() {
        actedThisTurn = true;
    }
}
