package entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private ArrayList<MenuItem> items = new ArrayList<>();
    
    public Menu(){
        // adding items into the menu

        MenuItem salad = new MenuItem("Salad",900,1);
        items.add(salad);

        MenuItem burger = new MenuItem("Burger", 1500, 1);
        items.add(burger);

        MenuItem pasta = new MenuItem("Pasta",1800,2);
        items.add(pasta);
    }

    /** Salad $9.00 (1 unit), Burger $15.00 (1), Pasta $18.00 (2), in that order. */
    public static Menu createDefault() {
        return new Menu();
    }

    /** Unmodifiable view. */
    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public MenuItem getItem(int index){
       return items.get(index); 
    }
    public int size(){
        return items.size();
    }
}
