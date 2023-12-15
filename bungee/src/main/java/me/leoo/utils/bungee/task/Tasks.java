package me.leoo.utils.bungee.task;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bungee.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class Tasks {

    public ScheduledTask runLater(Runnable task, int seconds) {
        return ProxyServer.getInstance().getScheduler().schedule(Utils.get(), task, seconds, TimeUnit.SECONDS);
    }

    public ScheduledTask runAsync(Runnable task) {
        return ProxyServer.getInstance().getScheduler().runAsync(Utils.get(), task);
    }
}
