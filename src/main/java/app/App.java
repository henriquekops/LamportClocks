package app;

import app.events.LocalEvent;
import app.events.SendEvent;
import app.sockets.CustomMultiSocket;
import app.sockets.CustomSocket;
import app.threads.ListenThread;

import java.util.List;
import java.util.Random;


public class App {

    static ListenThread thread;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: LamportCLocks.jar <confFilePath: str> <myId: int>");
            System.exit(-1);
        }

        String confFilePath = args[0];
        int myId = Integer.parseInt(args[1]);

        FileHandler fileHandler = new FileHandler(confFilePath, myId);
        List<Node> nodes = fileHandler.read();
        Node currentNode = nodes.remove(0);

        try {
            CustomSocket socket = new CustomSocket(currentNode.getPort());
            CustomMultiSocket multiSocket = new CustomMultiSocket();
            thread= new ListenThread(
                socket,
                currentNode,
                fileHandler
            );

            thread.start();

            multiSocket.connect();

            if (currentNode.isMaster()) {
                multiSocket.waitForConnections(nodes.size());
                multiSocket.waitForStart();
            } else {
                multiSocket.sendHeartBeat();
                multiSocket.waitForStart();
            }

            Random rand = new Random();
            Node targetNode;
            String message;

            for (int eventCounter = 0; eventCounter < 5; eventCounter++) {

                if (Math.round(rand.nextFloat() * 10) / 10.0 <= currentNode.getChance()) {
                    currentNode.getClock().increaseCounter(1, false);
                    message = currentNode.getClock().getCounter() + " " + currentNode.getId();
                    targetNode = nodes.get(rand.nextInt(nodes.size()));
                    socket.send(message, targetNode);
                    fileHandler.write(new SendEvent(
                            currentNode.getId(),
                            currentNode.getClock().getCounter(),
                            targetNode.getId()
                    ));
                    System.out.println(
                        "send event | " +
                        "updated clock = " + currentNode.getClock().getCounter() + " | " +
                        "target = " + targetNode.getId()
                    );
                } else {
                    currentNode.getClock().increaseCounter(1, false);
                    fileHandler.write(new LocalEvent(
                        currentNode.getId(),
                        currentNode.getClock().getCounter()
                    ));
                    System.out.println(
                        "local event | " +
                        "updated clock = " + currentNode.getClock().getCounter()
                    );
                }
            }
        }
        catch (Exception e) {
            System.out.println("ERROR at main: " + e);
            System.exit(-1);
        }
        finally {
            if(thread != null) {
                thread.kill();
            }
        }
    }
}
