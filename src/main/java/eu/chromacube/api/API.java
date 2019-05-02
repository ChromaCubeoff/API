package eu.chromacube.api;

import org.bukkit.plugin.java.JavaPlugin;

public class API extends JavaPlugin {

    private static API api;
    @Override
    public void onEnable() {
        api = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static API get() { return api; }
}
