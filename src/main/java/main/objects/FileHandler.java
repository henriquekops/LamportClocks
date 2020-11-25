package main.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Configuration file reader object
 */
public class FileHandler {

    private final File file;

    public FileHandler(String file) {
        this.file = new File(file);
    }

    public List<Node> read(int myId){
        LinkedList<Node> nodes = new LinkedList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(this.file));

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

    public void write(){
        /*TODO:
           1. VERIFICAR COMO SERÁ TRATADO A SAIDA
           2. ACREDITO QUE CRIAR OBJETOS PARA CADA TIPO DE SAIDA SEJA MAIS INTERESSANTE (como: Evento local, Envio de mensagem, Recebimento de mensagem)
        * */
    }

}
