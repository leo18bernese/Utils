package me.leoo.utils.velocity.task;

import com.velocitypowered.api.scheduler.ScheduledTask;
import me.leoo.utils.velocity.Utils;

import java.util.concurrent.TimeUnit;

public class Tasks {

    public static ScheduledTask runLater(Runnable task, int seconds) {
        return Utils.get().getServer().getScheduler()
                .buildTask(Utils.get(), task)
                .delay(seconds, TimeUnit.SECONDS)
                .schedule();
    }

    public static ScheduledTask runRepeating(Runnable task, int seconds) {
        return Utils.get().getServer().getScheduler()
                .buildTask(Utils.get(), task)
                .repeat(seconds, TimeUnit.SECONDS)
                .schedule();
    }
}
