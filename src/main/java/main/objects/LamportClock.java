package main.objects;

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
        return this.counter;
    }

    public void increaseCounter(int increment, boolean distributed) {
        try {
            if (distributed) {
                this.lock.acquire();
                this.counter = Math.max(this.counter, increment)+1;
                this.lock.release();
            }
            else {
                this.lock.acquire();
                this.counter += increment;
                this.lock.release();
            }
        } catch (InterruptedException e) {
            System.out.println("Error while increasing clock: " + e);
        }
    }
}
