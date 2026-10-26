package entity;

/** Money is stored as int cents everywhere; this only formats it. */
public final class Money {
    private Money() { }

    /** 1500 -> "$15.00", 2430 -> "$24.30". */
    public static String format(int cents) {
        throw new UnsupportedOperationException("TODO");
    }
}
