package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.commands.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalRequestUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;


public class EventListener implements Listener {


	@EventHandler
	public void onPluginDisable(final @NotNull PluginDisableEvent event) {
		if (event.getPlugin().getName().equalsIgnoreCase("WorldEdit")
			|| event.getPlugin().getName().equalsIgnoreCase("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		}
	}

	@EventHandler
	public void onPluginEnable(final @NotNull PluginEnableEvent event) {
		if (event.getPlugin().getName().equalsIgnoreCase("FastAsyncWorldEdit")) {
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
		final @NotNull Player p = event.getPlayer();
		GlobalRequestUtils.removeDisableRequest(p.getUniqueId());
		GlobalRequestUtils.removeUpdateRequest(p.getUniqueId());
		CommandRequestUtils.removeDeleteRequest(p.getUniqueId());
		CommandRequestUtils.removeDeleteFolderRequest(p.getUniqueId());
		CommandRequestUtils.removeRenameRequest(p.getUniqueId());
		CommandRequestUtils.removeRenameFolderRequest(p.getUniqueId());
		CommandRequestUtils.removeOverWriteRequest(p.getUniqueId());
		CommandRequestUtils.removeDownloadRequest(p.getUniqueId());
	}
}