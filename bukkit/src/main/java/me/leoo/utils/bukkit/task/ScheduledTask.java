package me.leoo.utils.bukkit.task;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ScheduledTask {

    int getTaskId();

    @NotNull
    Plugin getOwner();

    boolean isSync();

    boolean isCancelled();

    void cancel();
}
