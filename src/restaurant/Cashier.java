package restaurant;

import java.util.Comparator;
import java.util.List;

/**
 * Represents the Cashier in the restaurant.
 * Processes customer payments when customers are ready to pay,
 * frees tables, and records revenue and satisfaction in the game statistics.
 */
public class Cashier extends Staff {

    public Cashier() {
        super("Cashier", "Cashier");
    }

    public Cashier(String name) {
        super(name, "Cashier");
    }

    /**
     * Processes the next pending customer payment according to project rules:
     * takes the customer with the earliest readyToPayTurn below the current turn.
     * Ties are broken by the lower customer id.
     * Invokes c.pay(), then stats.recordPayment(bill, sat), and returns the customer.
     *
     * @param customers the list of active customers
     * @param turn the current game turn
     * @param stats the game stats tracker
     * @return the processed Customer, or null if no customer is eligible for payment
     */
    public Customer processNextPayment(List<Customer> customers, int turn, GameStats stats) {
        if (customers == null || stats == null) {
            return null;
        }

        Customer eligibleCustomer = customers.stream()
                .filter(c -> c.getState() == CustomerState.READY_TO_PAY)
                .filter(c -> c.getReadyToPayTurn() < turn)
                .min(Comparator.comparingInt(Customer::getReadyToPayTurn)
                               .thenComparingInt(Customer::getId))
                .orElse(null);

        if (eligibleCustomer != null) {
            eligibleCustomer.pay();
            stats.recordPayment(eligibleCustomer.getBill(), eligibleCustomer.getSatisfaction());
            markActed();
            return eligibleCustomer;
        }

        return null;
    }
}
