package de.zeanon.schemmanager.global.utils;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.handlers.WakeupListener;
import de.zeanon.schemmanager.worldeditmode.WorldEditMode;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storage.StorageManager;
import de.zeanon.storage.internal.basic.exceptions.FileParseException;
import de.zeanon.storage.internal.basic.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.data.files.config.ThunderConfig;
import de.zeanon.storage.internal.data.files.raw.YamlFile;
import de.zeanon.storage.internal.utility.setting.Comment;
import de.zeanon.storage.internal.utility.setting.Reload;
import de.zeanon.storage.internal.utility.utils.basic.Objects;
import java.io.FileNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

	@Getter(onMethod = @__({@NotNull}))
	private static YamlFile weConfig;
	@Getter(onMethod = @__({@NotNull}))
	private static ThunderConfig config;

	public static void initPlugin() {
		try {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Loading Configs...");
			loadConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are loaded successfully.");

			initVersion();
		} catch (RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load config files.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
		}
	}

	private static void initVersion() {
		try {
			if (!Objects.notNull(Utils.getConfig()).hasKeyUseArray("Plugin Version")
				|| !Utils.getConfig().getStringUseArray("Plugin Version")
						 .equals(SchemManager.getInstance().getDescription().getVersion())) {
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> Updating Configs...");
				Update.checkConfigUpdate();
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are updated successfully.");
			}

			/*if (pluginManager.getPlugin("FastAsyncWorldEdit") != null && pluginManager.isPluginEnabled("FastAsyncWorldEdit"))) { //NOSONAR
			//TODO //NOSONAR
			} else */
			if (SchemManager.getPluginManager().getPlugin("WorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("WorldEdit")) {
				initWorldEditMode();
			} else {
				enableSleepMode();
			}
		} catch (RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not update config files.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
		}
	}

	private static void loadConfigs() {
		Throwable cause = null;
		try {
			config = StorageManager.thunderConfig(SchemManager.getInstance().getDataFolder(), "config")
								   .fromResource("resources/config.tf")
								   .reloadSetting(Reload.INTELLIGENT)
								   .commentSetting(Comment.PRESERVE)
								   .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' loaded.");
		} catch (@NotNull RuntimeIOException | FileParseException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be loaded.");
			e.printStackTrace();
			cause = e;
		}
		if (cause != null) {
			throw new RuntimeIOException(cause);
		}
	}

	private static void initWorldEditMode() {
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Launching WorldEdit Version of " + SchemManager.getInstance().getName() + "...");

		try {
			weConfig = StorageManager.yamlFile(Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
									 .reloadSetting(Reload.AUTOMATICALLY)
									 .commentSetting(Comment.SKIP)
									 .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded successfully.");
			initWorldEditPlugin();
		} catch (@NotNull RuntimeIOException | FileParseException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
			enableSleepMode();
		}
	}

	private static void initWorldEditPlugin() {
		try {
			WorldEditModeSchemUtils.initSchemPath();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Schematic folder is loaded successfully.");
			WorldEditMode.onEnable();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder.");
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