package me.leoo.utils.bungee.listener;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

@UtilityClass
public class ListenerManager {

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            registerListener(listener);
        }
    }

    public void registerListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(Utils.get(), listener);
    }
}
