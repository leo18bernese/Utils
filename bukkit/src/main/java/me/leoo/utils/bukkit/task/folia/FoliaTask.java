package me.leoo.utils.bukkit.task.folia;

import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.task.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class FoliaTask implements ScheduledTask {

    private final io.papermc.paper.threadedregions.scheduler.ScheduledTask task;


    @Override
    public int getTaskId() {
        return -1;
    }

    @Override
    public @NotNull Plugin getOwner() {
        return task.getOwningPlugin();
    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return task.getExecutionState() == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.CANCELLED ||
                task.getExecutionState() == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
