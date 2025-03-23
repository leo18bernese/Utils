package me.leoo.utils.velocity.task;

import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.experimental.UtilityClass;
import me.leoo.utils.velocity.Utils;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class Tasks {

    public ScheduledTask runLater(Object plugin, Runnable task, int seconds) {
        return runLater(plugin, task, seconds, TimeUnit.SECONDS);
    }

    public ScheduledTask runLater(Object plugin, Runnable task, int time, TimeUnit unit) {
        return Utils.getInstance().getProxy().getScheduler()
                .buildTask(plugin, task)
                .delay(time, unit)
                .schedule();
    }


    public ScheduledTask runRepeating(Object plugin, Runnable task, int seconds) {
        return runRepeating(plugin, task, seconds, TimeUnit.SECONDS);
    }

    public ScheduledTask runRepeating(Object plugin, Runnable task, int time, TimeUnit unit) {
        return Utils.getInstance().getProxy().getScheduler()
                .buildTask(plugin, task)
                .repeat(time, unit)
                .schedule();
    }
}
