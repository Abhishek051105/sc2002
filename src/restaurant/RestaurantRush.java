package restaurant;

import java.util.Scanner;

/**
 * Main boundary class providing a playable text CLI interface for Restaurant Rush (Stage 1).
 * Supports interactive player choices, and reproducible automated runs for victory and defeat.
 */
public class RestaurantRush {

    public static void main(String[] args) {
        if (args.length > 0) {
            if ("--victory".equalsIgnoreCase(args[0])) {
                runReproducibleVictory();
                return;
            } else if ("--defeat".equalsIgnoreCase(args[0])) {
                runReproducibleDefeat();
                return;
            }
        }

        runInteractiveGame();
    }

    public static void runInteractiveGame() {
        Scanner scanner = new Scanner(System.in);
        GameController game = new GameController();

        System.out.println("==================================================");
        System.out.println("   WELCOME TO RESTAURANT RUSH - STAGE 1 (MAIN)");
        System.out.println("==================================================");

        while (!game.isGameOver()) {
            game.startTurn();
            displayTurnHeader(game);

            // Waiter Action Phase
            handleWaiterPhase(game, scanner);

            // Chef Action Phase
            handleChefPhase(game, scanner);

            // End of Turn Updates
            game.endTurn();

            // End-of-turn Summary (Section 3.5)
            game.getStats().displaySummary();

            if (game.isDefeat()) {
                System.out.println("\n[GAME OVER - DEFEAT] Too many unhappy customers departed!");
                break;
            }
        }

        if (game.isVictory()) {
            System.out.println("\n[CONGRATULATIONS - VICTORY!] Restaurant Rush Stage 1 completed successfully!");
        } else if (!game.isDefeat()) {
            System.out.println("\n[GAME OVER] Shifts ended. Target paid customer count not met.");
        }
    }

    private static void displayTurnHeader(GameController game) {
        System.out.println("\n=================== TURN " + game.getCurrentTurn() + " / 18 ===================");
        System.out.println("Waiting Queue: " + game.getWaitingQueue());
        System.out.println("Tables: " + game.getTables());
        System.out.println("Active Orders: " + game.getActiveOrders());
    }

