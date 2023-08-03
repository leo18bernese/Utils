package me.leoo.utils.bukkit;

import me.leoo.utils.bukkit.software.Software;
import me.leoo.utils.common.compatibility.SoftwareUtils;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Utils extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        SoftwareUtils.setInstance(new Software());
    }

    public static Plugin get() {
        /*if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }*/
        return plugin;
    }
}