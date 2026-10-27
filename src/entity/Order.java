package entity;


public class Order {
    private int orderId;
    private Customer customer;
    private MenuItem item;
    private int billCents;
    private int unitsDone;
    private OrderStatus status;
 
    public Order(int orderId, Customer customer, MenuItem item, int billCents) {
        this.orderId = orderId;
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
        this.unitsDone = 0;
        this.status = OrderStatus.IN_PREPARATION;
    }
       public void advanceOneUnit() {
        if (status != OrderStatus.IN_PREPARATION) {
            throw new IllegalStateException("Order " + orderId + " is not being prepared");
        }
        unitsDone = unitsDone + 1;
        if (unitsDone == item.getPrepUnits()) {
            status = OrderStatus.READY;
        }
    }
 
    public void markServed() {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Order " + orderId + " is not ready");
        }
        status = OrderStatus.SERVED;
    }
 
    public void markPaid() {
        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("Order " + orderId + " has not been served");
        }
        status = OrderStatus.PAID;
    }
 
    // customer left before getting their food
    public void cancel() {
        if (status == OrderStatus.SERVED || status == OrderStatus.PAID) {
            throw new IllegalStateException("Order " + orderId + " was already served");
        }
        status = OrderStatus.CANCELLED;
    }
 
    public boolean isReady() {
        return status == OrderStatus.READY;
    }
 
    public int getOrderId() {
        return orderId;
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
 
    public OrderStatus getStatus() {
        return status;
    }
}
