package me.leoo.utils.velocity.task;

import me.leoo.utils.velocity.Utils;

import java.util.concurrent.TimeUnit;

public class Tasks {

    public static void runLater(Runnable task, int seconds) {
        Utils.get().getServer().getScheduler()
                .buildTask(Utils.get(), task)
                .delay(seconds, TimeUnit.SECONDS)
                .schedule();
    }

    public static void runRepeating(Runnable task, int seconds) {
        Utils.get().getServer().getScheduler()
                .buildTask(Utils.get(), task)
                .repeat(seconds, TimeUnit.SECONDS)
                .schedule();
    }
}
