package me.leoo.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    public static Plugin get() {
        if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }
        return plugin;
    }
}