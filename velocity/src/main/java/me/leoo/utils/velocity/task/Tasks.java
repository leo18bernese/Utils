package me.leoo.utils.velocity.task;

import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class Tasks {

    public ScheduledTask runLater(Object plugin, Runnable task, int seconds) {
        return Utils.getInstance().getProxy().getScheduler()
                .buildTask(plugin, task)
                .delay(seconds, TimeUnit.SECONDS)
                .schedule();
    }

    public ScheduledTask runRepeating(Object plugin, Runnable task, int seconds) {
        return Utils.getInstance().getProxy().getScheduler()
                .buildTask(plugin, task)
                .repeat(seconds, TimeUnit.SECONDS)
                .schedule();
    }
}
