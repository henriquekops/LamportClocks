/**
 * Configuration object
 */
public class Configuration {

    private final int id;
    private final String host;
    private final int port;
    private final float chance;

    public Configuration(int id, String host, int port, float chance) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.chance = chance;
    }

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public float getChance() {
        return chance;
    }
}
