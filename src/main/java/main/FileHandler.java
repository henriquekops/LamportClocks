package main;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Configuration file reader object
 */
public class FileHandler {

    private final File configFile;
    private final File outputFile;

    public FileHandler(String configFilePath, String outputFilePath) {
        this.configFile = new File(configFilePath);
        this.outputFile = new File(outputFilePath);
    }

    public List<Node> read(int myId){
        LinkedList<Node> nodes = new LinkedList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            while ((line = br.readLine()) != null) {

                if (!line.equals("")) {
                    String[] rawConfig = line.split(" ");

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
            }
            br.close();
        }
        catch (Exception e) {
            System.out.println("Input file's format is wrong! Check out docs for help ...");
            e.printStackTrace();
            System.exit(-1);
        }
        return nodes;
    }

    public void write(EventType eventType, long m, int i, int c, Optional<String> dORs, Optional<Integer> t) throws IOException {

        /*
        LOCAL : m i c l,
        m = tempo do computador local em milissegundos,
        i = ID do nodo local
        c = valor do relógio lógico local, concatenado com o IDdo nodo
        l = local
         */

        /*
        ENVIO (DISTRIBUIDO): m i c s d
        m = tempo do computador local em milissegundos
        i = ID do nodo local
        c = valor do relógio lógico enviado(relógio concatenado com o ID)
        d = ID do nodo destinatário da mensagem
         */

        /*
        RECEBIMENTO (DISTRIBUÍDO): m i c r s t
        m = tempo do computador local em milissegundos
        i = ID do nodo local
        c = valor do relógio lógico depois do recebimento da mensagem
        s = ID do nodo remetente da mensagem
        t = valor do relógio lógico recebido com a mensagem
         */

        // TODO: Alternativa melhor?

        FileOutputStream fos = new FileOutputStream(outputFile);
        String line = "";

        switch(eventType) {
            case LOCAL_EVENT:
                line = String.format("m=%d i=%d c=%d l", m, i, c);
                break;
            case SEND_EVENT:
                String d = dORs.orElse("-");
                line = String.format("m=%d i=%d c=%d d=%s", m, i, c, d);
                break;
            case RECEIVE_EVENT:
                String s = dORs.orElse("-");
                String ts = dORs.orElse("-1");
                line = String.format("m=%d i=%d c=%d s=%s t=%d", m, i, c, s, ts);
                break;
            default: break;
        }
        fos.write(line.getBytes(), 0, line.length());
        fos.close();
    }

}
