package eu.chromacube.api;

import eu.chromacube.api.database.mysql.SQLConnection;
import eu.chromacube.api.database.redis.REDISConnection;
import eu.chromacube.api.tools.CreativeInventory;
import eu.chromacube.api.tools.CreativeItem;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class API extends JavaPlugin {

    private SQLConnection sql;
    private REDISConnection redisConnection;

    private static API api;
    @Override
    public void onEnable() {
        api = this;
        log("#==========[WELCOME TO CHROMACUBE API]==========#");
        log("#  ChromaAPI is now loading. Please read        #");
        log("#  carefully all outputs coming from it.        #");
        log("#  by ChromaCube start the 02/05/2019           #");
        log("#===============================================#");


        redisConnection = new REDISConnection("localhost", "8LgH2Yd8u", 6379, 4);
        redisConnection.connect();

        new CreativeInventory(this).init();
        new CreativeItem(this).init();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }


    public REDISConnection getRedisConnection() { return redisConnection; }
    public SQLConnection getMySQL() {return sql;}
    public static API get() { return api; }
    public void log(Level level, String msg) { this.getLogger().log(level, msg); }
    public void log(String msg) { this.getLogger().info(msg); }


    private void initConnection() {
            String user = "chroma";
            String password = "ReWQi954x";
            String host = "localhost";
            String database = "chromacube";
        try {
            BasicDataSource connectionPool = new BasicDataSource();
            connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
            connectionPool.setUsername(user);
            connectionPool.setPassword(password);
            connectionPool.setUrl("jdbc:mysql://"+host+":3306/"+database+"?autoReconnect=true");
            connectionPool.setInitialSize(1);
            connectionPool.setMaxTotal(10);
            sql = new SQLConnection(connectionPool);
            log("[MySQL] connected");
        } catch (Exception e) {
            log("[MySQL] failure");
            e.printStackTrace();
        }
    }

}
