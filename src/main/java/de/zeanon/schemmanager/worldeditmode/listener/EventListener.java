package de.zeanon.schemmanager.worldeditmode.listener;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.RequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		} else if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldGuard")) {
			EventListener.setWorldguardEnabled(false);
		}
	}

	@EventHandler
	public void onPluginEnable(final @NotNull PluginEnableEvent event) {
		if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
		} else if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldGuard")) {
			EventListener.setWorldguardEnabled(true);
		}
	}


	@EventHandler
	public void onQuit(final @NotNull PlayerQuitEvent event) {
		final @NotNull Player p = event.getPlayer();
		RequestUtils.removeDisableRequest(p);
		RequestUtils.removeUpdateRequest(p);
		WorldEditModeRequestUtils.removeDeleteRequest(p);
		WorldEditModeRequestUtils.removeDeleteFolderRequest(p);
		WorldEditModeRequestUtils.removeRenameRequest(p);
		WorldEditModeRequestUtils.removeRenameFolderRequest(p);
		WorldEditModeRequestUtils.removeOverWriteRequest(p);
	}
}