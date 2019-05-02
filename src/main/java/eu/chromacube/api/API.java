package eu.chromacube.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class API extends JavaPlugin {

    private static API api;
    @Override
    public void onEnable() {
        log("#==========[WELCOME TO CHROMACUBE API]==========#");
        log("#  ChromaAPI is now loading. Please read        #");
        log("#  carefully all outputs coming from it.        #");
        log("#  by ChromaCube start the 02/05/2019           #");
        log("#===============================================#");

        api = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static API get() { return api; }
    public void log(Level level, String msg) { this.getLogger().log(level, msg); }
    public void log(String msg) { this.getLogger().info(msg); }

}
