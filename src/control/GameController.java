package control;

import boundary.ConsoleUI;
import entity.Customer;
import entity.CustomerStatus;
import entity.GameStats;
import entity.InvalidActionException;
import entity.Order;
import entity.Restaurant;
import entity.Table;
import entity.WaiterTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller sequencing the 18-turn Restaurant Rush game according to Section 4.
 * Coordinates arrivals, waiter choice, chef choice, cashier collection,
 * eating progression, satisfaction decay, and objective evaluation.
 */
public class GameController {
    private final Restaurant restaurant;
    private final ArrivalSchedule arrivalSchedule;
    private final Objective objective;
    private final ConsoleUI ui;

    public GameController(Restaurant restaurant, ArrivalSchedule schedule, Objective objective, ConsoleUI ui) {
        this.restaurant = restaurant;
        this.arrivalSchedule = schedule;
        this.objective = objective;
        this.ui = ui;
    }

    /**
     * Executes the complete 18-turn game loop.
     */
    public void run() {
        for (int turn = 1; turn <= GameConfig.TOTAL_TURNS; turn++) {
            playTurn(turn);
        }
        ui.showFinalResult(restaurant, objective);
    }

    /**
     * Executes one turn strictly following the 6-step sequence in Section 4.
     */
    public void playTurn(int turn) {
        restaurant.getStats().setTurnsUsed(turn);

        // Reset staff turn action flags at start of each turn
        restaurant.getWaiter().startNewTurn();
        restaurant.getChef().startNewTurn();
        restaurant.getCashier().startNewTurn();

        // -------------------------------------------------------------
        // STEP 1: Arrivals
        // -------------------------------------------------------------
        Customer arrival = arrivalSchedule.customerArrivingAt(turn);
        if (arrival != null) {
            restaurant.addArrival(arrival);
            ui.showMessage(String.format(">> Turn %d Arrival: Customer %d arrived and joined the queue.",
                    turn, arrival.getId()));
        }
        ui.showStartOfTurn(turn, restaurant);

        // -------------------------------------------------------------
        // STEP 2: Waiter Choice
        // -------------------------------------------------------------
        executeWaiterChoice(turn);

        // -------------------------------------------------------------
        // STEP 3: Chef Choice
        // -------------------------------------------------------------
        executeChefChoice();

        // -------------------------------------------------------------
        // STEP 4: Eating and Payment
        // -------------------------------------------------------------
        // 4a. Cashier first: oldest READY_TO_PAY customer with readyToPayTurn < turn pays
        Customer paid = restaurant.getCashier().processNextPayment(
                restaurant.getAllCustomers(), turn, restaurant.getStats()
        );
        if (paid != null) {
            ui.showMessage(String.format(">> Cashier collected payment from Customer %d (Sat: %d). Table freed.",
                    paid.getId(), paid.getSatisfaction()));
        }

        // 4b. Customers finish eating: every EATING customer with servedTurn < turn becomes READY_TO_PAY
        for (Customer c : restaurant.getAllCustomers()) {
            if (c.getStatus() == CustomerStatus.EATING) {
                c.finishEatingIfDue(turn);
                if (c.getStatus() == CustomerStatus.READY_TO_PAY) {
                    ui.showMessage(String.format(">> Customer %d finished eating and is READY_TO_PAY.", c.getId()));
                }
            }
        }

        // -------------------------------------------------------------
        // STEP 5: Waiting and Abandonment
        // -------------------------------------------------------------
        List<Customer> departing = new ArrayList<>();
        for (Customer c : restaurant.getAllCustomers()) {
            boolean abandoned = c.applyEndOfTurnWaiting();
            if (abandoned) {
                departing.add(c);
            }
        }
        for (Customer c : departing) {
            restaurant.removeFromQueue(c);
            restaurant.getStats().recordDeparture();
            ui.showMessage(String.format(">> Customer %d abandoned the restaurant due to zero satisfaction!", c.getId()));
        }

        ui.showEndOfTurn(turn, restaurant);

        // STEP 6: After turn 18, objective is evaluated in run()
    }

    private void executeWaiterChoice(int turn) {
        boolean validChoice = false;
        while (!validChoice) {
            WaiterTask task = ui.askWaiterTask();
            try {
                switch (task) {
                    case SEAT:
                        int custId = ui.askCustomerId("Enter Customer ID to seat: ");
                        int tableNo = ui.askTableNo("Enter Table No (1 or 2): ");
                        Customer toSeat = restaurant.findCustomer(custId);
                        Table table = restaurant.getTable(tableNo);
                        restaurant.getWaiter().seat(toSeat, table);
                        restaurant.removeFromQueue(toSeat);
                        ui.showMessage(String.format("-> Waiter seated Customer %d at Table %d.", custId, tableNo));
                        validChoice = true;
                        break;
                    case TAKE_ORDER:
                        int orderCustId = ui.askCustomerId("Enter Customer ID to take order from: ");
                        Customer orderCust = restaurant.findCustomer(orderCustId);
                        Order order = restaurant.getWaiter().takeOrder(orderCust, restaurant.getMenu());
                        restaurant.addOrder(order);
                        ui.showMessage(String.format("-> Waiter took order from Customer %d: %s (%s).",
                                orderCustId, order.getItem().getName(), order.getItem().getPriceCents() / 100.0));
                        validChoice = true;
                        break;
                    case SERVE:
                        int serveOrderId = ui.askOrderIdOrWait("Enter Order ID to serve: ");
                        Order toServe = restaurant.findOrder(serveOrderId);
                        restaurant.getWaiter().serve(toServe, turn);
                        restaurant.getStats().recordServed();
                        ui.showMessage(String.format("-> Waiter served Order #%d to Customer %d.",
                                serveOrderId, toServe.getCustomer().getId()));
                        validChoice = true;
                        break;
                    case WAIT:
                        ui.showMessage("-> Waiter chose to WAIT this turn.");
                        validChoice = true;
                        break;
                }
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void executeChefChoice() {
        boolean validChoice = false;
        while (!validChoice) {
            int orderId = ui.askOrderIdOrWait("\nEnter Order ID for Chef to cook (0 = Wait): ");
            if (orderId == 0) {
                ui.showMessage("-> Chef chose to WAIT this turn.");
                validChoice = true;
                break;
            }

            Order toCook = restaurant.findOrder(orderId);
            try {
                restaurant.getChef().prepare(toCook);
                ui.showMessage(String.format("-> Chef prepared 1 unit for Order #%d. Units: %d/%d (%s).",
                        orderId, toCook.getUnitsDone(), toCook.getPrepUnits(), toCook.getStatus()));
                validChoice = true;
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
