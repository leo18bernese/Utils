package me.leoo.utils.bukkit.task;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Supplier;

@UtilityClass
public class Tasks {

    public BukkitTask run(Runnable task) {
        return Bukkit.getScheduler().runTask(Utils.get(), task);
    }

    public BukkitTask async(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(Utils.get(), task);
    }

    public BukkitTask later(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(Utils.get(), task, delay);
    }

    public BukkitTask asyncLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.get(), task, delay);
    }

    public BukkitTask timer(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(Utils.get(), task, delay, interval);
    }

    public BukkitTask asyncTimer(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Utils.get(), task, delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, int times) {
        new BukkitRunnable() {
            int remaining = times;

            @Override
            public void run() {
                task.run();

                if (--remaining == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, int times, Runnable onEnd) {
        new BukkitRunnable() {
            int remaining = times;

            @Override
            public void run() {
                task.run();

                if (--remaining == 0) {
                    onEnd.run();

                    cancel();
                }
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, Supplier<Boolean> condition) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!condition.get()) {
                    cancel();
                    return;
                }
                task.run();
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }

    public void repeat(Runnable task, long delay, long interval, Supplier<Boolean> condition, Runnable onEnd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!condition.get()) {
                    onEnd.run();

                    cancel();
                    return;
                }
                task.run();
            }
        }.runTaskTimer(Utils.get(), delay, interval);
    }
}
