package entity;

public class Table {
    private final int tableNo;
    private Customer occupant;

    public Table(int tableNo) {
        this.tableNo = tableNo;
        this.occupant = null;
    }

    public boolean isFree() {
        return occupant == null;
    }

    // Waiter checks isFree() first, so this only fails on a programming bug
    public void assign(Customer c) {
        if (!isFree()) {
            throw new IllegalStateException("Table " + tableNo + " is occupied");
        }
        occupant = c;
    }

    public void release() {
        occupant = null;
    }

    public int getTableNo() {
        return tableNo;
    }

    /** null when free. */
    public Customer getOccupant() {
        return occupant;
    }
}
