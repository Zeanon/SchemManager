package de.zeanon.schemmanager;

import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SchemManager extends JavaPlugin {

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		/*if (pm.getPlugin("FastAsyncWorldEdit") != null && pm.getPlugin("FastAsyncWorldEdit").isEnabled()) {
			//TODO
		}
		else */
		if (pm.getPlugin("WorldEdit") != null && Objects.requireNonNull(pm.getPlugin("WorldEdit")).isEnabled()) {
			WorldEditVersionMain main = new WorldEditVersionMain(this);
			main.onEnable();
		} else {
			System.out.println("[" + this.getName() + "] >> could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded");
	}
}