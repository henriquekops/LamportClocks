package app.nodes;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Node representation
 */
public class Node {

    private final int id;
    private final InetAddress host;
    private final int sendPort;
    private final int listenPort;
    private final double chance;
    private final LamportClock clock;
    private final boolean isMaster;

    public Node(int id, String host, int port, double chance, boolean isMaster) throws UnknownHostException {
        this.id = id;
        this.host = InetAddress.getByName(host);
        this.sendPort = 8000 + id;
        this.listenPort = port;
        this.chance = chance;
        this.clock = new LamportClock();
        this.isMaster = isMaster;
    }

    /**
     * Retrieves this node's ID
     *
     * @return Integer representing this node's ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves this node's host
     *
     * @return InetAddress representing this node's host
     */
    public InetAddress getHost() {
        return this.host;
    }

    /**
     * Retrieves this node's send port, responsible for sending events
     *
     * @return Integer representing this node's send port
     */
    public int getSendPort() {
        return this.sendPort;
    }

    /**
     * Retrieves this node's listen port, responsible for listening for events
     *
     * @return Integer representing this node's listen port
     */
    public int getListenPort() {
        return this.listenPort;
    }

    /**
     * Retrieves this node's chance to generate a distributed event
     *
     * @return Double representing a chance of this node's to generate a distributed event
     */
    public double getChance() {
        return this.chance;
    }

    /**
     * Retrieves this node's Lamport's clock
     *
     * @return This node's Lamport's clock
     */
    public LamportClock getClock() {
        return this.clock;
    }

    /**
     * Retrieves if the this node is the master node
     *
     * @return Boolean representing if this node is the master
     */
    public boolean isMaster() {
        return this.isMaster;
    }
}
