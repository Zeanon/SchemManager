package de.Zeanon.SchemManager.WorldEdit.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.Zeanon.SchemManager.WorldEdit.Helper.Helper;
import de.Zeanon.SchemManager.WorldEdit.Listener.MyListener;
import de.leonhard.storage.Config;

public class WorldEditVersionMain {
	
	public WorldEditVersionMain(Plugin plugin) {
		WorldEditVersionMain.plugin = plugin;
	}
	
	public static Config config;
	public static Helper helper;
	private static Plugin plugin;
	
	public void onEnable() {
		boolean failedToLoad = false;
		System.out.println("["+plugin.getName()+"] >> Loading Configs");
		try {
			config = new Config("config", plugin.getDataFolder().getAbsolutePath(), "config");
			System.out.println("["+plugin.getName()+"] >> [Configs] >> " + config.getFile().getName() +" loaded");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("["+plugin.getName()+"] >> [Configs] >> " + config.getFile().getName() +" could not be loaded");
			failedToLoad = true;
		}
		if (failedToLoad) {
			System.out.println("["+plugin.getName()+"] >> Could not load config files... unloading Plugin...");
			disable();
			return;
		}
		else {
			System.out.println("["+plugin.getName()+"] >> Config files are loaded sucessfully");
		}
		
		helper = new Helper(plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new MyListener(plugin), plugin);
		
		if (!Helper.updateConfig()) {
			disable();
			return;
		}
		else {
			System.out.println("[" + plugin.getName() + "] >> " + plugin.getName() + " v" + plugin.getDescription().getVersion() + " successfully launched...");
		}
	}
	
	
	public static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
}