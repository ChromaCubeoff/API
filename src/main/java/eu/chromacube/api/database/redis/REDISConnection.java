package eu.chromacube.api.database.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class REDISConnection {

    private JedisPool jedisPool;

    private String host, password;
    private int port, database;

    /**
     * @param host of the server
     * @param password of the server
     * @param port of the server
     * @param database of the server
     */
    public REDISConnection(String host, String password, int port, int database){
        this.host = host;
        this.password = password;
        this.port = port;
        this.database = database;
    }

    /**
     * @apiNote connect redis database of the server
     */
    public void connect() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(-1);
        config.setJmxEnabled(false);

        try {
            this.jedisPool = new JedisPool(config, host, port, 0, password,database);
            this.jedisPool.getResource().close();
            System.err.print("[Redis] connected");
        } catch (Exception e) {
            System.err.print("[Redis] failure");
            e.printStackTrace();
        }
    }

    /**
     * @return a connection jedis.
     */
    public Jedis get(){
        return jedisPool.getResource();
    }

    /**
     * @apiNote  kills all connections
     */
    public void kill(){
        jedisPool.close();
        jedisPool.destroy();
    }

}
