package control;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constants defining the base configuration for Stage 1.
 */
public class GameConfig {
    public static final int TOTAL_TURNS = 18;
    public static final int NUM_TABLES = 2;
    public static final List<Integer> ARRIVAL_TURNS = Collections.unmodifiableList(
            Arrays.asList(1, 4, 7, 10, 13, 16)
    );

    private GameConfig() {
        // Prevent instantiation of constants class
    }
}
