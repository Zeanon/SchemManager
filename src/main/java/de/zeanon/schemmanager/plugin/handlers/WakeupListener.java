package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;


public class WakeupListener implements Listener {

	@EventHandler
	public void onPluginEnable(final @NotNull PluginEnableEvent event) {
		if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldEdit")
			|| event.getPlugin() == SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		}
	}

	@EventHandler
	public void onJoin(final @NotNull PlayerJoinEvent event) {
		Update.updateAvailable(event.getPlayer());
	}

	@EventHandler
	public void onQuit(final @NotNull PlayerQuitEvent event) {
		final @NotNull String uuid = event.getPlayer().getUniqueId().toString();
		GlobalRequestUtils.removeDisableRequest(uuid);
		GlobalRequestUtils.removeUpdateRequest(uuid);
	}
}