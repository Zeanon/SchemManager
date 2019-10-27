package de.zeanon.schemmanager.worldeditversion;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import de.zeanon.schemmanager.worldeditversion.listener.tabcompleter.PaperTabListener;
import de.zeanon.schemmanager.worldeditversion.listener.tabcompleter.SpigotTabListener;
import org.bukkit.Bukkit;


public class WorldEditVersionMain {

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
}