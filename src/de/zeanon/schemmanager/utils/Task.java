package de.zeanon.schemmanager.utils;

import de.zeanon.schemmanager.SchemManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Task {

    public static <T> void runAsync(Callable<T> callable, Consumer<Future<T>> consumer) {
        FutureTask<T> task = new FutureTask<>(callable);

        new BukkitRunnable() {

            @Override
            public void run() {
                task.run();

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        consumer.accept(task);
                    }
                }.runTask(SchemManager.getInstance());
            }
        }.runTaskAsynchronously(SchemManager.getInstance());
    }
}
