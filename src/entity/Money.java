package entity;

/**
 * Utility class for monetary calculations and formatting.
 * Internal monetary values are strictly stored as integer cents.
 */
public class Money {
    private Money() {
        // Prevent instantiation of utility class
    }

    /**
     * Formats integer cents into standard dollar display with 2 decimal places.
     * Example: 1500 -> "$15.00", 2430 -> "$24.30"
     *
     * @param cents amount in cents
     * @return formatted dollar string
     */
    public static String format(int cents) {
        return String.format("$%.2f", cents / 100.0);
    }
}
