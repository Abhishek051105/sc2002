package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Rigorous test suite validating all required Staff, Waiter, Cashier,
 * Table, and GameStats behaviors.
 */
public class StaffValidationTest {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("RUNNING RESTAURANT RUSH STAGE 1 VALIDATION TESTS");
        System.out.println("==================================================");

        testDoneConditionSeatingAtOccupiedTable();
        testWaiterSeatingNotInQueue();
        testWaiterOneActionPerTurnConstraint();
        testWaiterTakeOrderValidation();
        testWaiterServeValidation();
        testCashierPaymentSelectionAndTieBreaking();
        testTableAssignmentAndRelease();
        testGameStatsAndOptionalDoubleAverage();
        testSection33ExactTurnLifecycle();

        System.out.println("\n>>> ALL TESTS PASSED SUCCESSFULLY! <<<");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("ASSERTION FAILED: " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("ASSERTION FAILED: " + message + " | Expected: " + expected + ", Actual: " + actual);
    }

    /**
     * CRITICAL USER REQUIREMENT:
     * "Done when: seating at an occupied table throws and the waiter can still act afterwards"
     * "Rule: validate before calling markActed(), so a failed choice never uses up the task"
     */
    private static void testDoneConditionSeatingAtOccupiedTable() {
        System.out.print("Testing: Seating at occupied table throws and waiter can still act... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table table1 = new Table(1);
        Table table2 = new Table(2);
        Customer customer1 = new Customer(1, 1);
        Customer customer2 = new Customer(2, 1);

        // Seat customer1 at table1
        try {
            waiter.seat(customer1, table1);
        } catch (InvalidActionException e) {
            throw new AssertionError("Initial seating should have succeeded: " + e.getMessage());
        }

        assertTrue(waiter.hasActedThisTurn(), "Waiter should have acted after valid seating");
        assertEquals(customer1, table1.getCurrentCustomer(), "Customer 1 should be at table 1");
        assertFalse(table1.isFree(), "Table 1 should be occupied");

        // Start new turn
        waiter.startNewTurn();
        assertFalse(waiter.hasActedThisTurn(), "Waiter should be ready for new turn");

        // Try to seat customer2 at already occupied table1
        boolean exceptionThrown = false;
        try {
            waiter.seat(customer2, table1);
        } catch (InvalidActionException e) {
            exceptionThrown = true;
            assertEquals("Table 1 is occupied", e.getMessage(), "Exception message must match requirement");
        }
        assertTrue(exceptionThrown, "Attempt to seat at occupied table must throw InvalidActionException");

        // CRITICAL CHECK: Waiter must NOT have used up their action!
        assertFalse(waiter.hasActedThisTurn(), "Failed seating must NOT use up the waiter's task!");

        // Waiter can now choose again and successfully seat customer2 at free table2
        try {
            waiter.seat(customer2, table2);
        } catch (InvalidActionException e) {
            throw new AssertionError("Subsequent seating at free table must succeed: " + e.getMessage());
        }

        assertTrue(waiter.hasActedThisTurn(), "Waiter should now have acted after successful seating");
        assertEquals(customer2, table2.getCurrentCustomer(), "Customer 2 should be at table 2");

        System.out.println("PASSED!");
    }

    private static void testWaiterSeatingNotInQueue() {
        System.out.print("Testing: Seating customer not in queue throws... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table table1 = new Table(1);
        Customer customer3 = new Customer(3, 1);
        // Change customer state away from WAITING
        customer3.setState(CustomerState.SEATED);

        boolean exceptionThrown = false;
        try {
            waiter.seat(customer3, table1);
        } catch (InvalidActionException e) {
            exceptionThrown = true;
            assertEquals("Customer 3 is not in the queue", e.getMessage(), "Message must match queue check requirement");
        }
        assertTrue(exceptionThrown, "Must throw when customer is not in queue");
        assertFalse(waiter.hasActedThisTurn(), "Failed seating must not consume waiter action");
        System.out.println("PASSED!");
    }

    private static void testWaiterOneActionPerTurnConstraint() {
        System.out.print("Testing: Waiter at most one action per turn constraint... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Table table1 = new Table(1);
        Table table2 = new Table(2);
        Customer c1 = new Customer(1, 1);
        Customer c2 = new Customer(2, 1);

        try {
            waiter.seat(c1, table1);
        } catch (InvalidActionException e) {
            throw new AssertionError("First action should succeed");
        }

        boolean exceptionThrown = false;
        try {
            waiter.seat(c2, table2);
        } catch (InvalidActionException e) {
            exceptionThrown = true;
            assertEquals("Waiter has already acted this turn", e.getMessage(), "Message must match staff role constraint");
        }
        assertTrue(exceptionThrown, "Second action in same turn must be prevented");
        System.out.println("PASSED!");
    }

    private static void testWaiterTakeOrderValidation() {
        System.out.print("Testing: Waiter.takeOrder validation... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();
        Menu menu = new Menu();

        Customer c1 = new Customer(1, 1); // Not seated yet (WAITING)
        boolean exceptionThrown = false;
        try {
            waiter.takeOrder(c1, menu);
        } catch (InvalidActionException e) {
            exceptionThrown = true;
            assertEquals("Customer 1 is not seated", e.getMessage(), "Message must indicate customer is not seated");
        }
        assertTrue(exceptionThrown, "Cannot take order if customer is not seated");
        assertFalse(waiter.hasActedThisTurn(), "Action must not be consumed on validation failure");

        // Seat customer
        Table table = new Table(1);
        table.release();
        c1.seatAt(table);

        // Take order
        Order order = null;
        try {
            order = waiter.takeOrder(c1, menu);
        } catch (InvalidActionException e) {
            throw new AssertionError("Take order should succeed when seated");
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter has acted");
        assertEquals(CustomerState.ORDERED, c1.getState(), "Customer state must be ORDERED");
        assertEquals(order, c1.getOrder(), "Customer order reference must be set");
        System.out.println("PASSED!");
    }

    private static void testWaiterServeValidation() {
        System.out.print("Testing: Waiter.serve validation... ");
        Waiter waiter = new Waiter();
        waiter.startNewTurn();

        Customer c1 = new Customer(1, 1);
        Table table = new Table(1);
        c1.seatAt(table);
        MenuItem pasta = new MenuItem("Pasta", 18.00, 2);
        Order order = new Order(2, c1, pasta, 18.00);
        c1.placeOrder(order);

        // Order has 0/2 units cooked -> NOT READY
        boolean exceptionThrown = false;
        try {
            waiter.serve(order, 3);
        } catch (InvalidActionException e) {
            exceptionThrown = true;
            assertEquals("Order 2 is not READY yet", e.getMessage(), "Message must match order not ready requirement");
        }
        assertTrue(exceptionThrown, "Cannot serve dish before it is ready");
        assertFalse(waiter.hasActedThisTurn(), "Action must not be consumed on validation failure");

        // Chef cooks unit 1 and unit 2
        order.cookUnit();
        assertFalse(order.isReady(), "Order should still be cooking after 1 unit");
        order.cookUnit();
        assertTrue(order.isReady(), "Order should be READY after 2 units");

        // Now waiter serves
        try {
            waiter.serve(order, 4);
        } catch (InvalidActionException e) {
            throw new AssertionError("Serve should succeed when order is ready: " + e.getMessage());
        }
        assertTrue(waiter.hasActedThisTurn(), "Waiter has acted");
        assertEquals(OrderStatus.SERVED, order.getStatus(), "Order status must be SERVED");
        assertEquals(CustomerState.SERVED, c1.getState(), "Customer state must be SERVED");
        assertEquals(4, c1.getServedTurn(), "Served turn must be recorded");
        System.out.println("PASSED!");
    }

    /**
     * Cashier rule:
     * "Cashier.processNextPayment(customers, turn, stats): take the earliest readyToPayTurn below turn,
     * ties to the lower id. Call c.pay(), then stats.recordPayment(bill, sat). Return the customer, or null"
     */
    private static void testCashierPaymentSelectionAndTieBreaking() {
        System.out.print("Testing: Cashier payment priority, timing (< turn), and tie-breaking... ");
        Cashier cashier = new Cashier();
        GameStats stats = new GameStats();
        List<Customer> customerList = new ArrayList<>();

        // Customer 1: readyToPayTurn = 5
        Customer c1 = new Customer(1, 1);
        Table t1 = new Table(1);
        try { t1.assign(c1); } catch (Exception ignored) {}
        c1.seatAt(t1);
        c1.setState(CustomerState.READY_TO_PAY);
        c1.setReadyToPayTurn(5);
        MenuItem pasta = new MenuItem("Pasta", 18.00, 2);
        Order o1 = new Order(1, c1, pasta, 18.00);
        c1.placeOrder(o1);
        c1.setState(CustomerState.READY_TO_PAY); // re-set state
        customerList.add(c1);

        // Turn = 5: readyToPayTurn is 5. 5 is NOT below 5 (< 5 is false).
        Customer paidAt5 = cashier.processNextPayment(customerList, 5, stats);
        assertEquals(null, paidAt5, "Customer whose readyToPayTurn equals turn cannot pay in the same turn");

        // Add Customer 3: readyToPayTurn = 4 (earlier than Customer 1)
        Customer c3 = new Customer(3, 1);
        Table t3 = new Table(3);
        try { t3.assign(c3); } catch (Exception ignored) {}
        c3.seatAt(t3);
        c3.setState(CustomerState.READY_TO_PAY);
        c3.setReadyToPayTurn(4);
        MenuItem salad = new MenuItem("Salad", 9.00, 1);
        Order o3 = new Order(3, c3, salad, 9.00);
        c3.placeOrder(o3);
        c3.setState(CustomerState.READY_TO_PAY);
        customerList.add(c3);

        // Add Customer 2: readyToPayTurn = 4 (tied with Customer 3, but c2 has lower id!)
        Customer c2 = new Customer(2, 1);
        Table t2 = new Table(2);
        try { t2.assign(c2); } catch (Exception ignored) {}
        c2.seatAt(t2);
        c2.setState(CustomerState.READY_TO_PAY);
        c2.setReadyToPayTurn(4);
        MenuItem burger = new MenuItem("Burger", 15.00, 2);
        Order o2 = new Order(2, c2, burger, 15.00);
        c2.placeOrder(o2);
        c2.setState(CustomerState.READY_TO_PAY);
        customerList.add(c2);

        // At Turn 6:
        // Candidates with readyToPayTurn < 6:
        // c1 (readyToPayTurn 5), c3 (readyToPayTurn 4), c2 (readyToPayTurn 4)
        // Earliest readyToPayTurn is 4 (c2 and c3).
        // Tie-breaker: lower ID -> Customer 2!
        Customer paidFirst = cashier.processNextPayment(customerList, 6, stats);
        assertEquals(c2, paidFirst, "Customer 2 should be paid first (earliest turn 4, ties to lower id 2 < 3)");
        assertEquals(CustomerState.PAID, c2.getState(), "Customer 2 should be PAID");
        assertTrue(t2.isFree(), "Customer 2's table must be released upon payment");
        assertEquals(15.00, stats.getRevenue(), "Revenue should reflect customer 2's $15 bill");

        // Next payment: between c3 (turn 4) and c1 (turn 5) -> c3 has earlier turn!
        Customer paidSecond = cashier.processNextPayment(customerList, 6, stats);
        assertEquals(c3, paidSecond, "Customer 3 should be paid second (earlier turn 4 < 5)");
        assertEquals(CustomerState.PAID, c3.getState(), "Customer 3 should be PAID");
        assertTrue(t3.isFree(), "Customer 3's table must be released upon payment");
        assertEquals(24.00, stats.getRevenue(), "Revenue should be 15 + 9 = $24.00");

        // Next payment: c1 (turn 5 < 6)
        Customer paidThird = cashier.processNextPayment(customerList, 6, stats);
        assertEquals(c1, paidThird, "Customer 1 should be paid third");
        assertEquals(CustomerState.PAID, c1.getState(), "Customer 1 should be PAID");
        assertTrue(t1.isFree(), "Customer 1's table must be released upon payment");
        assertEquals(42.00, stats.getRevenue(), "Revenue should be 24 + 18 = $42.00");

        // No more ready customers
        Customer noneLeft = cashier.processNextPayment(customerList, 6, stats);
        assertEquals(null, noneLeft, "When no ready customers remain, cashier returns null");

        System.out.println("PASSED!");
    }

    private static void testTableAssignmentAndRelease() {
        System.out.print("Testing: Table lifecycle (assign, isFree, release)... ");
        Table table = new Table(1);
        assertTrue(table.isFree(), "New table must be free");
        assertEquals(1, table.getTableNo(), "Table number must match");
        assertEquals(1, table.getNumber(), "getNumber() must match");

        Customer customer = new Customer(1, 1);
        try {
            table.assign(customer);
        } catch (InvalidActionException e) {
            throw new AssertionError("Assigning to free table should not fail");
        }
        assertFalse(table.isFree(), "Table must be occupied after assign");
        assertEquals(customer, table.getCurrentCustomer(), "Customer must match");

        table.release();
        assertTrue(table.isFree(), "Table must be free after release");
        assertEquals(null, table.getCurrentCustomer(), "Current customer must be null after release");
        System.out.println("PASSED!");
    }

    private static void testGameStatsAndOptionalDoubleAverage() {
        System.out.print("Testing: GameStats OptionalDouble average satisfaction and record methods... ");
        GameStats stats = new GameStats();

        // When no one has paid yet, average satisfaction must be OptionalDouble.empty() ("N/A")
        OptionalDouble emptyAvg = stats.getAverageSatisfaction();
        assertFalse(emptyAvg.isPresent(), "Average satisfaction must be empty before any payment");
        assertEquals("N/A", stats.getFormattedAverageSatisfaction(), "Formatted string must be N/A");

        stats.recordServed();
        stats.recordServed();
        stats.recordUnhappyDeparture();
        assertEquals(2, stats.getServedCount(), "Served count should be 2");
        assertEquals(1, stats.getUnhappyDepartures(), "Unhappy count should be 1");

        // Record first payment: $18.00, sat = 76
        stats.recordPayment(18.00, 76);
        OptionalDouble avg1 = stats.getAverageSatisfaction();
        assertTrue(avg1.isPresent(), "Average satisfaction should now be present");
        assertEquals(76.0, avg1.getAsDouble(), "Average satisfaction should be 76.0");

        // Record second payment: $15.00, sat = 84
        stats.recordPayment(15.00, 84);
        OptionalDouble avg2 = stats.getAverageSatisfaction();
        assertTrue(avg2.isPresent(), "Average satisfaction should be present");
        // (76 + 84) / 2 = 80.0
        assertEquals(80.0, avg2.getAsDouble(), "Average satisfaction should be (76 + 84) / 2 = 80.0");
        assertEquals(33.00, stats.getRevenue(), "Revenue should be 18 + 15 = 33.00");
        assertEquals(2, stats.getPaidCount(), "Paid count should be 2");

        System.out.println("PASSED!");
    }

    /**
     * Recreates the exact scenario from Section 3.3 of the project specification:
     * Turn 1: Arrives (100). Waiter seats the customer. End of turn: 92 (seated, not served)
     * Turn 2: Waiter takes Pasta order. Chef does unit 1 of 2. End of turn: 84
     * Turn 3: Chef does unit 2 of 2: order is READY. Waiter cannot serve it this turn. End of turn: 76
     * Turn 4: Waiter serves: SERVED. End of turn: 76 (no further loss)
     * Turn 5: Customer finishes eating: READY_TO_PAY. End of turn: 76
     * Turn 6: Cashier collects $18.00: PAID, table freed. Satisfaction 76 is recorded.
     */
    private static void testSection33ExactTurnLifecycle() {
        System.out.print("Testing: Section 3.3 full customer lifecycle from arrival to payment... ");

        Waiter waiter = new Waiter();
        Chef chef = new Chef();
        Cashier cashier = new Cashier();
        GameStats stats = new GameStats();
        Menu menu = new Menu();
        Table table1 = new Table(1);
        MenuItem pasta = menu.getItem("Pasta");

        List<Customer> customers = new ArrayList<>();

        // TURN 1:
        // Arrives (100). Waiter seats customer.
        Customer c1 = new Customer(1, 1, pasta);
        customers.add(c1);
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        assertEquals(100, c1.getSatisfaction(), "Customer starts with satisfaction 100");
        try {
            waiter.seat(c1, table1);
        } catch (InvalidActionException e) {
            throw new AssertionError("Turn 1 seating failed: " + e.getMessage());
        }
        assertEquals(CustomerState.SEATED, c1.getState(), "Turn 1: Customer must be SEATED");
        c1.applyTurnEndSatisfactionLoss();
        assertEquals(92, c1.getSatisfaction(), "Turn 1 end: Satisfaction should be 92 (100 - 8)");

        // TURN 2:
        // Waiter takes Pasta order. Chef does unit 1 of 2.
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        Order order = null;
        try {
            order = waiter.takeOrder(c1, menu);
        } catch (InvalidActionException e) {
            throw new AssertionError("Turn 2 takeOrder failed: " + e.getMessage());
        }
        try {
            chef.cook(order);
        } catch (InvalidActionException e) {
            throw new AssertionError("Turn 2 chef cooking failed: " + e.getMessage());
        }
        assertEquals(1, order.getCompletedUnits(), "Chef completed unit 1 of 2");
        assertFalse(order.isReady(), "Order is not ready after 1 unit");
        c1.applyTurnEndSatisfactionLoss();
        assertEquals(84, c1.getSatisfaction(), "Turn 2 end: Satisfaction should be 84 (92 - 8)");

        // TURN 3:
        // Chef does unit 2 of 2: order is READY. Waiter cannot serve it this turn.
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        try {
            chef.cook(order);
        } catch (InvalidActionException e) {
            throw new AssertionError("Turn 3 chef cooking failed: " + e.getMessage());
        }
        assertTrue(order.isReady(), "Turn 3: Chef completed unit 2 of 2, order is READY");
        // Waiter does not serve this turn (manager's attention or constraint)
        c1.applyTurnEndSatisfactionLoss();
        assertEquals(76, c1.getSatisfaction(), "Turn 3 end: Satisfaction should be 76 (84 - 8)");

        // TURN 4:
        // Waiter serves: SERVED. Satisfaction at turn end: 76 (no further loss)
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        try {
            waiter.serve(order, 4);
        } catch (InvalidActionException e) {
            throw new AssertionError("Turn 4 serve failed: " + e.getMessage());
        }
        assertEquals(CustomerState.SERVED, c1.getState(), "Turn 4: Customer state is SERVED");
        stats.recordServed();
        c1.applyTurnEndSatisfactionLoss();
        assertEquals(76, c1.getSatisfaction(), "Turn 4 end: Satisfaction remains 76 (no further loss)");

        // TURN 5:
        // Customer finishes eating: READY_TO_PAY. Satisfaction at turn end: 76
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        c1.updateEatingStatus(5);
        assertEquals(CustomerState.READY_TO_PAY, c1.getState(), "Turn 5: Customer state is READY_TO_PAY");
        assertEquals(5, c1.getReadyToPayTurn(), "Turn 5: readyToPayTurn is 5");
        c1.applyTurnEndSatisfactionLoss();
        assertEquals(76, c1.getSatisfaction(), "Turn 5 end: Satisfaction remains 76");

        // TURN 6:
        // Cashier collects $18.00: PAID, table freed. Satisfaction 76 is recorded.
        waiter.startNewTurn();
        chef.startNewTurn();
        cashier.startNewTurn();

        Customer paidCustomer = cashier.processNextPayment(customers, 6, stats);
        assertEquals(c1, paidCustomer, "Turn 6: Cashier collects from customer 1");
        assertEquals(CustomerState.PAID, c1.getState(), "Turn 6: Customer state is PAID");
        assertTrue(table1.isFree(), "Turn 6: Table 1 is freed upon payment");
        assertEquals(18.00, stats.getRevenue(), "Turn 6: Cashier collects $18.00");
        assertTrue(stats.getAverageSatisfaction().isPresent(), "Satisfaction is recorded");
        assertEquals(76.0, stats.getAverageSatisfaction().getAsDouble(), "Satisfaction 76 is recorded");

        System.out.println("PASSED!");
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError("ASSERTION FAILED: " + message);
        }
    }
}
