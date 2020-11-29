package app.files;

import app.events.Event;
import app.nodes.Node;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Configuration file reader object
 */
public class FileHandler {

    private final File configFile;
    private final File outputFile;
    private final int myId;

    public FileHandler(String configFilePath, int myId) throws IOException {
        this.configFile = new File(configFilePath);
        this.outputFile = new File("./output_" + myId + ".txt");
        this.myId = myId;
        if(!outputFile.createNewFile()) { clean(); }
    }

    public List<Node> read() {
        LinkedList<Node> nodes = new LinkedList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String[] rawConfig;
            String line;

            while ((line = br.readLine()) != null) {
                rawConfig = line.split(" ");

                int id = Integer.parseInt(rawConfig[0]);
                String host = rawConfig[1];
                int port = Integer.parseInt(rawConfig[2]);
                double chance = Double.parseDouble(rawConfig[3]);
                boolean isMaster = Boolean.parseBoolean(rawConfig[4]);

                Node n = new Node(id, host, port, chance, isMaster);

                if (myId == id) {
                    nodes.add(0, n);
                } else {
                    nodes.add(n);
                }
            }
            br.close();
        }
        catch (Exception e) {
            System.out.println("Input file's format is wrong! Check out docs for help ...");
            System.exit(-1);
        }
        return nodes;
    }

    public void write(Event event) {
        try {
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

    public void clean() {
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
