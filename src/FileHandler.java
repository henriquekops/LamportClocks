import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public void write(){
        /*TODO:
           1. VERIFICAR COMO SERÁ TRATADO A SAIDA
           2. ACREDITO QUE CRIAR OBJETOS PARA CADA TIPO DE SAIDA SEJA MAIS INTERESSANTE (como: Evento local, Envio de mensagem, Recebimento de mensagem)
        * */
    }

}
