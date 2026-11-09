package control;
import boundary.ConsoleUI;
import entity.ComboMeal;
import entity.CustomerType;
import entity.Menu;
import entity.Restaurant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Restaurant restaurant =  new Restaurant(GameConfig.NUM_TABLES);
        ConsoleUI console = new ConsoleUI(new Scanner(System.in), GameConfig.MAX_TURNS);
     
        // no argument = Stage 3, "stage2" = Stage 2 menu (no combos), "regular" = Stage 1 customers and menu
        String mode = "stage3";
        if (args.length > 0) {
            mode = args[0];
        }

        List<CustomerType> types = GameConfig.ARRIVAL_TYPES;
        if (mode.equals("regular")) {
            types = GameConfig.ALL_REGULAR;
        }

        // Stage 3 menu: the 2 combos go on the end of the base menu
        if (!mode.equals("regular") && !mode.equals("stage2")) {
            Menu menu = restaurant.getMenu();
            menu.addItem(new ComboMeal(menu.getItem(0), menu.getItem(2)));   // Salad + Pasta
            menu.addItem(new ComboMeal(menu.getItem(1), menu.getItem(0)));   // Burger + Salad
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
