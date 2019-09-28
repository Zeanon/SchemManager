package de.Zeanon.SchemManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.leonhard.storage.Config;

public class Main extends JavaPlugin {
	
	public static Config config;
	public static Helper helper;
	
	@Override
	public void onEnable() {
		try {
			config = new Config("config", this.getDataFolder().getAbsolutePath(), "config");
			System.out.println("["+this.getName()+"] >> [Configs] >> " + config.getFile().getName() +" loaded");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("["+this.getName()+"] >> [Configs] >> " + config.getFile().getName() +" could not be loaded");
			Helper.disable();
			return;
		}
		
		MyListener listener = new MyListener();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		helper = new Helper(this);
		pm.registerEvents(listener, this);
		
		if (!Helper.updateConfig()) {
			Helper.disable();
			return;
		}
		System.out.println("[SchemManager] Schemmanager v" + Bukkit.getServer().getPluginManager().getPlugin(this.getName()).getDescription().getVersion() + " successfully launched...");
	}
}