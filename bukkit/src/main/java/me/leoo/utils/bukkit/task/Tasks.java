package me.leoo.utils.bukkit.task;

import lombok.experimental.UtilityClass;
import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

@UtilityClass
public class Tasks {

    private static final Plugin PLUGIN = Utils.getInitializedFrom();

    public BukkitTask run(Runnable runnable) {
        return Bukkit.getScheduler().runTask(PLUGIN, runnable);
    }

    public BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, runnable);
    }

    public BukkitTask later(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(PLUGIN, runnable, delay);
    }

    public BukkitTask asyncLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, runnable, delay);
    }

    public BukkitTask timer(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(PLUGIN, runnable, delay, interval);
    }

    public BukkitTask asyncTimer(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(PLUGIN, runnable, delay, interval);
    }

    /*private static final PlatformScheduler SCHEDULER = Utils.foliaLib.getScheduler();

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
    }*/
}
