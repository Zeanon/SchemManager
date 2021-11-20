package de.zeanon.schemmanager.init;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.CommandListener;
import de.zeanon.schemmanager.plugin.handlers.EventListener;
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
		SchemManager.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
		SchemManager.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
		if (RunningMode.isPaperSpigot()) {
			if (RunningMode.steamEdit) {
				SchemManager.getPluginManager().registerEvents(new SteamEditPaperTabListener(), SchemManager.getInstance());
			} else {
				SchemManager.getPluginManager().registerEvents(new PaperTabListener(), SchemManager.getInstance());
			}
		} else {
			if (RunningMode.steamEdit) {
				SchemManager.getPluginManager().registerEvents(new SteamEditSpigotTabListener(), SchemManager.getInstance());
			} else {
				SchemManager.getPluginManager().registerEvents(new SpigotTabListener(), SchemManager.getInstance());
			}
		}
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully.");
	}
}