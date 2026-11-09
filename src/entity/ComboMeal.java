package entity;

// a combo is 2 existing menu items sold together, so it is still a MenuItem
public class ComboMeal extends MenuItem {
    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second){
        // name, price and prep units all come from the 2 items
        // price = 90% of both added up, the +50 makes it round to the nearest cent
        super(first.getName() + " + " + second.getName(),
              ((first.getPriceCents() + second.getPriceCents()) * 90 + 50) / 100,
              first.getPrepUnits() + second.getPrepUnits());
        this.first = first;
        this.second = second;
    }

    public MenuItem getFirst(){
        return this.first;
    }

    public MenuItem getSecond(){
        return this.second;
    }

}
