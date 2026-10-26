package boundary;

import control.Objective;
import entity.GameStats;
import entity.Restaurant;
import entity.WaiterTask;
import java.util.Scanner;

/** Input and output only. Never decides whether an action is allowed. */
public class ConsoleUI {
    private final Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Turn, revenue, avg (or N/A), queue, tables, which staff can still act. */
    public void showStartOfTurn(int turn, Restaurant r) { throw new UnsupportedOperationException("TODO"); }
    /** Served, departures, revenue, avg, turns used. */
    public void showEndOfTurn(int turn, Restaurant r) { throw new UnsupportedOperationException("TODO"); }
    /** VICTORY/DEFEAT plus which targets passed or failed. */
    public void showFinalResult(boolean won, GameStats stats, Objective objective) { throw new UnsupportedOperationException("TODO"); }

    public WaiterTask askWaiterTask() { throw new UnsupportedOperationException("TODO"); }
    public int askCustomerId(String prompt) { throw new UnsupportedOperationException("TODO"); }
    public int askTableNo(int numTables) { throw new UnsupportedOperationException("TODO"); }
    /** 0 = Wait */
    public int askOrderIdOrWait() { throw new UnsupportedOperationException("TODO"); }

    /** Prints the domain's reason text. */
    public void showError(String reason) { throw new UnsupportedOperationException("TODO"); }
    /** Arrivals, "C3 ordered Pasta", payments, departures. */
    public void showMessage(String message) { throw new UnsupportedOperationException("TODO"); }

    /** Re-prompts on junk/blank/out of range. Exits cleanly when piped input ends. */
    private int readInt(int min, int max) { throw new UnsupportedOperationException("TODO"); }
}
