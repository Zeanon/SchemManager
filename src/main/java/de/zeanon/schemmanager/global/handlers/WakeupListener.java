package de.zeanon.schemmanager.global.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.RequestUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;


public class WakeupListener implements Listener {

	@EventHandler
	public void onPluginEnable(@NotNull final PluginEnableEvent event) {
		if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldEdit")
			|| event.getPlugin() == SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		}
	}

	@EventHandler
	public void onQuit(@NotNull final PlayerQuitEvent event) {
		@NotNull Player p = event.getPlayer();
		RequestUtils.removeDisableRequest(p);
		RequestUtils.removeUpdateRequest(p);
	}
}