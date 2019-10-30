package de.zeanon.schemmanager;

import de.leonhard.storage.LightningStorage;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.enums.Comment;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.Reload;
import de.zeanon.schemmanager.global.handlers.CommandHandler;
import de.zeanon.schemmanager.global.handlers.TabCompleter;
import de.zeanon.schemmanager.global.handlers.WakeupListener;
import de.zeanon.schemmanager.global.utils.Update;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import java.io.FileNotFoundException;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SchemManager extends JavaPlugin {

	private static LightningConfig config;
	private static YamlFile weConfig;
	@SuppressWarnings("CanBeFinal")
	private static SchemManager instance;
	@SuppressWarnings("CanBeFinal")
	private static PluginManager pluginManager;

	{
		instance = this;
		pluginManager = Bukkit.getPluginManager();
	}

	public static LightningConfig getLocalConfig() {
		return config;
	}

	public static YamlFile getWeConfig() {
		return weConfig;
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
					try {
						weConfig = LightningStorage.create(Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
												   .reloadSetting(Reload.AUTOMATICALLY)
												   .commentSetting(Comment.SKIP)
												   .dataType(DataType.STANDARD)
												   .asYamlFile();
						System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded successfully.");
					} catch (IllegalStateException e) {
						e.printStackTrace();
						System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
						SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
						return;
					}
					try {
						WorldEditVersionSchemUtils.initWorldEditPlugin();
						WorldEditVersionSchemUtils.initSchemPath();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder.");
						this.enableSleepMode();
						return;
					}
					WorldEditVersionMain.onEnable();
				}
			}
		} else {
			this.enableSleepMode();
		}
	}

	private void enableSleepMode() {
		pluginManager.registerEvents(new WakeupListener(), this);
		System.out.println("[" + getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
		System.out.println("[" + getName() + "] >> " + getName() + " will automatically activate when one of the above gets enabled.");
		System.out.println("[" + getName() + "] >> Rudimentary function like updating and disabling will still work.");
	}

	public static PluginManager getPluginManager() {
		return pluginManager;
	}

	public static SchemManager getInstance() {
		return instance;
	}
}