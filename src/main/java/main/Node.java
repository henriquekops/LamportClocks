package main;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

/**
 * Node object
 */
public class Node {

    JChannel channel;
    JChannel listenChannel;

    private Configuration config;
    private LamportClock clock;

    private final boolean isMaster;
    private volatile boolean start;
    private volatile boolean stopListen;

    public Node(boolean isMaster) {
        this.isMaster = isMaster;
        this.start = isMaster;
        this.stopListen = false;
    }

    public void run() {
        /*
        1. Logic for JGroups
        2. While not reached 100 events
        3. Create local/distributed event (Random using `Configuration`)
        4. Socket send/receive with timeout (10s)
        5. Increment `LamportClock`
         */

        try {
            channel = new JChannel();
            channel.connect("LamportCLockCLuster");

            if (isMaster) {
                Message msg = new Message(null, "start");
                channel.send(msg);
            }

            this.listen();

            while (!start) {}

            for(int eventCounter = 0; eventCounter < 100; eventCounter++) {
                System.out.println("event " + eventCounter);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
            System.exit(-1);
        } finally {
            stopListen = true;
            channel.close();
        }
    }

    private void listen() {
        new Thread(() -> {
            try {
                listenChannel = new JChannel();
                listenChannel.connect("LamportCLockCLuster");
                listenChannel.setReceiver(new ReceiverAdapter() {
                    public void receive(Message msg) {
                        if (msg.getObject().equals("start")) {
                            start = true;
                        }
                    }
                });
                while (!stopListen) {}
            } catch (Exception e) {
                System.exit(-1);
            } finally {
                channel.close();
            }
        }).start();
    }
}
