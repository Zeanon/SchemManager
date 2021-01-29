package de.zeanon.schemmanager.init;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.CommandListener;
import de.zeanon.schemmanager.plugin.handlers.EventListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.PaperTabListener;
import de.zeanon.schemmanager.plugin.handlers.tabcompleter.SpigotTabListener;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class RunningMode {

	@Getter
	private final @Nullable
	WorldEditPlugin worldEditPlugin;

	static {
		worldEditPlugin = (WorldEditPlugin) SchemManager.getPluginManager().getPlugin("WorldEdit");
	}

	public void onEnable() {
		SchemManager.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
		SchemManager.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
		if (Bukkit.getVersion().contains("git-Paper")) {
			SchemManager.getPluginManager().registerEvents(new PaperTabListener(), SchemManager.getInstance());
		} else {
			SchemManager.getPluginManager().registerEvents(new SpigotTabListener(), SchemManager.getInstance());
		}
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully.");
	}
}