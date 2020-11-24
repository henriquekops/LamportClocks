package main;

import org.jgroups.util.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration file reader object
 */
public class FileHandler {

    private final File file;

    public FileHandler(String file) {
        this.file = new File(file);
    }

    public Tuple<Configuration, List<String>> read(int myId){
        try {
            Configuration myConfig = null;
            ArrayList<String> availableHosts = new ArrayList<>();
            String line;

            BufferedReader br = new BufferedReader(new FileReader(this.file));

            while ((line = br.readLine()) != null) {
                String[] rawConfig  = line.split(" ");

                int id = Integer.parseInt(rawConfig[0]);
                String host = rawConfig[1];
                int port = Integer.parseInt(rawConfig[2]);
                double chance = Double.parseDouble(rawConfig[3]);

                if (myId == id) {
                    myConfig = new Configuration(id, host, port, chance);
                }
                else {
                    availableHosts.add(host);
                }
            }
            br.close();

            return new Tuple<>(myConfig, availableHosts);
        }
        catch (java.io.IOException e) {
            System.out.println("FILE READING ERROR: " + e);
        }
        return null;
    }

    public void write(){
        /*TODO:
           1. VERIFICAR COMO SERÁ TRATADO A SAIDA
           2. ACREDITO QUE CRIAR OBJETOS PARA CADA TIPO DE SAIDA SEJA MAIS INTERESSANTE (como: Evento local, Envio de mensagem, Recebimento de mensagem)
        * */
    }

}
