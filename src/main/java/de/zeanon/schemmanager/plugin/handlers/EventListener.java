package de.zeanon.schemmanager.plugin.handlers;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.jetbrains.annotations.NotNull;


public class EventListener implements Listener {

	@Setter(AccessLevel.PRIVATE)
	static boolean worldguardEnabled = SchemManager.getPluginManager().getPlugin("WorldGuard") != null
									   && SchemManager.getPluginManager().isPluginEnabled("WorldGuard");


	@EventHandler
	public void onPluginDisable(final @NotNull PluginDisableEvent event) {
		if (event.getPlugin().getName().equalsIgnoreCase("WorldEdit")
			|| event.getPlugin().getName().equalsIgnoreCase("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		} else if (event.getPlugin().getName().equalsIgnoreCase("WorldGuard")) {
			EventListener.setWorldguardEnabled(false);
		}
	}

	@EventHandler
	public void onPluginEnable(final @NotNull PluginEnableEvent event) {
		if (event.getPlugin().getName().equalsIgnoreCase("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		} else if (event.getPlugin().getName().equalsIgnoreCase("WorldGuard")) {
			EventListener.setWorldguardEnabled(true);
		}
	}


	@EventHandler
	public void onJoin(final @NotNull PlayerJoinEvent event) {
		Update.updateAvailable(event.getPlayer());
	}

	@EventHandler
	public void onQuit(final @NotNull PlayerQuitEvent event) {
		final @NotNull Player p = event.getPlayer();
		GlobalRequestUtils.removeDisableRequest(p.getUniqueId().toString());
		GlobalRequestUtils.removeUpdateRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeDeleteRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeDeleteFolderRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeRenameRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeRenameFolderRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeOverWriteRequest(p.getUniqueId().toString());
		CommandRequestUtils.removeDownloadRequest(p.getUniqueId().toString());
	}
}