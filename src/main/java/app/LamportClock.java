package app;

import java.util.concurrent.Semaphore;

/**
 * Lamport's clock object
 */
public class LamportClock {

    private int counter;
    private final Semaphore lock;

    public LamportClock() {
        this.lock = new Semaphore(1);
        this.counter = 0;
    }

    public int getCounter() throws InterruptedException {
        int auxCounter;
        this.lock.acquire();
        auxCounter = this.counter;
        this.lock.release();
        return auxCounter;
    }

    public void increaseCounter(int increment, boolean distributed) throws InterruptedException {
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
    }
}
