package me.leoo.utils.bukkit.task;

import me.leoo.utils.bukkit.Utils;
import org.bukkit.Bukkit;

public class Tasks {

    public static void run(Runnable task) {
        Bukkit.getScheduler().runTask(Utils.get(), task);
    }

    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(Utils.get(), task);
    }

    public static void runLater(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(Utils.get(), task, delay);
    }

    public static void runAsyncLater(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.get(), task, delay);
    }

    public static void runTimer(Runnable task, long delay, long interval) {
        Bukkit.getScheduler().runTaskTimer(Utils.get(), task, delay, interval);
    }

    public static void runAsyncTimer(Runnable task, long delay, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Utils.get(), task, delay, interval);
    }

}
