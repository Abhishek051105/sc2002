package entity;

/**
 * Shared "one task per turn" rule. Each role exposes its own actions
 * (no fat interface). Validate first, call markActed() only on success.
 */
public abstract class Staff {
    private final String roleName;
    private boolean actedThisTurn;

    protected Staff(String roleName) {
        throw new UnsupportedOperationException("TODO");
    }

    public void startNewTurn() { throw new UnsupportedOperationException("TODO"); }
    public boolean hasActedThisTurn() { throw new UnsupportedOperationException("TODO"); }
    public String getRoleName() { throw new UnsupportedOperationException("TODO"); }

    /** Throws "Waiter has already acted this turn" etc. */
    protected void checkCanAct() throws InvalidActionException { throw new UnsupportedOperationException("TODO"); }
    protected void markActed() { throw new UnsupportedOperationException("TODO"); }
}
