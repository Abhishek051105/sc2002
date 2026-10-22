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
            throw new IllegalArgumentException("ID and arrival turn must be positive.");
        }
        this.customerId = customerId;
        this.arrivalTurn = arrivalTurn;
        satisfaction = 100;
        status = CustomerStatus.WAITING;
        servedTurn = -1;
        readyToPayTurn = -1;
        paidSatisfaction = -1;
    }

    public int getCustomerId() { return customerId; }
    public int getArrivalTurn() { return arrivalTurn; }
    public int getSatisfaction() { return satisfaction; }
    public CustomerStatus getStatus() { return status; }
    public int getPaidSatisfaction() { return paidSatisfaction; }

    public abstract int getSatisfactionDecay();

    // Called once at the end of each turn. True means the customer just left.
    public boolean decreaseSatisfaction() {
        if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED ||
                status == CustomerStatus.WAITING_FOR_FOOD) {
            satisfaction = Math.max(0, satisfaction - getSatisfactionDecay());
            if (satisfaction == 0) {
                status = CustomerStatus.LEFT;
                return true;
            }
        }
        return false;
    }

    public void seat() {
        if (status != CustomerStatus.WAITING) {
            throw new IllegalStateException("Customer is not waiting for a table.");
        }
        status = CustomerStatus.SEATED;
    }

    public void orderTaken() {
        if (status != CustomerStatus.SEATED) {
            throw new IllegalStateException("Customer must be seated before ordering.");
        }
        status = CustomerStatus.WAITING_FOR_FOOD;
    }

    public void serve(int turn) {
        if (status != CustomerStatus.WAITING_FOR_FOOD || turn < arrivalTurn) {
            throw new IllegalStateException("Customer cannot be served yet.");
        }
        status = CustomerStatus.SERVED;
        servedTurn = turn;
    }

    public void finishEating(int turn) {
        if (status != CustomerStatus.SERVED || turn <= servedTurn) {
            throw new IllegalStateException("Customer cannot finish eating yet.");
        }
        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = turn;
    }

    public void markPaid(int turn) {
        if (status != CustomerStatus.READY_TO_PAY || turn <= readyToPayTurn) {
            throw new IllegalStateException("Customer cannot pay yet.");
        }
        paidSatisfaction = satisfaction;
        status = CustomerStatus.PAID;
    }

    public boolean hasLeft() { return status == CustomerStatus.LEFT; }
    public boolean hasPaid() { return status == CustomerStatus.PAID; }
    public boolean isReadyToPay() { return status == CustomerStatus.READY_TO_PAY; }

    @Override
    public String toString() {
        return "Customer #" + customerId + " | Satisfaction: " + satisfaction +
                " | Status: " + status;
    }
}