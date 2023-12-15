package me.leoo.utils.velocity.listener;

import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

@UtilityClass
public class ListenerManager {

    public void registerListeners(Object plugin, Object... listeners) {
        for (Object listener : listeners) {
            registerListener(plugin, listener);
        }
    }

    public void registerListener(Object plugin, Object listener) {
        Utils.getInstance().getProxy().getEventManager().register(plugin, listener);
    }
}
