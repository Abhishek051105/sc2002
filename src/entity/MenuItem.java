package entity;

/** Immutable dish: name, price in cents, prep units. */
public class MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public MenuItem(String name, int priceCents, int prepUnits) {
        throw new UnsupportedOperationException("TODO");
    }

    public String getName() { throw new UnsupportedOperationException("TODO"); }
    public int getPriceCents() { throw new UnsupportedOperationException("TODO"); }
    public int getPrepUnits() { throw new UnsupportedOperationException("TODO"); }
}
