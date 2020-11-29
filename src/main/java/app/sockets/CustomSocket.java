package app.sockets;

import app.nodes.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class CustomSocket {

    private final byte[] buffer;
    private final DatagramSocket socket;

    public CustomSocket(int port) throws IOException {
        int BUFFER_SIZE = 1024;
        this.buffer = new byte[BUFFER_SIZE];
        this.socket = new DatagramSocket(port);
    }

    public void send(String message, Node target) throws IOException {
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, target.getHost(), target.getListenPort());
        socket.send(packet);
    }

    public String receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData());
    }
}
