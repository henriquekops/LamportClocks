package app.sockets;

import app.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class CustomSocket {

    private final int TIMEOUT;
    private final byte[] buffer;
    private final DatagramSocket socket;

    public CustomSocket(int port) throws IOException {
        int BUFFER_SIZE = 1024;
        this.TIMEOUT = 5000;
        this.buffer = new byte[BUFFER_SIZE];
        this.socket = new DatagramSocket(port);
    }

    public void send(String message, Node target) throws IOException {
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, target.getHost(), target.getPort());
        socket.send(packet);
    }

    public String receive() throws IOException {
        socket.setSoTimeout(TIMEOUT);
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return new String(packet.getData());
        } catch (SocketTimeoutException e) {
            return null;
        }
    }
}
