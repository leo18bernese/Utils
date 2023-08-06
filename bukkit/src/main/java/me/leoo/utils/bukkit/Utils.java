package me.leoo.utils.bukkit;

import me.leoo.utils.bukkit.software.Software;
import me.leoo.utils.common.compatibility.SoftwareManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    /**
     * Initialize utils.
     * Must be executed before running anything related to this plugin.
     */
    public static void initialize(){
        SoftwareManager.setUtils(new Software());
    }

    public static Plugin get() {
        /*if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }*/
        return plugin;
    }
}