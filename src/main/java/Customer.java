package entity;

public abstract class Customer {

    private final int customerId;
    private final int arrivalTurn;

    private int satisfaction;
    private CustomerStatus status;

    private int servedTurn;
    private int readyToPayTurn;
    private int paidSatisfaction;

    public Customer(int customerId, int arrivalTurn) {

        if (customerId <= 0 || arrivalTurn <= 0) {
            throw new IllegalArgumentException(
                    "ID and arrival turn must be positive.");
        }

        this.customerId = customerId;
        this.arrivalTurn = arrivalTurn;

        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING;

        this.servedTurn = -1;
        this.readyToPayTurn = -1;
        this.paidSatisfaction = -1;
    }

    // =========================
    // Getters
    // =========================

    public int getCustomerId() {
        return customerId;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public int getPaidSatisfaction() {
        return paidSatisfaction;
    }

    // =========================
    // Stage 2 Extension Hooks
    // =========================

    /**
     * Default deterministic food selection rule.
     * Customer 1 -> Salad
     * Customer 2 -> Burger
     * Customer 3 -> Pasta
     * Repeat...
     *
     * CriticCustomer can override this.
     */
    public MenuItem chooseMenuItem(Menu menu) {

        if (menu == null) {
            throw new IllegalArgumentException(
                    "Menu cannot be null.");
        }

        if (menu.size() == 0) {
            throw new IllegalArgumentException(
                    "Menu cannot be empty.");
        }

        int index = (customerId - 1) % menu.size();

        return menu.getItem(index);
    }

    /**
     * Stage 1 customers pay full price.
     * VIPCustomer will override this in Stage 2.
     */
    public int calculateBill(int basePriceCents) {

        if (basePriceCents < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative.");
        }

        return basePriceCents;
    }

    /**
     * Stage 1: RegularCustomer returns 8.
     * Stage 2:
     * VIP -> 5
     * Critic -> 12
     */
    public abstract int getSatisfactionDecay();

    // =========================
    // Satisfaction Handling
    // =========================

    /**
     * Called once at the end of the turn.
     *
     * @return true if the customer leaves.
     */
    public boolean decreaseSatisfaction() {

        if (status == CustomerStatus.WAITING
                || status == CustomerStatus.SEATED
                || status == CustomerStatus.WAITING_FOR_FOOD) {

            satisfaction = Math.max(
                    0,
                    satisfaction - getSatisfactionDecay());

            if (satisfaction == 0) {
                status = CustomerStatus.LEFT;
                return true;
            }
        }

        return false;
    }

    // =========================
    // State Transitions
    // =========================

    public void seat() {

        if (status != CustomerStatus.WAITING) {
            throw new IllegalStateException(
                    "Customer is not waiting for a table.");
        }

        status = CustomerStatus.SEATED;
    }

    public void orderTaken() {

        if (status != CustomerStatus.SEATED) {
            throw new IllegalStateException(
                    "Customer must be seated before ordering.");
        }

        status = CustomerStatus.WAITING_FOR_FOOD;
    }

    public void serve(int turn) {

        if (status != CustomerStatus.WAITING_FOR_FOOD) {
            throw new IllegalStateException(
                    "Customer cannot be served yet.");
        }

        if (turn < arrivalTurn) {
            throw new IllegalStateException(
                    "Invalid serving turn.");
        }

        status = CustomerStatus.SERVED;
        servedTurn = turn;
    }

    public void finishEating(int turn) {

        if (status != CustomerStatus.SERVED) {
            throw new IllegalStateException(
                    "Customer has not been served.");
        }

        if (turn <= servedTurn) {
            throw new IllegalStateException(
                    "Customer cannot finish eating yet.");
        }

        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = turn;
    }

    public void markPaid(int turn) {

        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalStateException(
                    "Customer is not ready to pay.");
        }

        if (turn <= readyToPayTurn) {
            throw new IllegalStateException(
                    "Customer cannot pay yet.");
        }

        paidSatisfaction = satisfaction;
        status = CustomerStatus.PAID;
    }

    // =========================
    // Helper Methods
    // =========================

    public boolean hasLeft() {
        return status == CustomerStatus.LEFT;
    }

    public boolean hasPaid() {
        return status == CustomerStatus.PAID;
    }

    public boolean isReadyToPay() {
        return status == CustomerStatus.READY_TO_PAY;
    }

    @Override
    public String toString() {

        return "Customer #"
                + customerId
                + " | Satisfaction: "
                + satisfaction
                + " | Status: "
                + status;
    }
}