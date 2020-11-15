import java.io.*;
import java.util.List;

/**
 * Configuration file reader object
 */
public class FileHandler {

    private final File arq;

    public FileHandler(File arq) {
        this.arq = arq;
    }

    public List<Configuration> read(){
        try {
            //String que irá receber cada linha do arquivo
            String line = "";
            int lineCounter = 0;

            //Indicamos o arquivo que será lido
            BufferedReader br = new BufferedReader(new FileReader(arq));

            while ((line = br.readLine()) != null) {
                lineCounter++;
                System.out.println(lineCounter + " Linha: " + line);
                // pegar os line.subString
                // new Configuration()
            }
            br.close();
        } catch (java.io.IOException e) {
            System.out.println("FILE READING ERROR: " + e);
        }
        return null;
    }

}
