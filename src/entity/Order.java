package entity;

/**
 * Represents a food order placed with the restaurant kitchen.
 * Tracks preparation unit progress and lifecycle states.
 */
public class Order {
    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private final int billCents;
    private int unitsDone;
    private OrderStatus status;

    public Order(int id, Customer customer, MenuItem item, int billCents) {
        this.id = id;
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
        this.unitsDone = 0;
        this.status = OrderStatus.IN_PREPARATION;
    }

    /**
     * Advances cooking by one unit. Flips status to READY on the last unit.
     */
    public void advanceOneUnit() {
        if (unitsDone < item.getPrepUnits()) {
            unitsDone++;
        }
        if (unitsDone >= item.getPrepUnits()) {
            status = OrderStatus.READY;
        }
    }

    /**
     * Checks if the order is ready to be served.
     */
    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    /**
     * Marks the order as served to the customer. Requires READY status.
     */
    public void markServed() {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Cannot serve order that is not READY");
        }
        this.status = OrderStatus.SERVED;
    }

    /**
     * Marks the order as paid during cashier checkout. Requires SERVED status.
     */
    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    /**
     * Cancels an unfinished order when a customer abandons.
     */
    public void cancel() {
        if (status != OrderStatus.SERVED && status != OrderStatus.PAID) {
            this.status = OrderStatus.CANCELLED;
        }
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

    public int getBillCents() {
        return billCents;
    }

    public int getUnitsDone() {
        return unitsDone;
    }

    public int getPrepUnits() {
        return item.getPrepUnits();
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Order #%d [%s for C%d, %d/%d units, %s, %s]",
                id, item.getName(), customer.getId(), unitsDone, item.getPrepUnits(), status, Money.format(billCents));
    }
}
