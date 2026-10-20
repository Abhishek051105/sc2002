package restaurant;

/**
 * Represents a menu item available at the restaurant.
 */
public class MenuItem {
    private final String name;
    private final double price;
    private final int preparationUnits;

    public MenuItem(String name, double price, int preparationUnits) {
        this.name = name;
        this.price = price;
        this.preparationUnits = preparationUnits;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPreparationUnits() {
        return preparationUnits;
    }

    @Override
    public String toString() {
        return String.format("%s ($%.2f, %d prep units)", name, price, preparationUnits);
    }
}
