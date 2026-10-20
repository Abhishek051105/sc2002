package entity;

/**
 * Immutable descriptor of a dish on the restaurant menu.
 * Price is stored in integer cents to eliminate floating-point rounding errors.
 */
public class MenuItem {
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public MenuItem(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }

    @Override
    public String toString() {
        return String.format("%s ($%.2f, %d unit%s)", name, priceCents / 100.0, prepUnits, prepUnits > 1 ? "s" : "");
    }
}
