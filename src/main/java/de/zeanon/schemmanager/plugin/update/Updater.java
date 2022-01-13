package de.zeanon.schemmanager.plugin.update;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public interface Updater {

	void updatePlugin(final boolean autoReload, final @NotNull JavaPlugin instance);

	void updatePlugin(final @NotNull Player p, final boolean autoReload, final @NotNull JavaPlugin instance);
}