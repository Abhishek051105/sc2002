package restaurant;

/**
 * Represents a Waiter in the restaurant.
 * Completes at most one task per turn: seating a queued customer,
 * taking an order from a seated customer, or serving a ready dish.
 */
public class Waiter extends Staff {
    private static int nextOrderId = 1;

    public Waiter() {
        super("Waiter", "Waiter");
    }

    public Waiter(String name) {
        super(name, "Waiter");
    }

    /**
     * Seats a queued customer at a specified free table.
     * Validates staff availability, queue status, and table availability
     * before modifying state and marking the waiter as acted.
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
        if (c.getState() != CustomerState.WAITING) {
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
     *
     * @param c the customer ordering
     * @param menu the menu to choose from
     * @return the created Order
     * @throws InvalidActionException if waiter already acted or customer is not seated
     */
    public Order takeOrder(Customer c, Menu menu) throws InvalidActionException {
        checkCanAct();
        if (c == null) {
            throw new InvalidActionException("Customer cannot be null");
        }
        if (c.getState() != CustomerState.SEATED) {
            throw new InvalidActionException("Customer " + c.getId() + " is not seated");
        }

        MenuItem item = c.chooseItem(menu);
        if (item == null) {
            throw new InvalidActionException("No valid menu item chosen for Customer " + c.getId());
        }

        double bill = c.calculateBill(item);
        Order o = new Order(nextOrderId++, c, item, bill);
        c.placeOrder(o);
        markActed();
        return o;
    }

    /**
     * Overloaded takeOrder() where a specific menu item is requested.
     *
     * @param c the customer ordering
     * @param item the specific menu item chosen
     * @return the created Order
     * @throws InvalidActionException if waiter already acted or customer is not seated
     */
    public Order takeOrder(Customer c, MenuItem item) throws InvalidActionException {
        checkCanAct();
        if (c == null) {
            throw new InvalidActionException("Customer cannot be null");
        }
        if (c.getState() != CustomerState.SEATED) {
            throw new InvalidActionException("Customer " + c.getId() + " is not seated");
        }
        if (item == null) {
            throw new InvalidActionException("Menu item cannot be null");
        }

        double bill = c.calculateBill(item);
        Order o = new Order(nextOrderId++, c, item, bill);
        c.placeOrder(o);
        markActed();
        return o;
    }

    /**
     * Serves a ready order to its customer.
     *
     * @param o the order to serve
     * @param turn the current turn number
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

    public static void resetOrderIdCounter() {
        nextOrderId = 1;
    }
}
