package de.zeanon.schemmanager.worldeditmode;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditmode.listener.CommandListener;
import de.zeanon.schemmanager.worldeditmode.listener.EventListener;
import de.zeanon.schemmanager.worldeditmode.listener.tabcompleter.PaperTabListener;
import de.zeanon.schemmanager.worldeditmode.listener.tabcompleter.SpigotTabListener;
import org.bukkit.Bukkit;


public class WorldEditMode {

	private static WorldEditPlugin we;

	public static void onEnable() {
		SchemManager.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
		SchemManager.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
		if (Bukkit.getVersion().contains("git-Paper")) {
			SchemManager.getPluginManager().registerEvents(new PaperTabListener(), SchemManager.getInstance());
		} else {
			SchemManager.getPluginManager().registerEvents(new SpigotTabListener(), SchemManager.getInstance());
		}
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
	}

	public static void initWorldEditPlugin() {
		we = (WorldEditPlugin) SchemManager.getPluginManager().getPlugin("WorldEdit");
	}

	public static WorldEditPlugin getWorldEditPlugin() {
		return we;
	}
}