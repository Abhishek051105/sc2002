package entity;

import java.util.Comparator;
import java.util.List;

/**
 * Represents the Cashier in the restaurant.
 * Processes customer payments in accordance with FIFO/priority timing:
 * collects payment from the customer with the earliest readyToPayTurn strictly
 * below the current turn, breaking ties by lowest customer ID.
 */
public class Cashier extends Staff {

    public Cashier() {
        super("Cashier", "Cashier");
    }

    public Cashier(String name) {
        super(name, "Cashier");
    }

    /**
     * Processes the next customer payment due for collection.
     * Takes the earliest readyToPayTurn below current turn, ties to lower customer ID.
     * Calls c.pay(), records the payment in game stats, and returns the customer.
     *
     * @param customers list of restaurant customers
     * @param turn the current turn
     * @param stats game statistics aggregator
     * @return the processed Customer, or null if no customer is eligible to pay
     */
    public Customer processNextPayment(List<Customer> customers, int turn, GameStats stats) {
        if (customers == null || stats == null) {
            return null;
        }

        Customer candidate = customers.stream()
                .filter(c -> c.getStatus() == CustomerStatus.READY_TO_PAY)
                .filter(c -> c.getReadyToPayTurn() < turn)
                .min(Comparator.comparingInt(Customer::getReadyToPayTurn)
                               .thenComparingInt(Customer::getId))
                .orElse(null);

        if (candidate != null) {
            int satisfaction = candidate.getSatisfaction();
            int billCents = candidate.pay();
            stats.recordPayment(billCents, satisfaction);
            markActed();
            return candidate;
        }

        return null;
    }
}
