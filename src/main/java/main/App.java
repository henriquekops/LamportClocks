package main;

public class App {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: LamportCLocks.jar <isMaster: boolean>");
        }
        boolean isMaster = Boolean.parseBoolean(args[0]);
        Node n = new Node(isMaster);
        n.run();
    }

}
