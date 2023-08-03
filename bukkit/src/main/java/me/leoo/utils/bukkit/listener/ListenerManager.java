package me.leoo.utils.bukkit.listener;

import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerManager {

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Utils.get());
    }
}
