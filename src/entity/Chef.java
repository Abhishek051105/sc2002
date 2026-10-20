package entity;

/**
 * Represents the Chef in the restaurant kitchen.
 * Advances cooking progress by one preparation unit per turn for the selected order.
 */
public class Chef extends Staff {

    public Chef() {
        super("Chef", "Chef");
    }

    public Chef(String name) {
        super(name, "Chef");
    }

    /**
     * Cooks one preparation unit for the specified order.
     *
     * @param order the order to prepare
     * @throws InvalidActionException if chef already acted, order is null, or order is not IN_PREPARATION
     */
    public void prepare(Order order) throws InvalidActionException {
        checkCanAct();
        if (order == null) {
            throw new InvalidActionException("Order does not exist");
        }
        if (order.getStatus() != OrderStatus.IN_PREPARATION) {
            throw new InvalidActionException("Order " + order.getId() + " is already " + order.getStatus());
        }

        order.advanceOneUnit();
        markActed();
    }
}
