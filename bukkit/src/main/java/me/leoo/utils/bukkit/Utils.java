package me.leoo.utils.bukkit;

import lombok.Getter;
import me.leoo.utils.bukkit.chat.ChatMessage;
import me.leoo.utils.bukkit.items.InteractListeners;
import me.leoo.utils.bukkit.menu.MenuListeners;
import me.leoo.utils.bukkit.menu.MenuTask;
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

    @Override
    public void onDisable() {
        disable();
    }

    /**
     * Initialize utils.
     * Must be executed before running anything related to this plugin.
     */
    public static void initialize(Plugin plugin) {
        initializedFrom = plugin;

        SoftwareManager.setUtils(new Software());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(get(), "BungeeCord");

        new MenuTask();

        ChatMessage.register();
        MenuListeners.register();
        InteractListeners.register();
    }

    public static void disable() {
        if (plugin == null) return;

        Bukkit.getScheduler().cancelTasks(plugin);
    }


    public static Plugin get() {
        if (plugin == null) {
            plugin = JavaPlugin.getProvidingPlugin(Utils.class);
        }
        return plugin;
    }
}