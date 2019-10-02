package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Common {

	public static BukkitTask runAsync(final int delay, final Runnable runnable) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(SchemManager.getInstance(), runnable, delay);
	}

	public static BukkitTask runAsync(final Runnable runnable) {
		return runAsync(0, runnable);
	}

	public static BukkitTask runLater(final int delay, final Runnable runnable) {
		return Bukkit.getScheduler().runTaskLater(SchemManager.getInstance(), runnable, delay);
	}

	public static BukkitTask runLater(final Runnable runnable) {
		return runLater(0, runnable);
	}
}
