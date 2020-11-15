/**
 * Node object
 */
public class Node {

    private Configuration config;
    private LamportClock clock;

    public static void main(String[]args) {
        /*
        1. Logic for JGroups (MulticastSocket)
        2. While not reached 100 events
        3. Create local/distributed event (Random using `Configuration`)
        4. Socket send/receive with timeout (10s)
        5. Increment `LamportClock`
         */
    }
}
