package control;

import entity.Customer;
import entity.RegularCustomer;

/**
 * Manages customer arrival timing. Creates new concrete customer instances
 * at designated turn intervals (turns 1, 4, 7, 10, 13, 16).
 */
public class ArrivalSchedule {
    private int nextId = 1;

    /**
     * Returns a newly arrived customer if scheduled for the given turn, or null.
     */
    public Customer customerArrivingAt(int turn) {
        if (GameConfig.ARRIVAL_TURNS.contains(turn)) {
            return new RegularCustomer(nextId++);
        }
        return null;
    }

    public void reset() {
        this.nextId = 1;
    }
}
