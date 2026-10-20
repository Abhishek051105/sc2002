package entity;

/**
 * Concrete entity representing a Regular customer.
 * Loses 8 satisfaction points per waiting turn and pays full price.
 */
public class RegularCustomer extends Customer {

    public RegularCustomer(int id) {
        super(id);
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return 8;
    }
}
