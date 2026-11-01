package control;
import boundary.ConsoleUI;
import entity.CustomerType;
import entity.Restaurant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Restaurant restaurant =  new Restaurant(GameConfig.NUM_TABLES);
        ConsoleUI console = new ConsoleUI(new Scanner(System.in), GameConfig.MAX_TURNS);
     
        List<CustomerType> types = GameConfig.ARRIVAL_TYPES;
        if (args.length > 0 && args[0].equals("regular")) {
            types = GameConfig.ALL_REGULAR;
        }
        ArrivalSchedule arrival = new ArrivalSchedule(GameConfig.ARRIVAL_TURNS, types);
        Objective objective = new Objective(GameConfig.TARGET_PAID, GameConfig.TARGET_REVENUE_CENTS, GameConfig.TARGET_AVG_SATISFACTION);
        GameController game = new GameController(restaurant, console, arrival, objective);

        try {
            game.run();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Input ended before the game finished.");
        }
    }
}
