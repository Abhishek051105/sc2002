package control;

import boundary.ConsoleUI;
import entity.Restaurant;

/**
 * Entry point for Restaurant Rush.
 * Wires together entity, control, and boundary layers and executes the game loop.
 */
public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        ArrivalSchedule schedule = new ArrivalSchedule();
        Objective objective = new Objective();
        ConsoleUI ui = new ConsoleUI();

        GameController controller = new GameController(restaurant, schedule, objective, ui);
        controller.run();
    }
}
