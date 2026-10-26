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
                                       List<Table> tables, int turn, GameStats stats) { throw new UnsupportedOperationException("TODO"); }
}
