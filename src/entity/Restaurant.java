package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container entity holding restaurant state: tables, waiting queue, staff, menu, and stats.
 * Provides lookup methods only to prevent God-class anti-patterns.
 */
public class Restaurant {
    private final List<Table> tables;
    private final List<Customer> waitingQueue;
    private final List<Customer> allCustomers;
    private final List<Order> activeOrders;
    private final Menu menu;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private final GameStats stats;

    public Restaurant() {
        this.tables = new ArrayList<>();
        // Stage 1 configuration: exactly 2 tables
        this.tables.add(new Table(1));
        this.tables.add(new Table(2));

        this.waitingQueue = new ArrayList<>();
        this.allCustomers = new ArrayList<>();
        this.activeOrders = new ArrayList<>();

        this.menu = Menu.createDefault();
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.cashier = new Cashier();
        this.stats = new GameStats();
    }

    public void addArrival(Customer c) {
        waitingQueue.add(c);
        allCustomers.add(c);
    }

    public void removeFromQueue(Customer c) {
        waitingQueue.remove(c);
    }

    public Customer findCustomer(int id) {
        for (Customer c : allCustomers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Table getTable(int no) {
        for (Table t : tables) {
            if (t.getTableNo() == no) {
                return t;
            }
        }
        return null;
    }

    public Order findOrder(int id) {
        for (Order o : activeOrders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order o) {
        activeOrders.add(o);
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

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public GameStats getStats() {
        return stats;
    }
}
