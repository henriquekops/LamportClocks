package app.events;

import java.util.Date;

public class LocalEvent extends Event {
    /*
        LOCAL : m i c l,
        m = time of computer in millis
        i = current node's ID
        c = current clock's value
        l = identifies 'local' action
    */

    String formatString = "m=%d | i=%d | c=%d | l";
    private final long m;
    private final int i;
    private final int c;

    public LocalEvent(int currentId, int currentClock) {
        this.m = new Date().getTime();
        this.i = currentId;
        this.c = Integer.parseInt(currentClock + "" + currentId);
    }

    @Override
    public String toString() {
        return String.format(formatString, m, i, c);
    }
}
