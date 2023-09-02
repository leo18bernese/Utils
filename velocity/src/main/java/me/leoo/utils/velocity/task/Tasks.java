package me.leoo.utils.velocity.task;

import com.velocitypowered.api.scheduler.ScheduledTask;
import me.leoo.utils.velocity.Utils;

import java.util.concurrent.TimeUnit;

public class Tasks {

    public static ScheduledTask runLater(Object plugin, Runnable task, int seconds) {
        return Utils.getInstance().getServer().getScheduler()
                .buildTask(plugin, task)
                .delay(seconds, TimeUnit.SECONDS)
                .schedule();
    }

    public static ScheduledTask runRepeating(Object plugin, Runnable task, int seconds) {
        return Utils.getInstance().getServer().getScheduler()
                .buildTask(plugin, task)
                .repeat(seconds, TimeUnit.SECONDS)
                .schedule();
    }
}
