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

    public void times(Runnable runnable, int times, long delay) {
        BukkitTask task = timer(runnable, 0, delay);

        later(task::cancel, delay * times);
    }
}
