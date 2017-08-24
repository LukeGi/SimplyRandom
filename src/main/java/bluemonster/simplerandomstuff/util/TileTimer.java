package bluemonster.simplerandomstuff.util;

public class TileTimer {
    public int target;
    public int current;

    public TileTimer(int interval, int current) {
        this.target = interval;
        this.current = current;
    }

    public TileTimer(int interval) {
        this(interval, 0);
    }

    /**
     * Ticks the timer.
     *
     * @return returns true if interval is hit.
     */
    public boolean tick() {
        current++;
        if (current == target) {
            current = 0;
            return true;
        }
        return false;
    }

    /**
     * Sets the interval of the timer.
     *
     * @param interval the new interval.
     */
    public void setInterval(int interval) {
        this.target = interval;
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        this.current = 0;
    }
}
