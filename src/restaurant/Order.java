package restaurant;

/**
 * Represents a customer's food order placed with the kitchen.
 */
public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private final double bill;
    private final int requiredUnits;
    private int completedUnits;
    private OrderStatus status;

    public Order(int id, Customer customer, MenuItem item, double bill) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.bill = bill;
        this.requiredUnits = item != null ? item.getPreparationUnits() : 0;
        this.completedUnits = 0;
        this.status = OrderStatus.PENDING;
    }

    public Order(int id, Customer customer, MenuItem item) {
        this(id, customer, item, item != null ? item.getPrice() : 0.0);
    }

    /**
     * Cooks one unit of preparation work on this order.
     * Transitions order to READY once all units are complete.
     */
    public void cookUnit() {
        if (completedUnits < requiredUnits) {
            completedUnits++;
        }
        if (completedUnits >= requiredUnits) {
            status = OrderStatus.READY;
        } else {
            status = OrderStatus.COOKING;
        }
    }

    /**
     * Checks if all preparation units are finished and the order is ready to serve.
     */
    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    /**
     * Marks the order as served to the customer.
     */
    public void markServed() {
        this.status = OrderStatus.SERVED;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return item;
    }

    public double getBill() {
        return bill;
    }

    public int getRequiredUnits() {
        return requiredUnits;
    }

    public int getCompletedUnits() {
        return completedUnits;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Order #%d [%s for Customer %d - %d/%d units - %s]",
                id, item.getName(), customer.getId(), completedUnits, requiredUnits, status);
    }
}
