package main;

public class App {

    public static void main(String[] args) {
        // TODO: this class will die when file handler is complete

        if (args.length != 2) {
            System.out.println("Usage: LamportCLocks.jar <isMaster: boolean> <threshold: int>");
            System.exit(-1);
        }
        boolean isMaster = Boolean.parseBoolean(args[0]);
        int threshold = Integer.parseInt(args[1]);
        Node n = new Node(isMaster, threshold);
        n.run();
    }

}
