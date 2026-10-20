package boundary;

import control.Objective;
import entity.Customer;
import entity.GameStats;
import entity.Money;
import entity.Restaurant;
import entity.Table;
import entity.WaiterTask;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Text-based user interface for Restaurant Rush.
 * Handles display and robust input parsing without enforcing game rules.
 */
public class ConsoleUI {
    private final Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public ConsoleUI() {
        this(new Scanner(System.in));
    }

    /**
     * Reads an integer within [min, max], handling non-numeric junk, blanks,
     * and stream exhaustion gracefully.
     */
    public int readInt(int min, int max) {
        while (true) {
            try {
                if (!scanner.hasNextLine()) {
                    throw new NoSuchElementException("Input stream ended");
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input. Please enter a number between %d and %d: ", min, max);
            }
        }
    }

    public void showStartOfTurn(int turn, Restaurant restaurant) {
        GameStats stats = restaurant.getStats();
        System.out.println("\n========================================================");
        System.out.printf("TURN %d / 18 | Revenue: %s | Avg Satisfaction: %s%n",
                turn, Money.format(stats.getRevenueCents()), stats.getFormattedAverageSatisfaction());
        System.out.println("========================================================");

        // Waiting queue
        System.out.print("Queue: ");
        if (restaurant.getWaitingQueue().isEmpty()) {
            System.out.println("[Empty]");
        } else {
            for (Customer c : restaurant.getWaitingQueue()) {
                System.out.printf("C%d(sat:%d) ", c.getId(), c.getSatisfaction());
            }
            System.out.println();
        }

        // Tables
        System.out.println("Tables:");
        for (Table t : restaurant.getTables()) {
            if (t.isFree()) {
                System.out.printf("  Table %d: [Free]%n", t.getTableNo());
            } else {
                Customer c = t.getCurrentCustomer();
                String orderInfo = c.getOrder() != null ? c.getOrder().getStatus().toString() : "NO_ORDER";
                System.out.printf("  Table %d: Occupied by C%d (sat:%d, status:%s, order:%s)%n",
                        t.getTableNo(), c.getId(), c.getSatisfaction(), c.getStatus(), orderInfo);
            }
        }

        // Staff availability
        System.out.printf("Staff Available: Waiter=%s, Chef=%s%n",
                !restaurant.getWaiter().hasActedThisTurn() ? "READY" : "DONE",
                !restaurant.getChef().hasActedThisTurn() ? "READY" : "DONE");
    }

    public WaiterTask askWaiterTask() {
        System.out.println("\nWaiter Action:");
        System.out.println("  1. Seat a customer");
        System.out.println("  2. Take order from seated customer");
        System.out.println("  3. Serve ready order");
        System.out.println("  4. Wait (pass)");
        System.out.print("Choose action (1-4): ");
        int choice = readInt(1, 4);
        switch (choice) {
            case 1: return WaiterTask.SEAT;
            case 2: return WaiterTask.TAKE_ORDER;
            case 3: return WaiterTask.SERVE;
            default: return WaiterTask.WAIT;
        }
    }

    public int askCustomerId(String prompt) {
        System.out.print(prompt);
        return readInt(1, 99);
    }

    public int askTableNo(String prompt) {
        System.out.print(prompt);
        return readInt(1, 2);
    }

    public int askOrderIdOrWait(String prompt) {
        System.out.print(prompt);
        // 0 = Wait / Pass
        return readInt(0, 99);
    }

    public void showError(String message) {
        System.out.println("[ACTION FAILED] " + message + ". Please choose again.");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showEndOfTurn(int turn, Restaurant restaurant) {
        GameStats stats = restaurant.getStats();
        System.out.println("\n--- Turn " + turn + " End Summary ---");
        System.out.printf("Dishes Served:        %d%n", stats.getServedCount());
        System.out.printf("Unhappy Departures:   %d%n", stats.getDepartureCount());
        System.out.printf("Current Revenue:      %s%n", Money.format(stats.getRevenueCents()));
        System.out.printf("Paid Customers Count: %d%n", stats.getPaidCount());
        System.out.printf("Average Satisfaction: %s%n", stats.getFormattedAverageSatisfaction());
        System.out.println("-----------------------------");
    }

    public void showFinalResult(Restaurant restaurant, Objective objective) {
        GameStats stats = restaurant.getStats();
        boolean victory = objective.isMet(stats);

        System.out.println("\n========================================================");
        System.out.println(victory ? "               *** GAME RESULT: VICTORY ***" : "               *** GAME RESULT: DEFEAT ***");
        System.out.println("========================================================");
        System.out.printf("1. Paid Customers (>= 4):        %d  [%s]%n",
                stats.getPaidCount(), objective.isPaidMet(stats) ? "PASSED" : "FAILED");
        System.out.printf("2. Total Revenue (>= $45.00):     %s  [%s]%n",
                Money.format(stats.getRevenueCents()), objective.isRevenueMet(stats) ? "PASSED" : "FAILED");
        System.out.printf("3. Average Satisfaction (>= 60):  %s  [%s]%n",
                stats.getFormattedAverageSatisfaction(), objective.isSatisfactionMet(stats) ? "PASSED" : "FAILED");
        System.out.println("--------------------------------------------------------");
        System.out.println("Final Outcome: " + (victory ? "VICTORY" : "DEFEAT"));
        System.out.println("========================================================");
    }
}
