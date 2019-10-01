package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import de.zeanon.schemmanager.worldeditversion.listener.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class WorldEditVersionMain {

	public static Config config;
	public static Plugin plugin;

	public WorldEditVersionMain(Plugin plugin) {
		WorldEditVersionMain.plugin = plugin;
	}

	public static void disable() {
		Bukkit.getPluginManager().disablePlugin(plugin);
	}

	public void onEnable() {
		boolean failedToLoad = false;
		System.out.println("[" + plugin.getName() + "] >> Loading Configs");
		try {
			config = new Config("config", plugin.getDataFolder().getAbsolutePath(), "config");
			System.out.println("[" + plugin.getName() + "] >> [Configs] >> " + config.getFile().getName() + " loaded");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[" + plugin.getName() + "] >> [Configs] >> " + config.getFile().getName() + " could not be loaded");
			failedToLoad = true;
		}
		if (failedToLoad) {
			System.out.println("[" + plugin.getName() + "] >> Could not load config files... unloading Plugin...");
			disable();
			return;
		} else {
			System.out.println("[" + plugin.getName() + "] >> Config files are loaded sucessfully");
		}

		Helper.initiate(plugin);

		Bukkit.getServer().getPluginManager().registerEvents(new CommandListener(plugin), plugin);
		Objects.requireNonNull(Bukkit.getPluginCommand("schem")).setTabCompleter(new TabCompleter());
		Objects.requireNonNull(Bukkit.getPluginCommand("schematic")).setTabCompleter(new TabCompleter());

		if (!Helper.updateConfig(false)) {
			disable();
		} else {
			System.out.println("[" + plugin.getName() + "] >> " + plugin.getName() + " v" + plugin.getDescription().getVersion() + " successfully launched...");
		}
	}
}