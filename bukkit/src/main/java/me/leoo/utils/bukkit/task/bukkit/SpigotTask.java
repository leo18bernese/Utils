package me.leoo.utils.bukkit.task.bukkit;

import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.task.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class SpigotTask implements ScheduledTask {

    private final BukkitTask task;

    @Override
    public int getTaskId() {
        return task.getTaskId();
    }

    @Override
    public @NotNull Plugin getOwner() {
        return task.getOwner();
    }

    @Override
    public boolean isSync() {
        return task.isSync();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
