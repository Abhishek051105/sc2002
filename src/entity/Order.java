package entity;

/**
 * One order per customer, so order id == customer id.
 * The bill is locked at creation (Stage 3 Happy Hour relies on this).
 */
public class Order {
    private final int orderId;
    private final Customer customer;
    private final MenuItem item;
    private final int billCents;
    private int unitsDone;
    private OrderStatus status;

    public Order(int orderId, Customer customer, MenuItem item, int billCents) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Cook one unit; flips to READY on the last unit. */
    public void advanceOneUnit() { throw new UnsupportedOperationException("TODO"); }
    /** Requires READY. */
    public void markServed() { throw new UnsupportedOperationException("TODO"); }
    /** Requires SERVED. */
    public void markPaid() { throw new UnsupportedOperationException("TODO"); }
    /** Only if not yet served. */
    public void cancel() { throw new UnsupportedOperationException("TODO"); }

    public boolean isReady() { throw new UnsupportedOperationException("TODO"); }
    public int getOrderId() { throw new UnsupportedOperationException("TODO"); }
    public Customer getCustomer() { throw new UnsupportedOperationException("TODO"); }
    public MenuItem getItem() { throw new UnsupportedOperationException("TODO"); }
    public int getBillCents() { throw new UnsupportedOperationException("TODO"); }
    public int getUnitsDone() { throw new UnsupportedOperationException("TODO"); }
    public OrderStatus getStatus() { throw new UnsupportedOperationException("TODO"); }
}
