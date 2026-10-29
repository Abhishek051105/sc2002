package boundary;

import control.Objective;
import entity.Customer;
import entity.GameStats;
import entity.Money;
import entity.Order;
import entity.Restaurant;
import entity.Table;
import entity.WaiterTask;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.Scanner;

/** Input and output only. Never decides whether an action is allowed. */
public class ConsoleUI {
    private final Scanner scanner;
    private final int maxTurns;

    public ConsoleUI(Scanner scanner, int maxTurns) {
        this.scanner = scanner;
        this.maxTurns = maxTurns;
    }

    /** Turn, revenue, avg (or N/A), queue, tables, which staff can still act. */
    public void showStartOfTurn(int turn, Restaurant restaurant) {
        GameStats stats = restaurant.getStats();

        System.out.println("");
        System.out.println("========================================");
        System.out.println("                TURN " + turn + " / " + maxTurns);
        System.out.println("========================================");

        // Revenue & Satisfaction
        String revenueText = Money.format(stats.getRevenueCents());
        String avgText = averageText(stats);

        System.out.println("Revenue: " + revenueText + " | Average Satisfaction (Customer has paid): " + avgText);
        System.out.println("----------------------------------------");

        // Waiting Queue
        List<Customer> queue = restaurant.getWaitingQueue();
        System.out.print("Waiting Queue (" + queue.size() + "): ");
        if (queue.isEmpty()) {
            System.out.println("Empty");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Customer c : queue) {
                sb.append("[C").append(c.getCustomerId())
                  .append(" (Sat: ").append(c.getSatisfaction()).append(")] ");
            }
            System.out.println(sb.toString().trim());
        }

        // Tables Status
        System.out.println("Tables:");
        for (Table t : restaurant.getTables()) {
            if (t.isFree()) {
                System.out.println("  Table " + t.getTableNo() + ": [EMPTY]");
            } else {
                Customer c = t.getOccupant();
                String orderInfo = "No Order";
                Order o = restaurant.findOrder(c.getCustomerId());
                if (o != null) {
                    orderInfo = o.getItem().getName() + " (" + o.getStatus() + ", " 
                              + o.getUnitsDone() + "/" + o.getItem().getPrepUnits() + " units)";
                }
                System.out.println("  Table " + t.getTableNo() + ": Seated C" + c.getCustomerId() 
                        + " (Sat: " + c.getSatisfaction() + ", Status: " + c.getStatus() 
                        + ") | Order: " + orderInfo);
            }
        }

        // Staff Availability
        System.out.println("Staff Available:");
        System.out.println("  Waiter : " + (restaurant.getWaiter().hasActedThisTurn() ? "Acted" : "Ready"));
        System.out.println("  Chef   : " + (restaurant.getChef().hasActedThisTurn() ? "Acted" : "Ready"));
        System.out.println("  Cashier: Ready ");
        System.out.println("----------------------------------------");
    }
    
    /** Served, departures, revenue, avg, turns used. */
    public void showEndOfTurn(int turn, Restaurant r) {
        GameStats stats = r.getStats();

        System.out.println("");
        System.out.println("--- End of Turn " + turn + " Summary ---");
        System.out.println("Turns Used       : " + turn + " / " + maxTurns);
        System.out.println("Served Count     : " + stats.getServedCount());
        System.out.println("Departures (Lost): " + stats.getUnhappyDepartures());
        System.out.println("Total Revenue    : " + Money.format(stats.getRevenueCents()));
        System.out.println("Avg Satisfaction : " + averageText(stats));
        System.out.println("--------------------------------");
    }

    /**
     * VICTORY/DEFEAT plus which targets passed or failed.
     * The pass/fail decisions come from Objective, so the win rule
     * only lives in one place.
     */
    public void showFinalResult(boolean won, GameStats stats, Objective objective) {
        System.out.println("");
        System.out.println("========================================");
        if (won) {
            System.out.println("                VICTORY");
        } else {
            System.out.println("                DEFEAT");
        }
        System.out.println("========================================");

        System.out.println("1. Paid Customers >= " + objective.getMinPaidCustomers()
                + " : " + stats.getPaidCount()
                + " (" + passOrFail(objective.paidTargetMet(stats)) + ")");

        System.out.println("2. Revenue >= " + Money.format(objective.getMinRevenueCents())
                + " : " + Money.format(stats.getRevenueCents())
                + " (" + passOrFail(objective.revenueTargetMet(stats)) + ")");

        System.out.println("3. Avg Satisfaction >= " + objective.getMinAvgSatisfaction()
                + " : " + averageText(stats)
                + " (" + passOrFail(objective.satisfactionTargetMet(stats)) + ")");

        System.out.println("========================================");
    }

    public WaiterTask askWaiterTask() {
        System.out.println("");
        System.out.println("--- Waiter Action ---");
        System.out.println("1: Seat a customer");
        System.out.println("2: Take an order");
        System.out.println("3: Serve ready food");
        System.out.println("4: Wait (Do nothing)");
        System.out.print("Choose one action (1-4): ");

        int choice = readInt(1, 4);
        switch (choice) {
            case 1:
                return WaiterTask.SEAT;
            case 2:
                return WaiterTask.TAKE_ORDER;
            case 3:
                return WaiterTask.SERVE;
            case 4:
            default:
                return WaiterTask.WAIT;
        }
    }

    public int askCustomerId(String prompt) {
        System.out.print(prompt != null && !prompt.isEmpty() ? prompt : "Enter Customer ID: ");
        return readInt(1, 100);
    }

    public int askTableNo(int numTables) {
        System.out.print("Enter Table Number (1-" + numTables + "): ");
        return readInt(1, numTables);
    }

    /** 0 = Wait */
    public int askOrderIdOrWait() {
        System.out.println("");
        System.out.println("--- Chef Action ---");
        System.out.print("Enter Order ID to cook (or 0 to Wait): ");
        return readInt(0, 100);
    }

    /** Prints the domain's reason text. */
    public void showError(String reason) {
        System.out.println(">> [Action Rejected]: " + reason);
    }

    /** Arrivals, "C3 ordered Pasta", payments, departures. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** "79.2", or "N/A" if nobody has paid yet. */
    private String averageText(GameStats stats) {
        OptionalDouble avg = stats.getAverageSatisfaction();
        if (avg.isPresent()) {
            return String.format("%.1f", avg.getAsDouble());
        }
        return "N/A";
    }

    private String passOrFail(boolean passed) {
        if (passed) {
            return "PASS";
        }
        return "FAIL";
    }

    /**
     * Re-prompts on junk/blank/out of range.
     * Throws NoSuchElementException if the input runs out (Main catches it).
     */
    private int readInt(int min, int max) {
        while (true) {
            try {
                if (!scanner.hasNextLine()) {
                    throw new NoSuchElementException("No input remaining in input stream");
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, Please enter a valid number only");
            }
        }
    }
}
