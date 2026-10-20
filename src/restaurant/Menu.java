package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the list of available items offered by the restaurant.
 */
public class Menu {
    private final List<MenuItem> items;

    public Menu() {
        this.items = new ArrayList<>();
        // Default Stage 1 menu items according to project specification
        items.add(new MenuItem("Burger", 15.00, 2));
        items.add(new MenuItem("Pasta", 18.00, 2));
        items.add(new MenuItem("Salad", 9.00, 1));
    }

    public Menu(List<MenuItem> customItems) {
        this.items = new ArrayList<>(customItems);
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public MenuItem getItem(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public MenuItem getItemByIndex(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public void displayMenu() {
        System.out.println("=== Restaurant Menu ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }
}
