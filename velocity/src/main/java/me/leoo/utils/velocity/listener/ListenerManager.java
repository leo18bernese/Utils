package me.leoo.utils.velocity.listener;

import me.leoo.utils.velocity.Utils;

public class ListenerManager {

    public static void registerListeners(Object... listeners) {
        for (Object listener : listeners) {
            registerListener(listener);
        }
    }

    public static void registerListener(Object listener) {
        Utils.get().getServer().getEventManager().register(Utils.get(), listener);
    }
}
