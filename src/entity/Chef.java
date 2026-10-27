package entity;

public class Chef extends Staff{
     public Chef() {
        super("Chef");
    }
 
    // cook one unit of an order
    public void prepare(Order o) throws InvalidActionException {
        checkCanAct();
        if (o == null) {
            throw new InvalidActionException("That order does not exist");
        }
        if (o.getStatus() != OrderStatus.IN_PREPARATION) {
            throw new InvalidActionException("Order " + o.getOrderId() + " cannot be cooked, it is " + o.getStatus());
        }
        o.advanceOneUnit();
        markActed();   // only reached if all checks passed
    }
}
 
