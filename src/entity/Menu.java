package entity;

import java.util.List;

/** The menu. Item ORDER matters: the food-choice rule indexes into it. */
public class Menu {
    private final List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Salad $9.00 (1 unit), Burger $15.00 (1), Pasta $18.00 (2), in that order. */
    public static Menu createDefault() { throw new UnsupportedOperationException("TODO"); }

    /** Unmodifiable view. */
    public List<MenuItem> getItems() { throw new UnsupportedOperationException("TODO"); }
    public MenuItem getItem(int index) { throw new UnsupportedOperationException("TODO"); }
    public int size() { throw new UnsupportedOperationException("TODO"); }
}
