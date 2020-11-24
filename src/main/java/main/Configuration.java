package main;

/**
 * Configuration object
 */
public class Configuration {

    private final int id;
    private final String host;
    private final int port;
    private final double chance;

    public Configuration(int id, String host, int port, double chance) {
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

    public double getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", chance=" + chance +
                '}';
    }
}
