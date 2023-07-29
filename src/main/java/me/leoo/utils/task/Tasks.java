package me.leoo.utils.task;

import me.leoo.utils.Utils;

public class Tasks {

    public static void run(Callable callable) {
        Utils.get().getServer().getScheduler().runTask(Utils.get(), callable::call);
    }

    public static void runAsync(Callable callable) {
        Utils.get().getServer().getScheduler().runTaskAsynchronously(Utils.get(), callable::call);
    }

    public static void runLater(Callable callable, long delay) {
        Utils.get().getServer().getScheduler().runTaskLater(Utils.get(), callable::call, delay);
    }

    public static void runAsyncLater(Callable callable, long delay) {
        Utils.get().getServer().getScheduler().runTaskLaterAsynchronously(Utils.get(), callable::call, delay);
    }

    public static void runTimer(Callable callable, long delay, long interval) {
        Utils.get().getServer().getScheduler().runTaskTimer(Utils.get(), callable::call, delay, interval);
    }

    public static void runAsyncTimer(Callable callable, long delay, long interval) {
        Utils.get().getServer().getScheduler().runTaskTimerAsynchronously(Utils.get(), callable::call, delay, interval);
    }

    public interface Callable {
        void call();
    }
}
