package de.Zeanon.SchemManager.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.Zeanon.SchemManager.WorldEdit.Main.WorldEditVersionMain;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		/*if (pm.getPlugin("FastAsyncWorldEdit").isEnabled()) {
			//TODO
		}
		else */if (pm.getPlugin("WorldEdit") != null && pm.getPlugin("WorldEdit").isEnabled()) {
			WorldEditVersionMain main = new WorldEditVersionMain(this);
			main.onEnable();
			return;
		}
		else {
			System.out.println("[" + this.getName() + "] >> could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	
	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded");
	}
}