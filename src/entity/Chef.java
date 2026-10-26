package entity;

public class Chef extends Staff {
    public Chef() {
        super("Chef");
    }

    /** Cooks one unit. o must exist and be IN_PREPARATION. */
    public void prepare(Order o) throws InvalidActionException { throw new UnsupportedOperationException("TODO"); }
}
