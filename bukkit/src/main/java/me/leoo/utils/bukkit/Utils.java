package me.leoo.utils.bukkit;

import lombok.Getter;
import me.leoo.utils.bukkit.software.Software;
import me.leoo.utils.common.compatibility.SoftwareManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils extends JavaPlugin {

    private static Plugin plugin;

    @Getter
    private static Plugin initializedFrom;

    @Override
    public void onEnable() {
        plugin = this;
    }

    /**
     * Initialize utils.
     * Must be executed before running anything related to this plugin.
     */
    public static void initialize(Plugin plugin) {
        initializedFrom = plugin;

        SoftwareManager.setUtils(new Software());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(get(), "BungeeCord");
    }

    public static Plugin get() {
        if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }
        return plugin;
    }
}