package main;

import java.util.Random;
import java.util.Scanner;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;


/**
 * Node object
 */
public class Node {

    JChannel channel;

    private Configuration config;
    private volatile LamportClock clock;

    private final boolean isMaster;
    private volatile boolean start;
    private volatile boolean stopListen;

    private final int threshold;

    public Node(boolean isMaster, int threshold) {
        // TODO: use config when file handler is complete
        this.isMaster = isMaster;
        this.start = isMaster;
        this.stopListen = false;
        this.threshold = threshold;
        this.clock = new LamportClock();
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
                String command = "command";
                while (!command.equals("")) {
                    Scanner input = new Scanner(System.in);
                    System.out.println("[MASTER] Press enter to start");
                    command = input.nextLine();
                }
                System.out.println("[MASTER]: Starting all nodes ...");
                Message msg = new Message(null, "start");
                channel.send(msg);
            }

            this.listen();

            while (!start) {}

            Random rand = new Random();
            for(int eventCounter = 0; eventCounter < 2; eventCounter++) {
                // TODO: move receiver to here (?)
                // TODO: i think that there is a problem synchronizing clocks

                int eventTypeSelector = rand.nextInt(99)+1;

                if (eventTypeSelector <= this.threshold) {
                    System.out.println("event=" + eventCounter + " type=distributed");
                    Message msg = new Message(null, this.clock.getCounter());
                    this.channel.send(msg);
                } else {
                    System.out.println("event=" + eventCounter + " type=local");
                    this.clock.increaseCounter(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
            System.exit(-1);
        } finally {
            System.out.println("main thread: stop!");
            stopListen = true;
            channel.disconnect();
            channel.close();
        }
    }

    private void listen() {
        // TODO: this thread may not be necessary
        new Thread(() -> {
            try {
                channel.setReceiver(new ReceiverAdapter() {
                    public void receive(Message msg) {
                        if (msg.getObject().equals("start")) {
                            start = true;
                        } else {
                            System.out.println("Received: clock=" + msg.getObject());
                            clock.increaseCounter(msg.getObject());
                        }
                    }
                });
                while (!stopListen) {
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                System.exit(-1);
            }
        }).start();
    }
}