    private static void handleWaiterPhase(GameController game, Scanner scanner) {
        boolean actionDone = false;
        while (!actionDone) {
            System.out.println("\n--- Waiter Action ---");
            System.out.println("1. Seat a customer");
            System.out.println("2. Take order from customer");
            System.out.println("3. Serve a ready order");
            System.out.println("4. Pass");
            System.out.print("Choose action (1-4): ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1":
                        System.out.print("Enter Customer ID to seat: ");
                        int custId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Table No: ");
                        int tableNo = Integer.parseInt(scanner.nextLine().trim());
                        game.seatCustomer(custId, tableNo);
                        System.out.println("-> Customer " + custId + " seated at Table " + tableNo);
                        actionDone = true;
                        break;
                    case "2":
                        System.out.print("Enter Customer ID to take order: ");
                        int orderCustId = Integer.parseInt(scanner.nextLine().trim());
                        Order o = game.takeCustomerOrder(orderCustId);
                        System.out.println("-> Order #" + o.getId() + " placed for " + o.getItem().getName());
                        actionDone = true;
                        break;
                    case "3":
                        System.out.print("Enter Order ID to serve: ");
                        int serveOrderId = Integer.parseInt(scanner.nextLine().trim());
                        game.serveOrder(serveOrderId);
                        System.out.println("-> Order #" + serveOrderId + " served!");
                        actionDone = true;
                        break;
                    case "4":
                        System.out.println("-> Waiter passes this turn.");
                        actionDone = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose 1-4.");
                }
            } catch (InvalidActionException e) {
                // Rule: Explain why it failed and let the manager choose again without losing the task
                System.out.println("[INVALID ACTION] " + e.getMessage() + ". Please choose again.");
            } catch (NumberFormatException e) {
                System.out.println("[INPUT ERROR] Please enter a valid number.");
            }
        }
    }

    private static void handleChefPhase(GameController game, Scanner scanner) {
        boolean actionDone = false;
        while (!actionDone) {
            System.out.println("\n--- Chef Action ---");
            System.out.println("1. Cook order unit");
            System.out.println("2. Pass");
            System.out.print("Choose action (1-2): ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1":
                        System.out.print("Enter Order ID to cook: ");
                        int cookOrderId = Integer.parseInt(scanner.nextLine().trim());
                        game.cookOrder(cookOrderId);
                        System.out.println("-> Chef cooked 1 unit of Order #" + cookOrderId);
                        actionDone = true;
                        break;
                    case "2":
                        System.out.println("-> Chef passes this turn.");
                        actionDone = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose 1-2.");
                }
            } catch (InvalidActionException e) {
                System.out.println("[INVALID ACTION] " + e.getMessage() + ". Please choose again.");
            } catch (NumberFormatException e) {
                System.out.println("[INPUT ERROR] Please enter a valid number.");
            }
        }
    }

    /**
     * Executes a reproducible Victory run required for Stage 1 submission notes.
     */
    public static void runReproducibleVictory() {
        System.out.println("=== EXECUTING REPRODUCIBLE VICTORY RUN ===");
        GameController game = new GameController();
        Waiter.resetOrderIdCounter();

        try {
            // Turn 1: Cust 1 arrives. Seat at T1.
            game.startTurn();
            game.seatCustomer(1, 1);
            game.endTurn();

            // Turn 2: Take Order 1 (Pasta, 2 units). Chef cooks unit 1.
            game.startTurn();
            game.takeCustomerOrder(1);
            game.cookOrder(1);
            game.endTurn();

            // Turn 3: Cust 2 arrives. Seat at T2. Chef cooks unit 2 of Order 1 (READY).
            game.startTurn();
            game.seatCustomer(2, 2);
            game.cookOrder(1);
            game.endTurn();

            // Turn 4: Waiter serves Order 1. Take Order 2 (Salad, 1 unit).
            game.startTurn();
            game.serveOrder(1);
            // Chef passes or cooks
            game.endTurn();

            // Turn 5: Cust 1 eating (becomes READY_TO_PAY at end of 5). Waiter takes Order 2. Chef cooks Order 2 (READY).
            game.startTurn();
            game.takeCustomerOrder(2);
            game.cookOrder(2);
            game.endTurn();

            // Turn 6: Cust 3 arrives. Cust 1 pays ($18.00). Waiter serves Order 2.
            game.startTurn();
            game.serveOrder(2);
            game.endTurn();

            // Turn 7: Waiter seats Cust 3 at T1. Cust 2 finishes eating.
            game.startTurn();
            game.seatCustomer(3, 1);
            game.endTurn();

            // Turn 8: Cust 2 pays ($9.00). Waiter takes Order 3 (Burger, 2 units). Chef cooks unit 1.
            game.startTurn();
            game.takeCustomerOrder(3);
            game.cookOrder(3);
            game.endTurn();

            // Turn 9: Cust 4 arrives. Seat Cust 4 at T2. Chef cooks unit 2 of Order 3 (READY).
            game.startTurn();
            game.seatCustomer(4, 2);
            game.cookOrder(3);
            game.endTurn();

            // Turn 10: Waiter serves Order 3.
            game.startTurn();
            game.serveOrder(3);
            game.endTurn();

            // Turn 11: Waiter takes Order 4 (Pasta, 2 units). Chef cooks unit 1.
            game.startTurn();
            game.takeCustomerOrder(4);
            game.cookOrder(4);
            game.endTurn();

            // Turn 12: Cust 5 arrives. Seat Cust 5 at T3. Chef cooks unit 2 of Order 4 (READY). Cust 3 pays ($15.00).
            game.startTurn();
            game.seatCustomer(5, 3);
            game.cookOrder(4);
            game.endTurn();

            // Turn 13: Waiter serves Order 4.
            game.startTurn();
            game.serveOrder(4);
            game.endTurn();

            // Turn 14: Waiter takes Order 5 (Salad, 1 unit). Chef cooks Order 5 (READY).
            game.startTurn();
            game.takeCustomerOrder(5);
            game.cookOrder(5);
            game.endTurn();

            // Turn 15: Waiter serves Order 5. Cust 4 finishes eating.
            game.startTurn();
            game.serveOrder(5);
            game.endTurn();

            // Turn 16: Cust 6 arrives. Cust 4 pays ($18.00).
            game.startTurn();
            game.endTurn();

            // Turn 17: Cust 5 finishes eating -> READY_TO_PAY.
            game.startTurn();
            game.endTurn();

            // Turn 18: Cust 5 pays ($9.00). Cust 6 cannot finish paying within 18 turns.
            game.startTurn();
            game.endTurn();

        } catch (InvalidActionException e) {
            System.err.println("Unexpected failure in victory run: " + e.getMessage());
            return;
        }

        System.out.println("Victory Run Completed!");
        game.getStats().displaySummary();
        if (game.isVictory()) {
            System.out.println("RESULT: [VICTORY] (5 customers paid, 0 unhappy departures, max possible paid achieved!)");
        } else {
            System.out.println("RESULT: FAILED TO ACHIEVE VICTORY");
        }
    }

    /**
     * Executes a reproducible Defeat run required for Stage 1 submission notes.
     */
    public static void runReproducibleDefeat() {
        System.out.println("=== EXECUTING REPRODUCIBLE DEFEAT RUN ===");
        GameController game = new GameController();
        Waiter.resetOrderIdCounter();

        // Abandon all customers by passing every turn until satisfaction drops to 0
        for (int t = 1; t <= 15; t++) {
            game.startTurn();
            // Manager does nothing; customers remain waiting in queue
            game.endTurn();
            if (game.isDefeat()) {
                System.out.println("Defeat triggered at Turn " + game.getCurrentTurn());
                break;
            }
        }

        System.out.println("Defeat Run Completed!");
        game.getStats().displaySummary();
        if (game.isDefeat()) {
            System.out.println("RESULT: [DEFEAT] (" + game.getStats().getUnhappyDepartures() + " unhappy departures recorded!)");
        } else {
            System.out.println("RESULT: FAILED TO TRIGGER DEFEAT");
        }
    }
}
