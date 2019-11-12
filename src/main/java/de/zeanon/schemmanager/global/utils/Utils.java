package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.handlers.WakeupListener;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.data.config.ThunderConfig;
import de.zeanon.storage.internal.data.raw.YamlFile;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.settings.DataType;
import de.zeanon.storage.internal.settings.Reload;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.io.FileNotFoundException;
import lombok.Getter;


public class Utils {

	@Getter
	private static YamlFile weConfig;
	@Getter
	private static ThunderConfig config;

	public static void initPlugin() {
		try {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Loading Configs...");
			loadConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are loaded successfully.");

			try {
				if (!Utils.getConfig().hasKey("Plugin Version")
					|| !Utils.getConfig().getString("Plugin Version")
							 .equals(SchemManager.getInstance().getDescription().getVersion())) {
					System.out.println("[" + SchemManager.getInstance().getName() + "] >> Updating Configs...");
					Update.updateConfig(false);
					System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are updated successfully.");
				}

				/*if (pluginManager.getPlugin("FastAsyncWorldEdit") != null && pluginManager.isPluginEnabled("FastAsyncWorldEdit"))) {
				//TODO
				} else */
				if (SchemManager.getPluginManager().getPlugin("WorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("WorldEdit")) {
					initWorldEditMode();
				} else {
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
			config = StorageManager.thunderConfig(SchemManager.getInstance().getDataFolder(), "config")
								   .fromResource("resources/config.tf")
								   .reloadSetting(Reload.INTELLIGENT)
								   .commentSetting(Comment.PRESERVE)
								   .dataType(DataType.SORTED)
								   .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' loaded.");
		} catch (IllegalStateException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be loaded.");
			e.printStackTrace();
			failedToLoad = true;
		}
		if (failedToLoad) {
			throw new IllegalStateException();
		}
	}

	private static void initWorldEditMode() {
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Launching WorldEdit Version of " + SchemManager.getInstance().getName() + ".");

		try {
			weConfig = StorageManager.yamlFile(Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
									 .reloadSetting(Reload.AUTOMATICALLY)
									 .commentSetting(Comment.SKIP)
									 .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded successfully.");
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
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
			enableSleepMode();
		}
	}

	private static void enableSleepMode() {
		SchemManager.getPluginManager().registerEvents(new WakeupListener(), SchemManager.getInstance());
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance().getName() + " will automatically activate when one of the above gets enabled.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Rudimentary function like updating and disabling will still work.");
	}
}