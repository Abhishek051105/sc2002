package entity;

public class Cashier extends Staff {
    public Cashier() {
        super("Cashier");
    }

    /**
     * Oldest READY_TO_PAY customer that can pay this turn (ties -> lower id).
     * Marks the customer and their order paid, frees their table and
     * records the bill + satisfaction in stats. Returns who paid, or null.
     */
    public Customer processNextPayment(Restaurant restaurant, int turn) {
        if (hasActedThisTurn()) {
            return null;
        }

        // pick the earliest readyToPayTurn, then the lowest id
        Customer next = null;
        for (Customer c : restaurant.getAllCustomers()) {
            if (!c.isReadyToPay() || c.getReadyToPayTurn() >= turn) {
                continue;
            }
            if (next == null
                    || c.getReadyToPayTurn() < next.getReadyToPayTurn()
                    || (c.getReadyToPayTurn() == next.getReadyToPayTurn()
                        && c.getCustomerId() < next.getCustomerId())) {
                next = c;
            }
        }
        if (next == null) {
            return null;
        }

        next.markPaid(turn);

        // order id == customer id, so the restaurant can look it up for us
        Order order = restaurant.findOrder(next.getCustomerId());
        order.markPaid();

        Table table = restaurant.findTableOf(next);
        if (table != null) {
            table.release();
        }

        restaurant.getStats().recordPayment(order.getBillCents(), next.getPaidSatisfaction());
        markActed();
        return next;
    }
}
