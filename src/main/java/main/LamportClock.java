package main;

import java.util.concurrent.Semaphore;

/**
 * Lamport's clock object
 */
public class LamportClock {

    private int counter;
    private Semaphore lock;

    public LamportClock() {
        this.lock = new Semaphore(1);
        this.counter = 0;
    }

    public int getCounter() {
        return counter;
    }

    public void increaseCounter(int counter) {
        try {
            lock.acquire();
            this.counter += counter;
            lock.release();
        } catch (InterruptedException e) {
            System.out.println("Error while increasing clock: " + e);
        }
    }
}
