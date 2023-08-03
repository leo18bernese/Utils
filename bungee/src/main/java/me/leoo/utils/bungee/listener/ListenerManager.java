package me.leoo.utils.bungee.listener;

import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.plugin.Listener;

public class ListenerManager {

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }

    public static void registerListener(Listener listener) {
        Utils.get().getProxy().getPluginManager().registerListener(Utils.get(), listener);
    }
}
