package app.nodes;

/**
 * Lamport's clock representation
 */
public class LamportClock {

    private int counter;

    public LamportClock() {
        this.counter = 0;
    }

    /**
     * Safely gets this clock's counter
     *
     * @return Integer representing this clock's value
     */
    public synchronized int getCounter() {
        return this.counter;
    }

    /**
     * Safely increases this clock's counter based on increment and increment's source
     *
     * @param increment   Increment to be added to this clock's counter
     * @param distributed Boolean indicating the increment's source (local or received)
     */
    public synchronized void increaseCounter(int increment, boolean distributed) {
        if (distributed) {
            this.counter = Math.max(this.counter, increment) + 1;
        } else {
            this.counter += increment;
        }
    }
}
