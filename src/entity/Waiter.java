package entity;

public class Waiter extends Staff {
    public Waiter() {
        super("Waiter");
    }

    /** c must be WAITING, t must be free. Calls t.assign(c) then c.seat(). */
    public void seat(Customer c, Table t) throws InvalidActionException { throw new UnsupportedOperationException("TODO"); }

    /**
     * c must be SEATED. Uses c.chooseMenuItem(menu) and c.calculateBill(price)
     * to lock the bill, creates the Order (id = customer id), calls c.orderTaken().
     */
    public Order takeOrder(Customer c, Menu menu) throws InvalidActionException { throw new UnsupportedOperationException("TODO"); }

    /** o must be READY. Calls o.markServed() and o.getCustomer().serve(turn). */
    public void serve(Order o, int turn) throws InvalidActionException { throw new UnsupportedOperationException("TODO"); }
}
