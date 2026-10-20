package entity;

/**
 * Represents the Waiter in the restaurant dining room.
 * Completes at most one task per turn: seating a customer,
 * taking an order, or serving a prepared dish.
 */
public class Waiter extends Staff {

    public Waiter() {
        super("Waiter", "Waiter");
    }

    public Waiter(String name) {
        super(name, "Waiter");
    }

    /**
     * Seats a queued customer at a specified free table.
     * Validates staff availability, customer status, and table availability
     * before modifying state or marking the staff action completed.
     *
     * @param c the customer to seat
     * @param t the table to seat the customer at
     * @throws InvalidActionException if waiter already acted, customer is not waiting, or table is occupied
     */
    public void seat(Customer c, Table t) throws InvalidActionException {
        checkCanAct();
        if (c == null) {
            throw new InvalidActionException("Customer cannot be null");
        }
        if (t == null) {
            throw new InvalidActionException("Table cannot be null");
        }
        if (c.getStatus() != CustomerStatus.WAITING) {
            throw new InvalidActionException("Customer " + c.getId() + " is not in the queue");
        }
        if (!t.isFree()) {
            throw new InvalidActionException("Table " + t.getTableNo() + " is occupied");
        }

        t.assign(c);
        c.seatAt(t);
        markActed();
    }

    /**
     * Takes an order from a seated customer using the restaurant menu.
     * Food selection is determined by the customer's deterministic rule.
     *
     * @param c the customer ordering
     * @param menu the restaurant menu
     * @return the created Order
     * @throws InvalidActionException if waiter already acted or customer is not seated
     */
    public Order takeOrder(Customer c, Menu menu) throws InvalidActionException {
        checkCanAct();
        if (c == null) {
            throw new InvalidActionException("Customer cannot be null");
        }
        if (c.getStatus() != CustomerStatus.SEATED) {
            throw new InvalidActionException("Customer " + c.getId() + " is not seated");
        }

        MenuItem item = c.chooseMenuItem(menu);
        if (item == null) {
            throw new InvalidActionException("No valid menu item found");
        }

        int billCents = c.calculateBill(item.getPriceCents());
        Order o = new Order(c.getId(), c, item, billCents);
        c.placeOrder(o);
        markActed();
        return o;
    }

    /**
     * Serves a ready order to its customer in the current turn.
     *
     * @param o the order to serve
     * @param turn the current game turn
     * @throws InvalidActionException if waiter already acted or order is not ready
     */
    public void serve(Order o, int turn) throws InvalidActionException {
        checkCanAct();
        if (o == null) {
            throw new InvalidActionException("Order cannot be null");
        }
        if (!o.isReady()) {
            throw new InvalidActionException("Order " + o.getId() + " is not READY yet");
        }

        o.markServed();
        Customer c = o.getCustomer();
        if (c != null) {
            c.markServed(turn);
        }
        markActed();
    }
}
