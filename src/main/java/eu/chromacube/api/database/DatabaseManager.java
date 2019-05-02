package eu.chromacube.api.database;

import eu.chromacube.api.API;
import eu.chromacube.api.database.redis.REDISConnection;

public class DatabaseManager {

    private API api;

    public DatabaseManager(API api) {
        this.api = api;
    }

    public void init(){
        new REDISConnection("localhost", "?", )
    }
}
