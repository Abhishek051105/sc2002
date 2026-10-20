package control;

import entity.Cashier;
import entity.Customer;
import entity.CustomerStatus;
import entity.GameStats;
import entity.InvalidActionException;
import entity.Menu;
import entity.MenuItem;
import entity.Money;
import entity.Order;
import entity.OrderStatus;
import entity.RegularCustomer;
import entity.Table;
import entity.Waiter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Acceptance test suite validating all specifications for Aekas' role:
 * Staff, Waiter, Cashier, Table, GameStats, and InvalidActionException.
 */
public class AekasValidationTest {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("  RUNNING AEKAS VALIDATION TEST SUITE (STAGE 1)     ");
        System.out.println("====================================================");

        testDoneConditionSeatingAtOccupiedTable();
        testWaiterSeatNotInQueue();
        testWaiterSingleActionPerTurn();
        testWaiterTakeOrder();
        testWaiterServe();
        testCashierTimingAndTieBreaking();
        testTableOperations();
        testGameStats();
        testMoneyUtility();
        testBriefSection33Trace();

        System.out.println("\n>>> ALL AEKAS TESTS PASSED WITH ZERO ERRORS! <<<");
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError("ASSERTION FAILED: " + msg);
    }

    private static void assertFalse(boolean cond, String msg) {
        if (cond) throw new AssertionError("ASSERTION FAILED: " + msg);
    }

    private static void assertEquals(Object expected, Object actual, String msg) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("ASSERTION FAILED: " + msg + " (expected: " + expected + ", actual: " + actual + ")");
    }

    /**
     * Requirement: "Done when: seating at an occupied table throws and the waiter can still act afterwards"
     * "Rule: validate before calling markActed(), so a failed choice never uses up the task"
     */
    private static void testDoneConditionSeatingAtOccupiedTable() {
        System.out.print("Testing: seating at occupied table throws and waiter can still act... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table t1 = new Table(1);
        Table t2 = new Table(2);
        Customer c1 = new RegularCustomer(1);
        Customer c2 = new RegularCustomer(2);

        // Seat c1 at t1 successfully
        try {
            waiter.seat(c1, t1);
        } catch (InvalidActionException e) {
            throw new AssertionError("Valid seating failed: " + e.getMessage());
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter has acted after successful seating");
        assertEquals(CustomerStatus.SEATED, c1.getStatus(), "Customer 1 must be SEATED");
        assertFalse(t1.isFree(), "Table 1 must be occupied");

        // Start new turn
        waiter.startNewTurn();
        assertFalse(waiter.hasActedThisTurn(), "Waiter ready for new turn");

        // Try to seat c2 at occupied t1
        boolean threw = false;
        try {
            waiter.seat(c2, t1);
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Table 1 is occupied", e.getMessage(), "Exception message mismatch");
        }
        assertTrue(threw, "Seating at occupied table must throw InvalidActionException");

        // CRITICAL CHECK: Waiter must NOT have acted!
        assertFalse(waiter.hasActedThisTurn(), "Failed choice must NOT use up the staff task");

        // Waiter can still act afterwards
        try {
            waiter.seat(c2, t2);
        } catch (InvalidActionException e) {
            throw new AssertionError("Subsequent seating at free table must succeed: " + e.getMessage());
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter acted after subsequent valid seating");
        assertEquals(CustomerStatus.SEATED, c2.getStatus(), "Customer 2 must be SEATED");
        assertFalse(t2.isFree(), "Table 2 must be occupied");

        System.out.println("PASSED!");
    }

    private static void testWaiterSeatNotInQueue() {
        System.out.print("Testing: seating customer not in queue throws... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table t1 = new Table(1);
        Customer c3 = new RegularCustomer(3);
        // Force state away from WAITING
        Table dummy = new Table(99);
        try { dummy.assign(c3); } catch (Exception ignored) {}
        c3.seatAt(dummy);

        boolean threw = false;
        try {
            waiter.seat(c3, t1);
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Customer 3 is not in the queue", e.getMessage(), "Message must match exactly");
        }
        assertTrue(threw, "Must throw when customer is not in queue");
        assertFalse(waiter.hasActedThisTurn(), "Failed choice must not use up task");
        System.out.println("PASSED!");
    }

    private static void testWaiterSingleActionPerTurn() {
        System.out.print("Testing: staff checkCanAct() throws 'Waiter has already acted this turn'... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table t1 = new Table(1);
        Customer c1 = new RegularCustomer(1);
        try {
            waiter.seat(c1, t1);
        } catch (InvalidActionException e) {
            throw new AssertionError(e.getMessage());
        }

        boolean threw = false;
        try {
            waiter.checkCanAct();
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Waiter has already acted this turn", e.getMessage(), "Staff exception message mismatch");
        }
        assertTrue(threw, "Must throw when acting twice");
        System.out.println("PASSED!");
    }

    private static void testWaiterTakeOrder() {
        System.out.print("Testing: Waiter.takeOrder validation and order creation... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();
        Menu menu = Menu.createDefault();

        Customer c1 = new RegularCustomer(1); // WAITING
        boolean threw = false;
        try {
            waiter.takeOrder(c1, menu);
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Customer 1 is not seated", e.getMessage(), "Must reject unseated customer");
        }
        assertTrue(threw, "Must throw when taking order from unseated customer");
        assertFalse(waiter.hasActedThisTurn(), "Failed choice must not use task");

        Table t1 = new Table(1);
        try { waiter.seat(c1, t1); } catch (Exception ignored) {}

        waiter.startNewTurn();
        Order order = null;
        try {
            order = waiter.takeOrder(c1, menu);
        } catch (InvalidActionException e) {
            throw new AssertionError("takeOrder failed: " + e.getMessage());
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter has acted");
        assertEquals(CustomerStatus.ORDERED, c1.getStatus(), "Customer status must be ORDERED");
        assertEquals(c1.getId(), order.getId(), "Order ID should equal Customer ID");
        // Customer 1 chooses menu[(1-1)%3] = menu[0] = Salad ($9.00 = 900 cents)
        assertEquals("Salad", order.getItem().getName(), "Customer 1 must choose Salad");
        assertEquals(900, order.getBillCents(), "Salad bill must be 900 cents");
        assertEquals(OrderStatus.IN_PREPARATION, order.getStatus(), "Initial order status must be IN_PREPARATION");

        System.out.println("PASSED!");
    }

    private static void testWaiterServe() {
        System.out.print("Testing: Waiter.serve ready check and status transition... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Customer c2 = new RegularCustomer(2);
        Table t2 = new Table(2);
        try { t2.assign(c2); } catch (Exception ignored) {}
        c2.seatAt(t2);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        Order o2 = new Order(2, c2, burger, 1500);
        c2.placeOrder(o2);

        // Not ready yet (unitsDone = 0, prepUnits = 1)
        boolean threw = false;
        try {
            waiter.serve(o2, 3);
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Order 2 is not READY yet", e.getMessage(), "Must match Order 2 is not READY yet");
        }
        assertTrue(threw, "Cannot serve before READY");
        assertFalse(waiter.hasActedThisTurn(), "Failed serve must not use task");

        // Advance cooking unit
        o2.advanceOneUnit();
        assertTrue(o2.isReady(), "Order must now be READY");

        try {
            waiter.serve(o2, 3);
        } catch (InvalidActionException e) {
            throw new AssertionError("Serve failed: " + e.getMessage());
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter has acted");
        assertEquals(OrderStatus.SERVED, o2.getStatus(), "Order status must be SERVED");
        assertEquals(CustomerStatus.EATING, c2.getStatus(), "Customer status must be EATING");
        assertEquals(3, c2.getServedTurn(), "Customer servedTurn must be 3");

        System.out.println("PASSED!");
    }

    /**
     * Requirement: "Cashier.processNextPayment(customers, turn, stats): take the earliest readyToPayTurn below turn,
     * ties to the lower id. Call c.pay(), then stats.recordPayment(bill, sat). Return the customer, or null"
     */
    private static void testCashierTimingAndTieBreaking() {
        System.out.print("Testing: Cashier timing (readyToPayTurn < turn) and tie-breaking... ");
        Cashier cashier = new Cashier();
        GameStats stats = new GameStats();
        List<Customer> customers = new ArrayList<>();

        // Customer 1: readyToPayTurn = 5
        Customer c1 = new RegularCustomer(1);
        Table t1 = new Table(1);
        try { t1.assign(c1); } catch (Exception ignored) {}
        c1.seatAt(t1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);
        Order o1 = new Order(1, c1, pasta, 1800);
        c1.placeOrder(o1);
        o1.advanceOneUnit();
        o1.advanceOneUnit();
        o1.markServed();
        c1.markServed(4);
        c1.finishEatingIfDue(5); // readyToPayTurn = 5
        customers.add(c1);

        // Turn 5: readyToPayTurn is 5. 5 is NOT < 5, so cashier cannot charge yet!
        Customer paidAt5 = cashier.processNextPayment(customers, 5, stats);
        assertEquals(null, paidAt5, "Customer cannot pay in the turn they finish eating (must be < turn)");

        // Customer 3: readyToPayTurn = 4
        Customer c3 = new RegularCustomer(3);
        Table t3 = new Table(3);
        try { t3.assign(c3); } catch (Exception ignored) {}
        c3.seatAt(t3);
        MenuItem salad = new MenuItem("Salad", 900, 1);
        Order o3 = new Order(3, c3, salad, 900);
        c3.placeOrder(o3);
        o3.advanceOneUnit();
        o3.markServed();
        c3.markServed(3);
        c3.finishEatingIfDue(4); // readyToPayTurn = 4
        customers.add(c3);

        // Customer 2: readyToPayTurn = 4 (ties with Customer 3, but lower ID 2 < 3)
        Customer c2 = new RegularCustomer(2);
        Table t2 = new Table(2);
        try { t2.assign(c2); } catch (Exception ignored) {}
        c2.seatAt(t2);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        Order o2 = new Order(2, c2, burger, 1500);
        c2.placeOrder(o2);
        o2.advanceOneUnit();
        o2.markServed();
        c2.markServed(3);
        c2.finishEatingIfDue(4); // readyToPayTurn = 4
        customers.add(c2);

        // At Turn 6: candidates are c1(5), c3(4), c2(4).
        // Earliest ready turn is 4 (c2 and c3). Tie-break: lowest ID -> Customer 2!
        Customer firstPaid = cashier.processNextPayment(customers, 6, stats);
        assertEquals(c2, firstPaid, "Customer 2 should pay first due to lower ID tie-breaker");
        assertEquals(CustomerStatus.PAID, c2.getStatus(), "Customer 2 must be PAID");
        assertTrue(t2.isFree(), "Customer 2 table must be freed");
        assertEquals(1500, stats.getRevenueCents(), "Revenue should be 1500 cents ($15.00)");

        // Next payment: c3 (ready turn 4) vs c1 (ready turn 5) -> c3 is earlier!
        Customer secondPaid = cashier.processNextPayment(customers, 6, stats);
        assertEquals(c3, secondPaid, "Customer 3 should pay second (ready turn 4 < 5)");
        assertEquals(CustomerStatus.PAID, c3.getStatus(), "Customer 3 must be PAID");
        assertTrue(t3.isFree(), "Customer 3 table must be freed");
        assertEquals(2400, stats.getRevenueCents(), "Revenue should be 1500 + 900 = 2400 cents ($24.00)");

        // Next payment: c1 (ready turn 5 < 6)
        Customer thirdPaid = cashier.processNextPayment(customers, 6, stats);
        assertEquals(c1, thirdPaid, "Customer 1 should pay third");
        assertEquals(CustomerStatus.PAID, c1.getStatus(), "Customer 1 must be PAID");
        assertTrue(t1.isFree(), "Customer 1 table must be freed");
        assertEquals(4200, stats.getRevenueCents(), "Revenue should be 2400 + 1800 = 4200 cents ($42.00)");

        // No more ready customers
        Customer none = cashier.processNextPayment(customers, 6, stats);
        assertEquals(null, none, "No more customers to pay");

        System.out.println("PASSED!");
    }

    private static void testTableOperations() {
        System.out.print("Testing: Table operations (isFree, assign, release)... ");
        Table table = new Table(1);
        assertTrue(table.isFree(), "New table must be free");
        assertEquals(1, table.getTableNo(), "Table number must be 1");

        Customer c = new RegularCustomer(1);
        try {
            table.assign(c);
        } catch (InvalidActionException e) {
            throw new AssertionError("Assigning to free table failed: " + e.getMessage());
        }
        assertFalse(table.isFree(), "Table must be occupied");
        assertEquals(c, table.getCurrentCustomer(), "Current customer must match");

        boolean threw = false;
        try {
            table.assign(new RegularCustomer(2));
        } catch (InvalidActionException e) {
            threw = true;
            assertEquals("Table 1 is occupied", e.getMessage(), "Must throw Table 1 is occupied");
        }
        assertTrue(threw, "Assigning to occupied table must throw");

        table.release();
        assertTrue(table.isFree(), "Table must be free after release");
        assertEquals(null, table.getCurrentCustomer(), "Current customer must be null");
        System.out.println("PASSED!");
    }

    private static void testGameStats() {
        System.out.print("Testing: GameStats OptionalDouble average satisfaction and records... ");
        GameStats stats = new GameStats();

        // Empty average returns OptionalDouble.empty() and "N/A"
        OptionalDouble avg0 = stats.getAverageSatisfaction();
        assertFalse(avg0.isPresent(), "Average must be empty before any payment");
        assertEquals("N/A", stats.getFormattedAverageSatisfaction(), "Formatted average must be N/A");

        stats.recordServed();
        stats.recordDeparture();
        assertEquals(1, stats.getServedCount(), "Served count must be 1");
        assertEquals(1, stats.getDepartureCount(), "Departure count must be 1");

        // Record first payment: $18.00 (1800 cents), sat = 76
        stats.recordPayment(1800, 76);
        OptionalDouble avg1 = stats.getAverageSatisfaction();
        assertTrue(avg1.isPresent(), "Average must be present after payment");
        assertEquals(76.0, avg1.getAsDouble(), "Average must be 76.0");

        // Record second payment: $15.00 (1500 cents), sat = 84
        stats.recordPayment(1500, 84);
        OptionalDouble avg2 = stats.getAverageSatisfaction();
        // (76 + 84) / 2 = 80.0
        assertEquals(80.0, avg2.getAsDouble(), "Average must be 80.0");
        assertEquals(3300, stats.getRevenueCents(), "Revenue must be 3300 cents");
        assertEquals(2, stats.getPaidCount(), "Paid count must be 2");

        System.out.println("PASSED!");
    }

    private static void testMoneyUtility() {
        System.out.print("Testing: Money.format cents to dollars... ");
        assertEquals("$15.00", Money.format(1500), "1500 cents must be $15.00");
        assertEquals("$24.30", Money.format(2430), "2430 cents must be $24.30");
        assertEquals("$9.00", Money.format(900), "900 cents must be $9.00");
        assertEquals("$0.00", Money.format(0), "0 cents must be $0.00");
        System.out.println("PASSED!");
    }

    /**
     * Recreates the exact scenario from Section 3.3 of the brief:
     * Regular customer ordering Pasta (2 prep units).
     * Turn 1: Arrives (100). Waiter seats. Satisfaction 92.
     * Turn 2: Waiter takes Pasta order. Chef does 1/2. Satisfaction 84.
     * Turn 3: Chef does 2/2 -> READY. Waiter cannot serve. Satisfaction 76.
     * Turn 4: Waiter serves: SERVED. Satisfaction 76 (no further loss).
     * Turn 5: Customer finishes eating: READY_TO_PAY. Satisfaction 76.
     * Turn 6: Cashier collects $18.00: PAID, table freed. Satisfaction 76 recorded.
     */
    private static void testBriefSection33Trace() {
        System.out.print("Testing: Brief Section 3.3 exact customer trace (Pasta customer)... ");
        Customer c1 = new RegularCustomer(3); // id 3 -> menu[(3-1)%3] = menu[2] = Pasta (1800 cents, 2 units)
        Menu menu = Menu.createDefault();
        Table t1 = new Table(1);
        Waiter waiter = new Waiter();
        entity.Chef chef = new entity.Chef();
        Cashier cashier = new Cashier();
        GameStats stats = new GameStats();
        List<Customer> list = new ArrayList<>();
        list.add(c1);

        // Turn 1: Arrives (100). Waiter seats.
        waiter.startNewTurn();
        assertEquals(100, c1.getSatisfaction(), "Starts at 100");
        try { waiter.seat(c1, t1); } catch (Exception e) { throw new AssertionError(e); }
        assertEquals(CustomerStatus.SEATED, c1.getStatus(), "Seated");
        c1.applyEndOfTurnWaiting();
        assertEquals(92, c1.getSatisfaction(), "Turn 1 end sat: 92");

        // Turn 2: Waiter takes Pasta order. Chef does 1 of 2.
        waiter.startNewTurn();
        chef.startNewTurn();
        Order order = null;
        try {
            order = waiter.takeOrder(c1, menu);
            chef.prepare(order);
        } catch (Exception e) { throw new AssertionError(e); }
        assertEquals(1, order.getUnitsDone(), "1 unit done");
        assertFalse(order.isReady(), "Not ready");
        c1.applyEndOfTurnWaiting();
        assertEquals(84, c1.getSatisfaction(), "Turn 2 end sat: 84");

        // Turn 3: Chef does 2 of 2: READY. Waiter cannot serve.
        waiter.startNewTurn();
        chef.startNewTurn();
        try { chef.prepare(order); } catch (Exception e) { throw new AssertionError(e); }
        assertTrue(order.isReady(), "Ready after unit 2");
        c1.applyEndOfTurnWaiting();
        assertEquals(76, c1.getSatisfaction(), "Turn 3 end sat: 76");

        // Turn 4: Waiter serves: SERVED (EATING).
        waiter.startNewTurn();
        try { waiter.serve(order, 4); } catch (Exception e) { throw new AssertionError(e); }
        assertEquals(CustomerStatus.EATING, c1.getStatus(), "Customer is EATING");
        c1.applyEndOfTurnWaiting(); // EATING customers do not lose satisfaction
        assertEquals(76, c1.getSatisfaction(), "Turn 4 end sat: 76 (no loss)");

        // Turn 5: Customer finishes eating: READY_TO_PAY.
        waiter.startNewTurn();
        c1.finishEatingIfDue(5);
        assertEquals(CustomerStatus.READY_TO_PAY, c1.getStatus(), "Finished eating -> READY_TO_PAY");
        assertEquals(5, c1.getReadyToPayTurn(), "readyToPayTurn is 5");
        c1.applyEndOfTurnWaiting();
        assertEquals(76, c1.getSatisfaction(), "Turn 5 end sat: 76");

        // Turn 6: Cashier collects $18.00: PAID, table freed.
        waiter.startNewTurn();
        Customer paid = cashier.processNextPayment(list, 6, stats);
        assertEquals(c1, paid, "Customer 1 paid");
        assertEquals(CustomerStatus.PAID, c1.getStatus(), "Status is PAID");
        assertTrue(t1.isFree(), "Table 1 freed");
        assertEquals(1800, stats.getRevenueCents(), "Revenue is 1800 cents ($18.00)");
        assertEquals(76.0, stats.getAverageSatisfaction().getAsDouble(), "Satisfaction 76 recorded");

        System.out.println("PASSED!");
    }
}
