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
		else */if (pm.getPlugin("WorldEdit").isEnabled()) {
			WorldEditVersionMain main = new WorldEditVersionMain();
			main.onEnable(this);
			return;
		}
		else {
			System.out.println("[" + this.getName() + "] could not load plugin, it needs WorldEdit or FastAsyncWorldEdit to work");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
	}
}