package me.leoo.utils.bukkit.task;

import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.Utils;

@UtilityClass
public class Tasks {

    private static final PlatformScheduler SCHEDULER = Utils.foliaLib.getScheduler();

    public void run(Runnable runnable) {
        SCHEDULER.runNextTick(task -> runnable.run());
    }

    public void async(Runnable runnable) {
        SCHEDULER.runAsync(task -> runnable.run());
    }

    public WrappedTask later(Runnable runnable, long delay) {
        return SCHEDULER.runLater(runnable, delay);
    }

    public WrappedTask asyncLater(Runnable runnable, long delay) {
        return SCHEDULER.runLaterAsync(runnable, delay);
    }

    public WrappedTask timer(Runnable runnable, long delay, long interval) {
        return SCHEDULER.runTimer(runnable, delay, interval);
    }

    public WrappedTask asyncTimer(Runnable runnable, long delay, long interval) {
        return SCHEDULER.runTimerAsync(runnable, delay, interval);
    }
}
