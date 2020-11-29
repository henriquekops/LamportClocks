package app.nodes;

/**
 * Lamport's clock object
 */
public class LamportClock {

    private int counter;

    public LamportClock() {
        this.counter = 0;
    }

    public synchronized int getCounter() {
        return this.counter;
    }

    public synchronized void increaseCounter(int increment, boolean distributed) {
        if (distributed) {
            this.counter = Math.max(this.counter, increment)+1;
        }
        else {
            this.counter += increment;
        }
    }
}
