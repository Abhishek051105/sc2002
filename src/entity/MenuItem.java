package entity;

public class MenuItem {
    private final int priceCents;
    private final String name;
    private final int prepUnits;

    public MenuItem( String name,int priceCents, int prepUnits){
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (priceCents <= 0 ){
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (prepUnits < 1 ){
            throw new IllegalArgumentException("Prep units must be greater than 0");
        }
        this.priceCents = priceCents;
        this.name = name;
        this.prepUnits = prepUnits;
    }
    
    public int getPriceCents (){
        return this.priceCents;
    }

    public String getName(){
        return this.name;
    }

    public int getPrepUnits(){
        return this.prepUnits;
    }

}

