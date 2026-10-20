package entity;

/**
 * Abstract entity representing a customer in the restaurant.
 * Encapsulates the dining lifecycle state machine, satisfaction decay,
 * and billing calculation.
 */
public abstract class Customer {
    private final int id;
    private int satisfaction;
    private CustomerStatus status;
    private Table table;
    private Order order;
    private int servedTurn;
    private int readyToPayTurn;

    public Customer(int id) {
        this.id = id;
        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING;
        this.table = null;
        this.order = null;
        this.servedTurn = -1;
        this.readyToPayTurn = -1;
    }

    /**
     * Seats the customer at a dining table.
     * Transitions state from WAITING to SEATED.
     */
    public void seatAt(Table t) {
        if (status != CustomerStatus.WAITING) {
            throw new IllegalStateException("Customer " + id + " cannot be seated from state " + status);
        }
        this.table = t;
        this.status = CustomerStatus.SEATED;
    }

    /**
     * Places the food order. Transitions state from SEATED to ORDERED.
     */
    public void placeOrder(Order o) {
        if (status != CustomerStatus.SEATED) {
            throw new IllegalStateException("Customer " + id + " cannot place order from state " + status);
        }
        this.order = o;
        this.status = CustomerStatus.ORDERED;
    }

    /**
     * Marks the customer as served in the given turn.
     * Transitions state from ORDERED to EATING and stores servedTurn.
     */
    public void markServed(int turn) {
        if (status != CustomerStatus.ORDERED) {
            throw new IllegalStateException("Customer " + id + " cannot be served from state " + status);
        }
        this.servedTurn = turn;
        this.status = CustomerStatus.EATING;
    }

    /**
     * Updates dining progress. When a customer is EATING and servedTurn < currentTurn,
     * eating finishes and status flips to READY_TO_PAY, recording readyToPayTurn.
     */
    public void finishEatingIfDue(int turn) {
        if (status == CustomerStatus.EATING && servedTurn < turn) {
            this.status = CustomerStatus.READY_TO_PAY;
            this.readyToPayTurn = turn;
        }
    }

    /**
     * Processes payment collection by the Cashier.
     * Transitions state from READY_TO_PAY to PAID, marks order paid,
     * releases the occupied table, and returns the bill in cents.
     */
    public int pay() {
        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalStateException("Customer " + id + " cannot pay from state " + status);
        }
        this.status = CustomerStatus.PAID;
        if (order != null) {
            order.markPaid();
        }
        if (table != null) {
            table.release();
            table = null;
        }
        return order != null ? order.getBillCents() : 0;
    }

    /**
     * Handles customer departure when satisfaction drops to 0.
     * Cancels any unfinished order and releases the table.
     */
    public void leave() {
        this.status = CustomerStatus.LEFT;
        if (order != null) {
            order.cancel();
        }
        if (table != null) {
            table.release();
            table = null;
        }
    }

    /**
     * Applies satisfaction decay at turn end.
     * Only WAITING, SEATED, or ORDERED customers lose satisfaction.
     * Clamps satisfaction to [0..100]. If 0 is reached, customer leaves.
     *
     * @return true if customer abandoned this turn, false otherwise
     */
    public boolean applyEndOfTurnWaiting() {
        if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED) {
            this.satisfaction = Math.max(0, this.satisfaction - getSatisfactionLossPerTurn());
            if (this.satisfaction == 0) {
                leave();
                return true;
            }
        }
        return false;
    }

    /**
     * Food selection rule: customer n orders menu[(n-1) % menu.size()].
     */
    public MenuItem chooseMenuItem(Menu menu) {
        if (menu == null || menu.size() == 0) {
            throw new IllegalArgumentException("Menu cannot be null or empty");
        }
        return menu.getItem((id - 1) % menu.size());
    }

    /**
     * Calculates the final bill in cents from the base price.
     * Subclasses (e.g. VIP in Stage 2) override this for discounts.
     */
    public int calculateBill(int basePriceCents) {
        return basePriceCents;
    }

    /**
     * Satisfaction points lost per turn while waiting for food or seating.
     */
    public abstract int getSatisfactionLossPerTurn();

    public int getId() {
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Table getTable() {
        return table;
    }

    public Order getOrder() {
        return order;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    @Override
    public String toString() {
        return String.format("Customer %d [%s, Sat: %d]", id, status, satisfaction);
    }
}
