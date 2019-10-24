package de.zeanon.schemmanager;

import de.leonhard.storage.LightningStorage;
import de.leonhard.storage.lightningstorage.internal.datafiles.config.LightningConfig;
import de.zeanon.schemmanager.utils.CommandHandler;
import de.zeanon.schemmanager.utils.TabCompleter;
import de.zeanon.schemmanager.utils.Update;
import de.zeanon.schemmanager.utils.WakeupListener;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SchemManager extends JavaPlugin {

	public static LightningConfig config;
	@Getter
	private static SchemManager instance;
	@Getter
	private static PluginManager pluginManager;

	{
		instance = this;
		pluginManager = Bukkit.getPluginManager();
	}

	@Override
	public void onDisable() {
		System.out.println("[" + getName() + "] >> unloaded.");
	}

	@Override
	public void onEnable() {
		Objects.requireNonNull(getCommand("schemmanager")).setExecutor(new CommandHandler());
		Objects.requireNonNull(getCommand("schemmanager")).setTabCompleter(new TabCompleter());
		/*if (pluginManager.getPlugin("FastAsyncWorldEdit") != null && pluginManager.isPluginEnabled("FastAsyncWorldEdit"))) {
		//TODO
		}
		else */
		if (pluginManager.getPlugin("WorldEdit") != null && pluginManager.isPluginEnabled("WorldEdit")) {
			boolean failedToLoad = false;
			System.out.println("[" + getName() + "] >> Launching WorldEdit Version of " + getName() + ".");
			System.out.println("[" + getName() + "] >> Loading Configs.");
			try {
				config = LightningStorage.create(getDataFolder(), "config")
										 .fromResource("resources/config.ls")
										 .asLightningConfig();
				System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " loaded.");
			} catch (IllegalStateException e) {
				e.printStackTrace();
				System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " could not be loaded");
				failedToLoad = true;
			}
			if (failedToLoad) {
				System.out.println("[" + getName() + "] >> Could not load config files... unloading Plugin...");
				SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			} else {
				if (!Update.updateConfig(false)) {
					SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
				} else {
					System.out.println("[" + getName() + "] >> Config files are loaded successfully.");
					WorldEditVersionMain.onEnable();
				}
			}
		} else {
			pluginManager.registerEvents(new WakeupListener(), this);
			System.out.println("[" + getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
			System.out.println("[" + getName() + "] >> " + getName() + " will automatically activate when one of the above gets enabled.");
			System.out.println("[" + getName() + "] >> Rudimentary function like updating and disabling will still work.");
		}
	}
}