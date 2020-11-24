package main;

import java.util.List;
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

    public Node(boolean isMaster, Configuration config) {
        // TODO: use config when file handler is complete
        this.isMaster = isMaster;
        this.start = isMaster;
        this.stopListen = false;
        this.config = config;
        this.clock = new LamportClock();
    }

    public void run(List<String> availableHosts) {
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

                double eventTypeSelector = Math.round(rand.nextFloat()*10)/10.0;

                if (eventTypeSelector <= this.config.getChance()) {
                    this.clock.increaseCounter(1, false);
                    Message msg = new Message(null, this.clock.getCounter());
                    this.channel.send(msg);

                    System.out.println("event=" + eventCounter + " type=distributed");
                }

                else {
                    this.clock.increaseCounter(1, false);

                    System.out.println("event=" + eventCounter + " type=local " + "clock=" + this.clock.getCounter());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
            System.exit(-1);
        } finally {
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
                            clock.increaseCounter(msg.getObject(), true);
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
