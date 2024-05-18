package me.leoo.utils.bukkit.task.folia;

import me.leoo.utils.bukkit.Utils;
import me.leoo.utils.bukkit.task.ScheduledTask;
import me.leoo.utils.bukkit.task.Tasks;
import org.bukkit.Bukkit;

public class FoliaScheduler extends Tasks {

    @Override
    public ScheduledTask run(Runnable runnable) {
        return new FoliaTask(Bukkit.getGlobalRegionScheduler().run(Utils.get(), task -> runnable.run()));
    }

    @Override
    public ScheduledTask async(Runnable runnable) {
        return run(runnable);
    }

    @Override
    public ScheduledTask later(Runnable runnable, long delay) {
        return new FoliaTask(Bukkit.getGlobalRegionScheduler().runDelayed(Utils.get(), task -> runnable.run(), delay));
    }

    @Override
    public ScheduledTask asyncLater(Runnable runnable, long delay) {
        return later(runnable, delay);
    }

    @Override
    public ScheduledTask timer(Runnable runnable, long delay, long interval) {
        if(delay <= 0) delay = 1;

        return new FoliaTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(Utils.get(), task -> runnable.run(), delay, interval));
    }

    @Override
    public ScheduledTask asyncTimer(Runnable runnable, long delay, long interval) {
        return timer(runnable, delay, interval);
    }
}
