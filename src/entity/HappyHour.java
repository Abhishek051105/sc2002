package entity;

/**
 * Happy Hour: 20% off orders taken while it is on. Can be started once per
 * game and lasts that turn and the next. Also tracks whether it has been
 * used and how many turns are left; both change only through activate()
 * and endTurn().
 */
public class HappyHour implements PricingPolicy {
    public static final int DURATION_TURNS = 2;
    public static final int RECOVERY_POINTS = 15;

    private boolean used;
    private int turnsLeft;

    /** Starts Happy Hour for this turn and the next. */
    public void activate() throws InvalidActionException {
        if (used) {
            throw new InvalidActionException("Happy Hour has already been used this game");
        }
        used = true;
        turnsLeft = DURATION_TURNS;
    }

    public boolean isActive() {
        return turnsLeft > 0;
    }

    public boolean isAvailable() {
        return !used;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    /** Called once at the end of every turn. */
    public void endTurn() {
        if (turnsLeft > 0) {
            turnsLeft = turnsLeft - 1;
        }
    }

    @Override
    public int applyTo(int billCents) {
        return (billCents * 80 + 50) / 100;   // 20% off, rounded to the nearest cent
    }
}
