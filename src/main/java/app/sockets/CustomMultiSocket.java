package app.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Custom multicast socket representation
 */
public class CustomMultiSocket {

    private final String START_MSG, CONNECT_MSG;
    private final int BUFFER_SIZE, CLUSTER_PORT;
    private final InetAddress cluster;
    private final MulticastSocket multiSocket;
    private boolean start;

    public CustomMultiSocket() throws IOException {
        this.START_MSG = "start";
        this.CONNECT_MSG = "connect";
        this.BUFFER_SIZE = 1024;
        this.CLUSTER_PORT = 5000;
        this.cluster = InetAddress.getByName("224.0.0.1");
        this.multiSocket = new MulticastSocket(CLUSTER_PORT);
        this.start = false;
    }

    /**
     * Connects this socket to multicast group
     *
     * @throws IOException In case this socket cannot connect to the cluster
     */
    public void connect() throws IOException {
        System.out.println("connecting to cluster...");
        multiSocket.joinGroup(cluster);
    }

    /**
     * Sends a heartbeat to the cluster through this socket
     *
     * @throws IOException In case this socket cannot send a heartbeat message to the cluster
     */
    public void sendHeartBeat() throws IOException {
        byte[] heartBeat = CONNECT_MSG.getBytes();
        System.out.println("Sending heartbeat to master ...");
        multiSocket.send(new DatagramPacket(heartBeat, heartBeat.length, cluster, CLUSTER_PORT));
    }

    /**
     * Waits for all connections if this socket was created at master node
     *
     * @param numNodes Number of nodes to wait for
     * @throws IOException In case if an error occurs when receiving heartbeat messages
     */
    public void waitForConnections(int numNodes) throws IOException {
        int numConnections = 0;

        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        System.out.println("waiting for connections...");
        while (numConnections < numNodes) {
            multiSocket.receive(packet);
            numConnections++;
            System.out.println(numConnections + "/" + numNodes + " nodes connected...");
        }
        System.out.println("starting nodes...");
        byte[] startMessage = START_MSG.getBytes();
        multiSocket.send(new DatagramPacket(startMessage, startMessage.length, cluster, CLUSTER_PORT));
    }

    /**
     * Waits for the master node to start all connection if this socket was created at a default cluster node
     *
     * @throws IOException In case if an error occurs when receiving master's start message
     */
    public void waitForStart() throws IOException {
        String received;
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        System.out.println("waiting for start...");
        while (!start) {
            multiSocket.receive(packet);
            received = new String(packet.getData(), 0, packet.getLength());

            if (received.equals(START_MSG)) {
                start = true;
            }
        }
        System.out.println("master started!");
        multiSocket.close();
    }
}