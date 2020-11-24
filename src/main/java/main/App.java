package main;

import main.objects.FileHandler;
import main.objects.Node;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class App {

    static String CLUSTER = "LamportClockCluster";

    static JChannel channel;

    static volatile Node currentNode;
    static volatile boolean start;
    static volatile boolean stopListen = false;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: LamportCLocks.jar <confPath: str> <myId: int>");
            System.exit(-1);
        }

        String confPath = args[0];
        int myId = Integer.parseInt(args[1]);

        FileHandler handler = new FileHandler(confPath);
        List<Node> nodes = handler.read(myId);

        currentNode = nodes.remove(0);

        try {
            channel = new JChannel();
            channel.connect(CLUSTER);

            if (currentNode.isMaster()) {
                Scanner input = new Scanner(System.in);
                System.out.println("[MASTER] Press any key to start");
                input.nextLine();

                System.out.println("[MASTER]: Starting all nodes ...");
                Message msg = new Message(null, 1);
                channel.send(msg);
                channel.disconnect();
                channel.close();
            }
            else {
                channel.setReceiver(new ReceiverAdapter() {
                    public void receive(Message msg) {
                        while (!start) {
                            if (msg.getObject().equals(1)) {
                                start = true;
                            }
                        }
                    }
                });
            }

            listen();
            while (!start) { }

            Random rand = new Random();
            DatagramSocket socket = new DatagramSocket();
            Node targetNode;

            for(int eventCounter = 0; eventCounter < 2; eventCounter++) {

                if (Math.round(rand.nextFloat()*10)/10.0 <= currentNode.getChance()) {
                    currentNode.getClock().increaseCounter(1, false);
                    targetNode = nodes.get(rand.nextInt(nodes.size()));

                    byte[] msg = ByteBuffer.allocate(4).putInt(currentNode.getClock().getCounter()).array();
                    socket.send(new DatagramPacket(msg, msg.length, targetNode.getHost(), targetNode.getPort()));
                }
                else {
                    currentNode.getClock().increaseCounter(1, false);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
            System.exit(-1);

        }
        finally {
            stopListen = true;
        }
    }

    public static void listen() {
        new Thread(() -> {
            try {
                int increase;
                byte[] buffer = new byte[1024];
                DatagramSocket socket = new DatagramSocket(currentNode.getPort());
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (!stopListen) {
                    socket.setSoTimeout(3000);
                    try {
                        socket.receive(packet);
                        increase = ByteBuffer.wrap(packet.getData()).getShort();
                        currentNode.getClock().increaseCounter(increase, true);
                    } catch (SocketTimeoutException e) {/* ignore*/}
                }
            }
            catch (Exception e) {
                System.exit(-1);
            }
        }).start();
    }
}
