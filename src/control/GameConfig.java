package control;

import java.util.Set;

/** Base-setting constants. */
public final class GameConfig {
    private GameConfig() { }

    public static final int MAX_TURNS = 18;
    public static final int NUM_TABLES = 2;
    public static final Set<Integer> ARRIVAL_TURNS = Set.of(1, 4, 7, 10, 13, 16);

    public static final int TARGET_PAID = 4;
    public static final int TARGET_REVENUE_CENTS = 4500;
    public static final int TARGET_AVG_SATISFACTION = 60;
}
