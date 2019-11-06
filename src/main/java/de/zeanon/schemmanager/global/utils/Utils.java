package de.zeanon.schemmanager.global.utils;

import de.leonhard.storage.LightningStorage;
import de.leonhard.storage.internal.datafiles.config.LightningConfig;
import de.leonhard.storage.internal.datafiles.raw.YamlFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.handlers.WakeupListener;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import java.io.FileNotFoundException;
import java.util.Objects;


public class Utils {

	private static YamlFile weConfig;
	private static LightningConfig config;

	public static YamlFile getWeConfig() {
		return weConfig;
	}

	public static void initPlugin() {
		/*if (pluginManager.getPlugin("FastAsyncWorldEdit") != null && pluginManager.isPluginEnabled("FastAsyncWorldEdit"))) {
		//TODO
		}
		else */
		if (SchemManager.getPluginManager().getPlugin("WorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("WorldEdit")) {
			initWorldEditMode();
		} else {
			enableSleepMode();
		}
	}

	private static void initWorldEditMode() {
		WorldEditMode.initWorldEditPlugin();
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Launching WorldEdit Version of " + SchemManager.getInstance().getName() + ".");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Loading Configs...");

		try {
			loadConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are loaded successfully.");

			try {
				Update.updateConfig(false);
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are updated successfully.");

				try {
					weConfig = LightningStorage.create(Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
											   .reloadSetting(Reload.AUTOMATICALLY)
											   .commentSetting(Comment.SKIP)
											   .dataType(DataType.STANDARD)
											   .asYamlFile();
					System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded successfully.");
				} catch (IllegalStateException e) {
					e.printStackTrace();
					System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
					enableSleepMode();
					return;
				}
				try {
					WorldEditModeSchemUtils.initSchemPath();
					System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Schematic folder is loaded successfully.");
					WorldEditMode.onEnable();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder.");
					enableSleepMode();
				}
			} catch (IllegalStateException e) {
				System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not update config files.");
				System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
				System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");
				SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			}
		} catch (IllegalStateException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load config files.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
		}
	}

	private static void loadConfigs() {
		boolean failedToLoad = false;
		try {
			config = LightningStorage.create(SchemManager.getInstance().getDataFolder(), "config")
									 .fromResource("resources/config.ls")
									 .asLightningConfig();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + config.getName() + " loaded.");
		} catch (IllegalStateException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> " + config.getName() + " could not be loaded.");
			e.printStackTrace();
			failedToLoad = true;
		}
		if (failedToLoad) {
			throw new IllegalStateException();
		}
	}

	private static void enableSleepMode() {
		SchemManager.getPluginManager().registerEvents(new WakeupListener(), SchemManager.getInstance());
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance().getName() + " will automatically activate when one of the above gets enabled.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Rudimentary function like updating and disabling will still work.");
	}

	static LightningConfig getConfig() {
		return config;
	}
}