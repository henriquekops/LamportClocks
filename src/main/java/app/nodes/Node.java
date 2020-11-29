package app.nodes;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Configuration object
 */
public class Node {

    private final int id;
    private final InetAddress host;
    private final int sendPort;
    private final int listenPort;
    private final double chance;
    private final boolean isMaster;
    private final LamportClock clock;

    public Node(int id, String host, int port, double chance, boolean isMaster) throws UnknownHostException {
        this.id = id;
        this.host = InetAddress.getByName(host);
        this.sendPort = 8000 + id;
        this.listenPort = port;
        this.chance = chance;
        this.isMaster = isMaster;
        this.clock = new LamportClock();
    }

    public int getId() {
        return this.id;
    }

    public InetAddress getHost() {
        return this.host;
    }

    public int getSendPort () {
        return this.sendPort;
    }

    public int getListenPort() {
        return this.listenPort;
    }

    public double getChance() {
        return this.chance;
    }

    public LamportClock getClock() {
        return this.clock;
    }

    public boolean isMaster() {
        return this.isMaster;
    }
}
