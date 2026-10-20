package entity;

/**
 * Represents a dining table in the restaurant.
 * A table remains occupied continuously until its customer pays or leaves.
 */
public class Table {
    private final int tableNo;
    private Customer currentCustomer;

    public Table(int no) {
        this.tableNo = no;
        this.currentCustomer = null;
    }

    /**
     * Checks if the table is currently free.
     */
    public boolean isFree() {
        return currentCustomer == null;
    }

    /**
     * Assigns a customer to this table.
     *
     * @param c the customer being seated
     * @throws InvalidActionException if the table is already occupied
     */
    public void assign(Customer c) throws InvalidActionException {
        if (!isFree()) {
            throw new InvalidActionException("Table " + tableNo + " is occupied");
        }
        this.currentCustomer = c;
    }

    /**
     * Overloaded assign() to mark the table occupied.
     *
     * @throws InvalidActionException if the table is already occupied
     */
    public void assign() throws InvalidActionException {
        if (!isFree()) {
            throw new InvalidActionException("Table " + tableNo + " is occupied");
        }
    }

    /**
     * Releases this table, freeing it for new arrivals.
     */
    public void release() {
        this.currentCustomer = null;
    }

    public int getTableNo() {
        return tableNo;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    @Override
    public String toString() {
        return "Table " + tableNo + (isFree() ? " (Free)" : " (Occupied by Customer " + currentCustomer.getId() + ")");
    }
}
