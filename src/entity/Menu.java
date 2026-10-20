package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry of available dishes offered by the restaurant.
 * Stage 1 default menu order: Salad ($9.00, 1 unit), Burger ($15.00, 1 unit), Pasta ($18.00, 2 units).
 */
public class Menu {
    private final List<MenuItem> items;

    public Menu() {
        this.items = new ArrayList<>();
    }

    public static Menu createDefault() {
        Menu menu = new Menu();
        // Mandatory order: Salad, Burger, Pasta (required for customer food choice rule)
        menu.addItem(new MenuItem("Salad", 900, 1));
        menu.addItem(new MenuItem("Burger", 1500, 1));
        menu.addItem(new MenuItem("Pasta", 1800, 2));
        return menu;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public MenuItem getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public MenuItem getItem(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public int size() {
        return items.size();
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
