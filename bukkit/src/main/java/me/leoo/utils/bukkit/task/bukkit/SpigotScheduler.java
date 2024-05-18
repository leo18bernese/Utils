package me.leoo.utils.bukkit.task.bukkit;

import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.task.ScheduledTask;
import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.Bukkit;

public class SpigotScheduler extends Tasks {

    @Override
    public ScheduledTask run(Runnable runnable) {
        return new SpigotTask(Bukkit.getScheduler().runTask(Utils.get(), runnable));
    }

    @Override
    public ScheduledTask async(Runnable runnable) {
        return new SpigotTask(Bukkit.getScheduler().runTaskAsynchronously(Utils.get(), runnable));
    }

    @Override
    public ScheduledTask later(Runnable runnable, long delay) {
        return new SpigotTask(Bukkit.getScheduler().runTaskLater(Utils.get(), runnable, delay));
    }

    @Override
    public ScheduledTask asyncLater(Runnable runnable, long delay) {
        return new SpigotTask(Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.get(), runnable, delay));
    }

    @Override
    public ScheduledTask timer(Runnable runnable, long delay, long interval) {
        return new SpigotTask(Bukkit.getScheduler().runTaskTimer(Utils.get(), runnable, delay, interval));
    }

    @Override
    public ScheduledTask asyncTimer(Runnable runnable, long delay, long interval) {
        return new SpigotTask(Bukkit.getScheduler().runTaskTimerAsynchronously(Utils.get(), runnable, delay, interval));
    }
}
