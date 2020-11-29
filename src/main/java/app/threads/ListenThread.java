package app.threads;

import app.events.ReceiveEvent;
import app.files.FileHandler;
import app.nodes.Node;
import app.sockets.CustomSocket;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Listen thread representation
 */
public class ListenThread extends Thread {

    private final Node currentNode;
    private final CustomSocket socket;
    private final FileHandler fileHandler;
    private final AtomicBoolean running;

    public ListenThread(Node currentNode) throws IOException {
        this.currentNode = currentNode;
        this.socket = new CustomSocket(currentNode.getListenPort());
        this.fileHandler = new FileHandler();
        this.running = new AtomicBoolean(true);
    }

    /**
     * Kills this thread
     */
    public void kill() {
        this.running.set(false);
    }

    /**
     * Overwritten Thread method to receive messages from other nodes
     */
    @Override
    public void run() {
        try {
            while (running.get()) {
                String convertedMsg = socket.receive();

                if (convertedMsg == null) {
                    continue;
                }

                String[] splitMsg = convertedMsg.split(" ");

                int senderCLock = Integer.parseInt(splitMsg[0].trim());
                int senderId = Integer.parseInt(splitMsg[1].trim());

                currentNode.getClock().increaseCounter(senderCLock, true);

                fileHandler.write(new ReceiveEvent(
                        currentNode.getId(),
                        currentNode.getClock().getCounter(),
                        senderId,
                        senderCLock
                ));
                System.out.println("receive event | " +
                        "updated clock = " + currentNode.getClock().getCounter() + " | " +
                        "origin = " + senderId + " | " +
                        "received clock = " + senderCLock
                );
            }
        } catch (Exception e) {
            System.out.println("ERROR at listen thread: " + e);
            System.exit(-1);
        }
    }
}
