package app.files;

import app.events.Event;
import app.nodes.Node;

import java.io.*;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

/**
 * Configuration file reader object
 *
 * Expected configuration file format:
 *
 * <node id> <node host> <node port> <chance>
 * <node id> <node host> <node port> <chance>
 * <node id> <node host> <node port> <chance> ...
 */
public class FileHandler {

    private final File outputFile;

    public FileHandler() {
        this.outputFile = new File("./output.txt");
    }

    /**
     * Reads configuration at configFilePath considering currentId as current node
     *
     * @param configFilePath Config file's absolute path
     * @param currentId      Target node to be considered as current
     * @return A list of nodes having its first element as the current node
     */
    public List<Node> read(String configFilePath, int currentId) {
        try {
            LinkedList<Node> nodes = new LinkedList<>();
            File configFile = new File(configFilePath);
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;

            while ((line = br.readLine()) != null) {
                Node n = createNodeByConfig(line);
                if (currentId == n.getId()) {
                    nodes.add(0, n);
                } else {
                    nodes.add(n);
                }
            }
            br.close();
            return nodes;
        } catch (IOException e) {
            System.out.println("Input file's format is wrong! Check out docs for help ...");
            System.exit(-1);
        }
        return new LinkedList<>();
    }

    /**
     * Writes generated events to output file
     *
     * @param event Event to be written
     */
    public void write(Event event) {
        try {
            if (!outputFile.createNewFile()) {
                clean();
            }
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), true);
            PrintWriter printer = new PrintWriter(writer, false);
            printer.println(event.toString());
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR when writing to output: " + e);
            System.exit(-1);
        }
    }

    /**
     * Creates a Node object through configuration string
     *
     * @param configString String formatted as expected node configuration
     * @return New Node object
     */
    private Node createNodeByConfig(String configString) {
        try {
            String[] rawConfig = configString.split(" ");
            int id = Integer.parseInt(rawConfig[0]);
            String host = rawConfig[1];
            int port = Integer.parseInt(rawConfig[2]);
            double chance = Double.parseDouble(rawConfig[3]);
            return new Node(id, host, port, chance);
        } catch (UnknownHostException e) {
            System.out.println("ERROR while creating nodes: " + e);
            System.exit(-1);
        }
        return null;
    }

    /**
     * Clears a file
     */
    private void clean() {
        try {
            FileWriter writer = new FileWriter(outputFile.getAbsoluteFile(), false);
            PrintWriter printer = new PrintWriter(writer, false);
            printer.flush();
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR when cleaning output file: " + e);
            System.exit(-1);
        }
    }
}
