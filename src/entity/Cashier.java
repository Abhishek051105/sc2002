package entity;

import java.util.List;

public class Cashier extends Staff {
    public Cashier() {
        super("Cashier");
    }

    /**
     * Oldest READY_TO_PAY customer that can pay this turn (ties -> lower id).
     * Calls c.markPaid(turn), marks the order paid, releases the table,
     * records bill + c.getPaidSatisfaction() in stats. Returns who paid, or null.
     */
    public Customer processNextPayment(List<Customer> customers, List<Order> orders,
                                       List<Table> tables, int turn, GameStats stats) {
        if (hasActedThisTurn()) {
            return null;
        }

        // pick the earliest readyToPayTurn, then the lowest id
        Customer next = null;
        for (Customer c : customers) {
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

        int billCents = 0;
        for (Order o : orders) {
            if (o.getCustomer() == next) {
                o.markPaid();
                billCents = o.getBillCents();
            }
        }
        for (Table t : tables) {
            if (t.getOccupant() == next) {
                t.release();
            }
        }

        stats.recordPayment(billCents, next.getPaidSatisfaction());
        markActed();
        return next;
    }
}
