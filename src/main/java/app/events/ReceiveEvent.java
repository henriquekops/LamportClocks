package app.events;

import java.util.Date;

public class ReceiveEvent extends Event {
    /*
        RECEIVE (DISTRIBUTED): m i c r s t
        m = time of computer in millis
        i = current node's ID
        c = current clock's value (after received)
        r = identifies 'receive' action
        s = origin node's ID
        t = received clock's value
    */

    String formatString = "m=%d | i=%d | c=%d | r | s=%d | t=%d";
    private final long m;
    private final int i;
    private final int c;
    private final int s;
    private final int t;

    public ReceiveEvent(int currentId, int currentClock, int originId, int originClock) {
        this.m = new Date().getTime();
        this.i = currentId;
        this.c = Integer.parseInt(currentClock+""+currentId);
        this.s = originId;
        this.t = originClock;
    }

    @Override
    public String toString() {
        return String.format(formatString, m, i, c, s, t);
    }
}
