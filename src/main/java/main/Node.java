package main;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Configuration object
 */
public class Node {

    private final int id;
    private final InetAddress host;
    private final int port;
    private final double chance;
    private final boolean isMaster;
    private final LamportClock clock;

    public Node(int id, String host, int port, double chance, boolean isMaster) throws UnknownHostException {
        this.id = id;
        this.host = InetAddress.getByName(host);
        this.port = port;
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

    public int getPort() {
        return this.port;
    }

    public double getChance() {
        return this.chance;
    }

    public LamportClock getClock() { return this.clock; }

    public boolean isMaster() { return this.isMaster; }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + this.id +
                ", host='" + this.host + '\'' +
                ", port=" + this.port +
                ", chance=" + this.chance +
                '}';
    }
}
