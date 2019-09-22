package de.Zeanon.SchemManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static Config config;
	public static Helper helper;
	
	@Override
	public void onEnable() {
		config = new Config("config.yml", "config.yml", this);
		MyListener listener = new MyListener();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		helper = new Helper(this);
		pm.registerEvents(listener, this);
		if (!config.isLoaded()) {
			System.out.println("[SchemManager] Could not load Config file, shutting down");
			Helper.disable();
			return;
		}
		if (!config.hasKey("Automatic Reload")) {
			config.setBoolean("Automatic Reload", true);
		}
		System.out.println("[SchemManager] Schemmanager v" + Bukkit.getServer().getPluginManager().getPlugin("SchemManager").getDescription().getVersion() + " successfully launched...");
	}
}