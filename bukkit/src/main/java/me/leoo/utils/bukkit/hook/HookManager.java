package me.leoo.utils.bukkit.hook;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;

@UtilityClass
public class HookManager {

    /**
     * Check if a plugin is enabled.
     *
     * @param pluginName The name of the plugin to check.
     * @return true if the plugin is enabled, false otherwise.
     */
    public boolean isEnabled(String pluginName) {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName);
    }

    /**
     * Check if a plugin is enabled and disable the current plugin if not.
     *
     * @param pluginName The name of the plugin to check.
     * @return true if the plugin is enabled, false otherwise.
     */
    public boolean checkRequired(String pluginName) {
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {

            Bukkit.getLogger().severe(pluginName + " not found! Disabling...");
            Bukkit.getServer().getPluginManager().disablePlugin(Utils.getInitializedFrom());

            return false;
        }

        Bukkit.getLogger().info(pluginName + " enabled, hooking...");

        return true;
    }
}
