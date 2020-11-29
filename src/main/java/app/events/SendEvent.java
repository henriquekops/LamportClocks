package app.events;

import java.util.Date;

public class SendEvent extends Event {
    /*
        SEND (DISTRIBUTED): m i c s d
        m = time of computer in millis
        i = current node's ID
        c = current clock's value (sent)
        s = identifies 'send' action
        d = target node's ID
    */

    String formatString = "m=%d | i=%d | c=%d | s | d=%d";
    private final long m;
    private final int i;
    private final int c;
    private final int d;

    public SendEvent(int currentId, int currentClock, int targetId) {
        this.m = new Date().getTime();
        this.i = currentId;
        this.c = currentClock;
        this.d = targetId;
    }

    @Override
    public String toString() {
        return String.format(formatString, m, i, c, d);
    }
}