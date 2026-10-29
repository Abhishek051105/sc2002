package entity;
import java.util.ArrayList;

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

    public MenuItem getItem(int index){
       return items.get(index); 
    }
    public int size(){
        return items.size();
    }
}
