package de.zeanon.schemmanager;

import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SchemManager extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getScheduler().runTask(this, () -> {
			PluginManager pm = Bukkit.getPluginManager();
			/*if (pm.getPlugin("FastAsyncWorldEdit") != null && pm.isPluginEnabled("FastAsyncWorldEdit"))) {
			//TODO
			}
			else */
			if (pm.getPlugin("WorldEdit") != null && pm.isPluginEnabled("WorldEdit")) {
				new WorldEditVersionMain(this).onEnable();
			} else {
				System.out.println("[" + this.getName() + "] >> could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work");
				pm.registerEvents(new WakeupListener(this), this);
			}
		});
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded");
	}
}