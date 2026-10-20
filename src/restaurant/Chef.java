package restaurant;

/**
 * Represents the Chef in the restaurant kitchen.
 * In Stage 1, the manager sets kitchen priority, and the Chef completes
 * one preparation unit per turn for the selected order.
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
     * @throws InvalidActionException if chef already acted, order is null, or order is already ready/served
     */
    public void cook(Order order) throws InvalidActionException {
        checkCanAct();
        if (order == null) {
            throw new InvalidActionException("Order cannot be null");
        }
        if (order.isReady()) {
            throw new InvalidActionException("Order " + order.getId() + " is already READY");
        }
        if (order.getStatus() == OrderStatus.SERVED) {
            throw new InvalidActionException("Order " + order.getId() + " has already been served");
        }

        order.cookUnit();
        markActed();
    }
}
