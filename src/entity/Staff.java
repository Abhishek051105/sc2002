package entity;

/**
 * Shared "one task per turn" rule. Each role exposes its own actions
 * (no fat interface). Validate first, call markActed() only on success.
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
