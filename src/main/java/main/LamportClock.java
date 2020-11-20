package main;

/**
 * Lamport's clock object
 */
public class LamportClock {

    private int counter;

    public LamportClock() {
        this.counter = 0;
    }

    public int getCounter() {
        return counter;
    }

    public void increaseCounter(int counter) {
        this.counter += counter;
    }
}
