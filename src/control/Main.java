package control;
import boundary.ConsoleUI;
import entity.Restaurant;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Wiring only. */
public class Main {
    public static void main(String[] args) {
        Restaurant restaurant =  new Restaurant(GameConfig.NUM_TABLES);
        ConsoleUI console = new ConsoleUI(new Scanner(System.in), GameConfig.MAX_TURNS);
        ArrivalSchedule arrival = new ArrivalSchedule(GameConfig.ARRIVAL_TURNS);
        Objective objective = new Objective(GameConfig.TARGET_PAID, GameConfig.TARGET_REVENUE_CENTS, GameConfig.TARGET_AVG_SATISFACTION);
        GameController game = new GameController(restaurant, console, arrival, objective);

        try {
            game.run();
        } catch (NoSuchElementException e) {
            // happens when a piped test script runs out before turn 18
            System.out.println();
            System.out.println("Input ended before the game finished.");
        }
    }
}
