package me.leoo.utils.bungee.task;

import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class Tasks {

    public static void runLater(Runnable task, int seconds) {
        ProxyServer.getInstance().getScheduler().schedule(Utils.get(), task, seconds, TimeUnit.SECONDS);
    }

    public static void runAsync(Runnable task) {
        ProxyServer.getInstance().getScheduler().runAsync(Utils.get(), task);
    }
}
