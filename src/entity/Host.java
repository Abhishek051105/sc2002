package entity;

public class Host extends Staff {

    public Host() {
        super("Host");
    }

    public void seat(Customer c, Table t) throws InvalidActionException {
        checkCanAct();

        if (c.getStatus() != CustomerStatus.WAITING) {
            throw new InvalidActionException("Customer " + c.getCustomerId() + " is not in the queue");
        }

        if (!t.isFree()) {
            throw new InvalidActionException("Table " + t.getTableNo() + " is occupied");
        }

        t.assign(c);
        c.seat();
        markActed();
    }
}