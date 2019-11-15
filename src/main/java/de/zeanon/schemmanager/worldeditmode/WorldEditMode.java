package de.zeanon.schemmanager.worldeditmode;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditmode.listener.CommandListener;
import de.zeanon.schemmanager.worldeditmode.listener.EventListener;
import de.zeanon.schemmanager.worldeditmode.listener.tabcompleter.PaperTabListener;
import de.zeanon.schemmanager.worldeditmode.listener.tabcompleter.SpigotTabListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldEditMode {

	@Nullable
	@Getter
	private static final WorldEditPlugin worldEditPlugin;

	static {
		worldEditPlugin = (WorldEditPlugin) SchemManager.getPluginManager().getPlugin("WorldEdit");
	}

	public static void onEnable() {
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