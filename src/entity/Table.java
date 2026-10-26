package entity;

public class Table {
    private final int tableNo;
    private Customer occupant;

    public Table(int tableNo) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isFree() { throw new UnsupportedOperationException("TODO"); }
    public void assign(Customer c) { throw new UnsupportedOperationException("TODO"); }
    public void release() { throw new UnsupportedOperationException("TODO"); }
    public int getTableNo() { throw new UnsupportedOperationException("TODO"); }
    /** null when free. */
    public Customer getOccupant() { throw new UnsupportedOperationException("TODO"); }
}
