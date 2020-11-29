package app.threads;

import app.FileHandler;
import app.Node;
import app.events.ReceiveEvent;
import app.sockets.CustomSocket;

import java.util.concurrent.atomic.AtomicBoolean;

public class ListenThread extends Thread {

    private final Node currentNode;
    private final CustomSocket socket;
    private final FileHandler fileHandler;
    private final AtomicBoolean running;

    public ListenThread(CustomSocket socket, Node currentNode, FileHandler fileHandler) {
        this.currentNode = currentNode;
        this.socket = socket;
        this.fileHandler = fileHandler;
        this.running = new AtomicBoolean(true);
    }

    public void kill() {
        this.running.set(false);
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                String convertedMsg = socket.receive();

                if (convertedMsg != null) {
                    String[] splitMsg = convertedMsg.split(" ");

                    int senderCLock = Integer.parseInt(splitMsg[0].trim());
                    int senderId = Integer.parseInt(splitMsg[1].trim());

                    currentNode.getClock().increaseCounter(senderCLock, true);

                    System.out.println("receive event | " +
                            "updated clock = " + currentNode.getClock().getCounter() + " | " +
                            "origin = " + senderId + " | " +
                            "received clock = " + senderCLock
                    );

                    fileHandler.write(new ReceiveEvent(
                            currentNode.getId(),
                            currentNode.getClock().getCounter(),
                            senderId,
                            senderCLock
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR at listen thread: " + e);
            System.exit(-1);
        }
    }
}
