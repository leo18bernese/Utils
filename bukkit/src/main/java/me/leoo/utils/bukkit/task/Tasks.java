package me.leoo.utils.bukkit.task;

import me.leoo.utils.bukkit.task.bukkit.SpigotScheduler;
import me.leoo.utils.bukkit.task.folia.FoliaScheduler;
import me.leoo.utils.common.reflection.ReflectionUtil;

public abstract class Tasks {

    private static boolean FOLIA;
    private static Tasks INSTANCE;

    public abstract ScheduledTask run(Runnable runnable);

    public abstract ScheduledTask async(Runnable runnable);

    public abstract ScheduledTask later(Runnable runnable, long delay);

    public abstract ScheduledTask asyncLater(Runnable runnable, long delay);

    public abstract ScheduledTask timer(Runnable runnable, long delay, long interval);

    public abstract ScheduledTask asyncTimer(Runnable runnable, long delay, long interval);

    public static Tasks get() {
        if (INSTANCE == null) {
            FOLIA = ReflectionUtil.existClass("io.papermc.paper.threadedregions.RegionizedServer");

            INSTANCE = FOLIA ? new FoliaScheduler() : new SpigotScheduler();
        }

        return INSTANCE;
    }
}
