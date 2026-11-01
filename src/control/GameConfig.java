package control;

import java.util.List;
import entity.CustomerType;

public final class GameConfig {
    public static final int MAX_TURNS = 18;
    public static final int NUM_TABLES = 2;

    public static final List<Integer> ARRIVAL_TURNS = List.of(1, 4, 7, 10, 13, 16);

    public static final List<CustomerType> ARRIVAL_TYPES = List.of(
            CustomerType.REGULAR, CustomerType.VIP, CustomerType.REGULAR,
            CustomerType.CRITIC, CustomerType.REGULAR, CustomerType.REGULAR);

    public static final List<CustomerType> ALL_REGULAR = List.of(
            CustomerType.REGULAR, CustomerType.REGULAR, CustomerType.REGULAR,
            CustomerType.REGULAR, CustomerType.REGULAR, CustomerType.REGULAR);

    public static final int TARGET_PAID = 4;
    public static final int TARGET_REVENUE_CENTS = 4500;
    public static final int TARGET_AVG_SATISFACTION = 60;
}
