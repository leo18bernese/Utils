package me.leoo.utils.velocity.listener;

import me.leoo.utils.velocity.Utils;

public class ListenerManager {

    public static void registerListeners(Object plugin, Object... listeners) {
        for (Object listener : listeners) {
            registerListener(plugin, listener);
        }
    }

    public static void registerListener(Object plugin, Object listener) {
        Utils.getInstance().getServer().getEventManager().register(plugin, listener);
    }
}
