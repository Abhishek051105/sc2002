package control;

import boundary.ConsoleUI;
import entity.Customer;
import entity.CustomerStatus;
import entity.GameStats;
import entity.InvalidActionException;
import entity.Order;
import entity.Restaurant;
import entity.Staff;
import entity.Table;
import entity.WaiterTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequences the 6 steps of a turn. It never checks customer subclasses (no instanceof),
 * it just calls methods on Customer and lets each subclass behave its own way.
 * Catches InvalidActionException and re-prompts.
 */
public class GameController {
    private final Restaurant restaurant;
    private final ConsoleUI ui;
    private final ArrivalSchedule schedule;
    private final Objective objective;
    private int currentTurn;

    public GameController(Restaurant restaurant, ConsoleUI ui,
                          ArrivalSchedule schedule, Objective objective) {
        this.restaurant = restaurant;
        this.ui = ui;
        this.schedule = schedule;
        this.objective = objective;
        this.currentTurn = 0;
    }

    /** Turns 1..MAX_TURNS, then step 6 (VICTORY / DEFEAT). */
    public void run() {
        for (currentTurn = 1; currentTurn <= GameConfig.MAX_TURNS; currentTurn++) {
            playTurn();
        }
        // Step 6: only after the last turn
        GameStats stats = restaurant.getStats();
        boolean won = objective.isMet(stats);
        ui.showFinalResult(won, stats, objective);
    }

    /** Steps 1-5 for currentTurn. Resets staff first. */
    private void playTurn() {
        for (Staff s : restaurant.getAllStaff()) {
            s.startNewTurn();
        }
        handleArrivals();
        handleWaiter();
        handleChef();               // must come after the waiter
        handleEatingAndPayment();
        handleEndOfTurnWaiting();
        ui.showEndOfTurn(currentTurn, restaurant);
    }

    /** Step 1 */
    private void handleArrivals() {
        List<Customer> arrivals = schedule.customersArrivingAt(currentTurn);
        for (Customer c : arrivals) {
            restaurant.addArrival(c);
            ui.showMessage("C" + c.getCustomerId() + " has arrived.");
        }
        ui.showStartOfTurn(currentTurn, restaurant);
    }

    /** Step 2: loop until a valid task or WAIT */
    private void handleWaiter() {
        while (true) {
            WaiterTask task = ui.askWaiterTask();
            try {
                switch (task) {
                    case SEAT -> {
                        Customer c = getCustomerOrThrow(ui.askCustomerId("Customer to seat: C"));
                        Table t = restaurant.getTable(ui.askTableNo(restaurant.getTables().size()));
                        if (t == null) {
                            throw new InvalidActionException("That table does not exist.");
                        }
                        restaurant.getWaiter().seat(c, t);
                        restaurant.removeFromQueue(c);
                        ui.showMessage("C" + c.getCustomerId() + " seated at Table " + t.getTableNo() + ".");
                    }
                    case TAKE_ORDER -> {
                        Customer c = getCustomerOrThrow(ui.askCustomerId("Take order from: C"));
                        Order o = restaurant.getWaiter().takeOrder(c, restaurant.getMenu());
                        restaurant.addOrder(o);
                        ui.showMessage("C" + c.getCustomerId() + " ordered " + o.getItem().getName() + ".");
                    }
                    case SERVE -> {
                        Order o = getOrderOrThrow(ui.askCustomerId("Serve order of: C"));
                        restaurant.getWaiter().serve(o, currentTurn);
                        restaurant.getStats().recordServed();
                        ui.showMessage("Served " + o.getItem().getName() + " to C" + o.getOrderId() + ".");
                    }
                    case WAIT -> ui.showMessage("Waiter waits this turn.");
                }
                return;   // valid choice made, leave the loop
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());   // nothing was used up, ask again
            }
        }
    }

    /** Step 3: AFTER the waiter. Loop until a valid order or WAIT */
    private void handleChef() {
        while (true) {
            int orderId = ui.askOrderIdOrWait();
            if (orderId == 0) {
                ui.showMessage("Chef waits this turn.");
                return;
            }
            try {
                Order o = getOrderOrThrow(orderId);
                restaurant.getChef().prepare(o);
                if (o.isReady()) {
                    ui.showMessage(o.getItem().getName() + " for C" + orderId + " is READY.");
                } else {
                    ui.showMessage("Chef cooked " + o.getItem().getName() + " for C" + orderId
                            + " (" + o.getUnitsDone() + "/" + o.getItem().getPrepUnits() + ").");
                }
                return;
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /** Step 4: cashier first, then SERVED customers with servedTurn < turn -> finishEating(turn) */
    private void handleEatingAndPayment() {
        Customer paid = restaurant.getCashier().processNextPayment(restaurant, currentTurn);
        if (paid != null) {
            ui.showMessage("C" + paid.getCustomerId() + " paid.");
        }

        // done after the cashier, so nobody eats and pays in the same turn
        for (Customer c : restaurant.getAllCustomers()) {
            if (c.getStatus() == CustomerStatus.SERVED && c.getServedTurn() < currentTurn) {
                c.finishEating(currentTurn);
                ui.showMessage("C" + c.getCustomerId() + " finished eating and is ready to pay.");
            }
        }
    }

    /**
     * Step 5: c.decreaseSatisfaction() for everyone (iterate over a COPY).
     * If it returns true, the restaurant does the clean-up and we count a departure.
     */
    private void handleEndOfTurnWaiting() {
        List<Customer> everyone = new ArrayList<>(restaurant.getAllCustomers());
        for (Customer c : everyone) {
            boolean left = c.decreaseSatisfaction();
            if (left) {
                restaurant.customerLeft(c);
                restaurant.getStats().recordDeparture();
                ui.showMessage("C" + c.getCustomerId() + " got fed up and left!");
            }
        }
    }

    // ---- small helpers so a bad ID becomes a normal "try again" error ----

    private Customer getCustomerOrThrow(int id) throws InvalidActionException {
        Customer c = restaurant.findCustomer(id);
        if (c == null) {
            throw new InvalidActionException("There is no customer C" + id + ".");
        }
        return c;
    }

    private Order getOrderOrThrow(int id) throws InvalidActionException {
        Order o = restaurant.findOrder(id);
        if (o == null) {
            throw new InvalidActionException("There is no order for C" + id + ".");
        }
        return o;
    }
}
