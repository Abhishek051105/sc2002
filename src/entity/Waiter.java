package entity;

public class Waiter extends Staff {
    public Waiter() {
        super("Waiter");
    }

    /** c must be WAITING, t must be free. Calls t.assign(c) then c.seat(). */
    public void seat(Customer c, Table t) throws InvalidActionException {
        checkCanAct();
        if (c.getStatus() != CustomerStatus.WAITING) {
            throw new InvalidActionException("Customer " + c.getCustomerId() + " is not in the queue");
        }
        if (!t.isFree()) {
            throw new InvalidActionException("Table " + t.getTableNo() + " is occupied");
        }
        t.assign(c);
        c.seat();
        markActed();   // only reached if all checks passed
    }

    /**
     * c must be SEATED. Uses c.chooseMenuItem(menu) and c.calculateBill(price)
     * to lock the bill, creates the Order (id = customer id), calls c.orderTaken().
     */
    public Order takeOrder(Customer c, Menu menu) throws InvalidActionException {
        checkCanAct();
        if (c.getStatus() != CustomerStatus.SEATED) {
            throw new InvalidActionException("Customer " + c.getCustomerId()
                    + " cannot order, they are " + c.getStatus());
        }
        MenuItem item = c.chooseMenuItem(menu);
        int billCents = c.calculateBill(item.getPriceCents());
        Order o = new Order(c.getCustomerId(), c, item, billCents);
        c.orderTaken();
        markActed();
        return o;
    }

    /** o must be READY. Calls o.markServed() and o.getCustomer().serve(turn). */
    public void serve(Order o, int turn) throws InvalidActionException {
        checkCanAct();
        if (!o.isReady()) {
            throw new InvalidActionException("Order " + o.getOrderId()
                    + " is not READY yet, it is " + o.getStatus());
        }
        o.markServed();
        o.getCustomer().serve(turn);
        markActed();
    }
}
