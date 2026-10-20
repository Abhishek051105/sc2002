package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller class orchestrating the 18-turn Restaurant Rush game for Stage 1.
 * Coordinates customer arrivals, staff action turns, dining lifecycle updates,
 * cashier collection, and win/loss determination.
 */
public class GameController {
    public static final int TOTAL_TURNS = 18;
    public static final int NUM_TABLES = 3;
    public static final int MAX_UNHAPPY_TOLERANCE = 1; // 2 or more unhappy departures = Defeat

    private int currentTurn;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private final List<Table> tables;
    private final Menu menu;
    private final GameStats stats;

    private final List<Customer> waitingQueue;
    private final List<Customer> allCustomers;
    private final List<Order> activeOrders;
    private final List<CustomerArrivalSchedule> arrivalSchedules;

    public static class CustomerArrivalSchedule {
        public final int turn;
        public final int customerId;
        public final String itemName;

        public CustomerArrivalSchedule(int turn, int customerId, String itemName) {
            this.turn = turn;
            this.customerId = customerId;
            this.itemName = itemName;
        }
    }

    public GameController() {
        this.currentTurn = 0;
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.cashier = new Cashier();
        this.menu = new Menu();
        this.stats = new GameStats();

        this.tables = new ArrayList<>();
        for (int i = 1; i <= NUM_TABLES; i++) {
            tables.add(new Table(i));
        }

        this.waitingQueue = new ArrayList<>();
        this.allCustomers = new ArrayList<>();
        this.activeOrders = new ArrayList<>();
        this.arrivalSchedules = new ArrayList<>();

        setupDefaultArrivals();
    }

    private void setupDefaultArrivals() {
        // 6 arrivals across the 18 turns; customer 6 arrives on turn 16
        arrivalSchedules.add(new CustomerArrivalSchedule(1, 1, "Pasta"));
        arrivalSchedules.add(new CustomerArrivalSchedule(3, 2, "Salad"));
        arrivalSchedules.add(new CustomerArrivalSchedule(6, 3, "Burger"));
        arrivalSchedules.add(new CustomerArrivalSchedule(9, 4, "Pasta"));
        arrivalSchedules.add(new CustomerArrivalSchedule(12, 5, "Salad"));
        arrivalSchedules.add(new CustomerArrivalSchedule(16, 6, "Burger"));
    }

    /**
     * Starts a new turn, processing arrivals and updating staff states.
     */
    public void startTurn() {
        currentTurn++;
        stats.setTurnsUsed(currentTurn);

        // Reset staff turn action flags
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        // Process arrivals for this turn
        for (CustomerArrivalSchedule sched : arrivalSchedules) {
            if (sched.turn == currentTurn) {
                MenuItem item = menu.getItem(sched.itemName);
                Customer c = new Customer(sched.customerId, currentTurn, item);
                waitingQueue.add(c);
                allCustomers.add(c);
            }
        }
    }

    /**
     * Executes the manager's seating choice through the Waiter.
     */
    public void seatCustomer(int customerId, int tableNo) throws InvalidActionException {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new InvalidActionException("Customer " + customerId + " not found");
        }
        Table table = findTableByNo(tableNo);
        if (table == null) {
            throw new InvalidActionException("Table " + tableNo + " does not exist");
        }

        waiter.seat(customer, table);
        waitingQueue.remove(customer);
    }

    /**
     * Executes the manager's order taking choice through the Waiter.
     */
    public Order takeCustomerOrder(int customerId) throws InvalidActionException {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new InvalidActionException("Customer " + customerId + " not found");
        }

        Order order = waiter.takeOrder(customer, menu);
        activeOrders.add(order);
        return order;
    }

    /**
     * Executes the manager's serving choice through the Waiter.
     */
    public void serveOrder(int orderId) throws InvalidActionException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new InvalidActionException("Order " + orderId + " not found");
        }

        waiter.serve(order, currentTurn);
        stats.recordServed();
    }

    /**
     * Executes the manager's cooking choice through the Chef.
     */
    public void cookOrder(int orderId) throws InvalidActionException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new InvalidActionException("Order " + orderId + " not found");
        }

        chef.cook(order);
    }

    /**
     * Updates dining progress and cashier collections at turn end.
     */
    public void endTurn() {
        // 1. Update eating progress for served customers
        for (Customer c : allCustomers) {
            c.updateEatingStatus(currentTurn);
        }

        // 2. Cashier payment processing
        cashier.processNextPayment(allCustomers, currentTurn, stats);

        // 3. Customer satisfaction loss for waiting, seated, and ordered customers
        for (Customer c : allCustomers) {
            CustomerState stateBefore = c.getState();
            c.applyTurnEndSatisfactionLoss();
            if (stateBefore != CustomerState.LEFT_UNHAPPY && c.getState() == CustomerState.LEFT_UNHAPPY) {
                stats.recordUnhappyDeparture();
                waitingQueue.remove(c);
            }
        }
    }

    /**
     * Checks if the game has ended in defeat.
     */
    public boolean isDefeat() {
        return stats.getUnhappyDepartures() > MAX_UNHAPPY_TOLERANCE;
    }

    /**
     * Checks if the game has concluded all 18 turns.
     */
    public boolean isGameOver() {
        return currentTurn >= TOTAL_TURNS || isDefeat();
    }

    /**
     * Determines whether the player achieved victory at the end of the game.
     */
    public boolean isVictory() {
        return currentTurn >= TOTAL_TURNS && !isDefeat() && stats.getPaidCount() >= 4;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : allCustomers) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public Table findTableByNo(int no) {
        for (Table t : tables) {
            if (t.getTableNo() == no) return t;
        }
        return null;
    }

    public Order findOrderById(int id) {
        for (Order o : activeOrders) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public List<Customer> getWaitingQueue() {
        return Collections.unmodifiableList(waitingQueue);
    }

    public List<Customer> getAllCustomers() {
        return Collections.unmodifiableList(allCustomers);
    }

    public List<Order> getActiveOrders() {
        return Collections.unmodifiableList(activeOrders);
    }

    public Menu getMenu() {
        return menu;
    }

    public GameStats getStats() {
        return stats;
    }
}
