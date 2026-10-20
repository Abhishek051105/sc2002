package restaurant;

/**
 * Represents a customer dining at the restaurant.
 * In Stage 1, this represents a Regular customer who loses 8 satisfaction
 * per waiting turn and pays full price.
 */
public class Customer {
    private final int id;
    private final int arrivalTurn;
    private CustomerState state;
    private int satisfaction;
    protected int satisfactionLossRate;
    private Table table;
    private Order order;
    private MenuItem desiredItem;
    private double bill;
    private int servedTurn;
    private int readyToPayTurn;

    public Customer(int id, int arrivalTurn, MenuItem desiredItem) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.desiredItem = desiredItem;
        this.state = CustomerState.WAITING;
        this.satisfaction = 100;
        this.satisfactionLossRate = 8; // Regular customer baseline loss
        this.table = null;
        this.order = null;
        this.bill = 0.0;
        this.servedTurn = -1;
        this.readyToPayTurn = -1;
    }

    public Customer(int id, int arrivalTurn) {
        this(id, arrivalTurn, null);
    }

    /**
     * Seats the customer at the specified table.
     */
    public void seatAt(Table t) {
        this.table = t;
        this.state = CustomerState.SEATED;
    }

    /**
     * Decides what menu item to order.
     */
    public MenuItem chooseItem(Menu menu) {
        if (desiredItem != null) {
            return desiredItem;
        }
        if (menu != null && !menu.getItems().isEmpty()) {
            return menu.getItems().get(0);
        }
        return null;
    }

    /**
     * Calculates the bill for the ordered item.
     * Regular customers pay full price. Subclasses (e.g. VIP in Stage 2) can override.
     */
    public double calculateBill(MenuItem item) {
        return item != null ? item.getPrice() : 0.0;
    }

    /**
     * Places an order for the customer.
     */
    public void placeOrder(Order o) {
        this.order = o;
        this.bill = o.getBill();
        this.state = CustomerState.ORDERED;
    }

    /**
     * Marks the customer as served in the given turn.
     * Starts customer eating; satisfaction loss ceases.
     */
    public void markServed(int turn) {
        this.servedTurn = turn;
        this.state = CustomerState.SERVED;
    }

    /**
     * Checks if the customer has finished eating in the current turn.
     * Eating takes 1 turn after being served.
     */
    public void updateEatingStatus(int turn) {
        if (state == CustomerState.SERVED && turn > servedTurn) {
            this.state = CustomerState.READY_TO_PAY;
            this.readyToPayTurn = turn;
        }
    }

    /**
     * Completes payment, releasing the table and transitioning state to PAID.
     */
    public void pay() {
        this.state = CustomerState.PAID;
        if (table != null) {
            table.release();
            table = null;
        }
    }

    /**
     * Marks the customer as having left unhappy due to zero satisfaction.
     */
    public void leaveUnhappy() {
        this.state = CustomerState.LEFT_UNHAPPY;
        if (table != null) {
            table.release();
            table = null;
        }
    }

    /**
     * Applies end-of-turn satisfaction loss while waiting for service.
     */
    public void applyTurnEndSatisfactionLoss() {
        if (state == CustomerState.WAITING || state == CustomerState.SEATED || state == CustomerState.ORDERED) {
            satisfaction = Math.max(0, satisfaction - satisfactionLossRate);
            if (satisfaction == 0) {
                leaveUnhappy();
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = Math.max(0, Math.min(100, satisfaction));
    }

    public int getSatisfactionLossRate() {
        return satisfactionLossRate;
    }

    public Table getTable() {
        return table;
    }

    public Order getOrder() {
        return order;
    }

    public MenuItem getDesiredItem() {
        return desiredItem;
    }

    public void setDesiredItem(MenuItem desiredItem) {
        this.desiredItem = desiredItem;
    }

    public double getBill() {
        return bill;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    public void setReadyToPayTurn(int readyToPayTurn) {
        this.readyToPayTurn = readyToPayTurn;
    }

    public boolean isReadyToPay() {
        return state == CustomerState.READY_TO_PAY;
    }

    @Override
    public String toString() {
        return String.format("Customer %d [%s, Sat: %d, Bill: $%.2f]",
                id, state, satisfaction, bill);
    }
}
