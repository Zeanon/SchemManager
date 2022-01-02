package de.zeanon.schemmanager.init;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.EventListener;
import de.zeanon.schemmanager.plugin.handlers.SteamEditCommandListener;
import de.zeanon.schemmanager.plugin.handlers.WorldEditCommandListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.paper.PaperTabListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.paper.SteamEditPaperTabListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.spigot.SpigotTabListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.spigot.SteamEditSpigotTabListener;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class RunningMode {

	@Getter
	private final @NotNull WorldEditPlugin worldEditPlugin;
	@Getter
	private final boolean paperSpigot;
	@Getter
	private final boolean steamEdit;

	static {
		worldEditPlugin = (WorldEditPlugin) Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit"));
		paperSpigot = Bukkit.getVersion().contains("git-Paper");
		steamEdit = RunningMode.getWorldEditPlugin().getDescription().getVersion().contains("SteamEdit");
	}

	public void onEnable() {
		if (RunningMode.isSteamEdit()) {
			InitMode.registerEvents(new SteamEditCommandListener());
		} else {
			InitMode.registerEvents(new WorldEditCommandListener());
		}
		InitMode.registerEvents(new EventListener());
		if (RunningMode.isPaperSpigot()) {
			if (RunningMode.isSteamEdit()) {
				InitMode.registerEvents(new SteamEditPaperTabListener());
			} else {
				InitMode.registerEvents(new PaperTabListener());
			}
		} else {
			if (RunningMode.isSteamEdit()) {
				InitMode.registerEvents(new SteamEditSpigotTabListener());
			} else {
				InitMode.registerEvents(new SpigotTabListener());
			}
		}
		SchemManager.getChatLogger().info(">> " + SchemManager.getInstance() + " launched successfully.");
	}
}