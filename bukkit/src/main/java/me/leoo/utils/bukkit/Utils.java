package me.leoo.utils.bukkit;

import lombok.Getter;
import me.leoo.utils.bukkit.chat.ChatMessage;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.config.ConfigSection;
import me.leoo.utils.bukkit.items.InteractListeners;
import me.leoo.utils.bukkit.menu.MenuListeners;
import me.leoo.utils.bukkit.menu.MenuTask;
import me.leoo.utils.bukkit.software.Software;
import me.leoo.utils.common.compatibility.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Utils extends JavaPlugin {

    private static Plugin plugin;

    @Getter
    private static Plugin initializedFrom;

    public static Supplier<ConfigManager> language;
    public static List<String> menuFunctions = new ArrayList<>();
    public static int menuUpdate = 60;
    public static boolean setNumberInventoryTitle = true;

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
    public static void initialize(JavaPlugin plugin) {
        initializedFrom = plugin;

        CommonUtils.init(new Software());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(get(), "BungeeCord");

        new MenuTask();

        ChatMessage.register();
        MenuListeners.register();
        InteractListeners.register();
    }

    public static void setLanguage(Supplier<ConfigManager> language) {
        Utils.language = language;
    }

    public static void setMenuFunctions(String... functions) {
        menuFunctions.addAll(Arrays.asList(functions));
    }

    public static void setMenuUpdate(int menuUpdate) {
        Utils.menuUpdate = menuUpdate;
    }

    public static void setNumberInventoryTitle(boolean setNumberInventoryTitle) {
        Utils.setNumberInventoryTitle = setNumberInventoryTitle;
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

    public static ConfigSection getLanguage(ConfigSection config) {
        return language != null ? language.get() : config;
    }
}