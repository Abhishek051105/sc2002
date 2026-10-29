package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the game state: lookups plus the clean-up when a customer leaves
 * (customerLeft). No turn rules, so it does not become a God class.
 * Owns tables/menu/staff/stats (composition).
 */
public class Restaurant {
    private final List<Table> tables = new ArrayList<>();
    private final List<Customer> waitingQueue = new ArrayList<>();
    private final List<Customer> allCustomers = new ArrayList<>();
    private final List<Order> allOrders = new ArrayList<>();
    private final Menu menu;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private final GameStats stats;

    /** Builds numTables tables, the default menu, the 3 staff and stats. */
    public Restaurant(int numTables) {
        for(int i =0; i<numTables;i++){
            tables.add(new Table(i+1));
        }
        menu = new Menu();
        waiter = new Waiter();
        chef = new Chef();
        cashier = new Cashier();
        stats = new GameStats();
    }

    /** New arrival joins the queue and the all-customers list. */
    public void addArrival(Customer c) { 
        waitingQueue.add(c);
        allCustomers.add(c);
    }
    public void removeFromQueue(Customer c) {
        waitingQueue.remove(c);
    }

    /**
     * Clean-up when a customer gets fed up and leaves:
     * take them out of the queue, cancel their order and free their table.
     * A leaving customer was never served, so cancel() is always allowed.
     */
    public void customerLeft(Customer c) {
        removeFromQueue(c);

        Order o = findOrder(c.getCustomerId());
        if (o != null) {
            o.cancel();
        }

        Table t = findTableOf(c);
        if (t != null) {
            t.release();
        }
    }


    public List<Customer> getWaitingQueue() { 
        return Collections.unmodifiableList(waitingQueue);
    }
 
    public List<Customer> getAllCustomers() { 
    return Collections.unmodifiableList(allCustomers);    
    }
  
    public List<Table> getTables() { 
        return Collections.unmodifiableList(tables);
    }

    public List<Staff> getAllStaff() { 
        return List.of(waiter,chef,cashier);
    }

    /** null if not found (matches getCustomerId()). */
    public Customer findCustomer(int id) { 
        Customer found = allCustomers.stream()
                .filter(n->n.getCustomerId() == id)
                .findFirst()
                .orElse(null);
        return found;
    }
    /** null if not found. Order id == customer id. */
    public Order findOrder(int orderId) {
        return allOrders.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }
    /** 1-based; null if out of range. */
    public Table getTable(int tableNo) {
        if (tableNo < 1 || tableNo > tables.size()) {
            return null;
        }
        return tables.get(tableNo - 1);
    }
    /** The table this customer sits at, or null. */
    public Table findTableOf(Customer c) {
        return tables.stream()
                .filter(t -> t.getOccupant() == c)
                .findFirst()
                .orElse(null);
    }
    public void addOrder(Order o) { 
        allOrders.add(o);
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
