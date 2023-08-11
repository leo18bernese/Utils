package me.leoo.utils.bukkit.task;

import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Tasks {

    public static BukkitTask run(Runnable task) {
        return Bukkit.getScheduler().runTask(Utils.get(), task);
    }

    public static BukkitTask runAsync(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(Utils.get(), task);
    }

    public static BukkitTask runLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLater(Utils.get(), task, delay);
    }

    public static BukkitTask runAsyncLater(Runnable task, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.get(), task, delay);
    }

    public static BukkitTask runTimer(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(Utils.get(), task, delay, interval);
    }

    public static BukkitTask runAsyncTimer(Runnable task, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Utils.get(), task, delay, interval);
    }

}
