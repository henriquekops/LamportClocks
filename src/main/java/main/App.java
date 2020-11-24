package main;

import org.jgroups.util.Tuple;

import java.util.List;

public class App {

    public static void main(String[] args) {
        // TODO: this class will die when file handler is complete

        if (args.length != 2) {
            System.out.println("Usage: LamportCLocks.jar <isMaster: boolean> <myId: int>");
            System.exit(-1);
        }
        boolean isMaster = Boolean.parseBoolean(args[0]);
        int myId = Integer.parseInt(args[1]);
        FileHandler handler = new FileHandler("./conf.txt");
        Tuple<Configuration, List<String>> configTuple = handler.read(myId);

        Node n = new Node(isMaster, configTuple.getVal1());
        n.run(configTuple.getVal2());
    }

}
