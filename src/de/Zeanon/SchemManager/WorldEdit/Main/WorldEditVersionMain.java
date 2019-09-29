package de.Zeanon.SchemManager.WorldEdit.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.Zeanon.SchemManager.WorldEdit.Helper.Helper;
import de.Zeanon.SchemManager.WorldEdit.Listener.MyListener;
import de.leonhard.storage.Config;

public class WorldEditVersionMain {
	
	public static Config config;
	public static Helper helper;
	private static Plugin plugin;
	
	public void onEnable(Plugin plugin) {
		WorldEditVersionMain.plugin = plugin;
		try {
			config = new Config("config", plugin.getDataFolder().getAbsolutePath(), "config");
			System.out.println("["+plugin.getName()+"] >> [Configs] >> " + config.getFile().getName() +" loaded");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("["+plugin.getName()+"] >> [Configs] >> " + config.getFile().getName() +" could not be loaded");
			disable();
			return;
		}
		
		helper = new Helper(plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new MyListener(), plugin);
		
		if (!Helper.updateConfig()) {
			disable();
			return;
		}
		else {
			System.out.println("[" + plugin.getName() + "] " + plugin.getName() + " v" + plugin.getDescription().getVersion() + " successfully launched...");
		}
	}
	
	public static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
}