package de.zeanon.schemmanager.init;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.WakeupListener;
import de.zeanon.schemmanager.plugin.update.Update;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanager.StorageManager;
import de.zeanon.storagemanager.internal.base.exceptions.FileParseException;
import de.zeanon.storagemanager.internal.base.exceptions.ObjectNullException;
import de.zeanon.storagemanager.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storagemanager.internal.base.settings.Comment;
import de.zeanon.storagemanager.internal.base.settings.Reload;
import de.zeanon.storagemanager.internal.files.config.ThunderConfig;
import de.zeanon.storagemanager.internal.files.raw.YamlFile;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.FileNotFoundException;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class InitMode {

	@Getter(onMethod_ = {@NotNull})
	private YamlFile weConfig;
	@Getter(onMethod_ = {@NotNull})
	private ThunderConfig config;
	@Getter(onMethod_ = {@NotNull})
	private String worldEditPluginName;

	public void initPlugin() {
		try {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Loading Configs...");
			InitMode.loadConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are loaded successfully.");

			InitMode.initConfigs();
		} catch (RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load config files.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
		}
	}

	private void initConfigs() {
		try {
			if (!InitMode.getConfig().hasKeyUseArray("Plugin Version")
				|| !Objects.notNull(InitMode.getConfig().getStringUseArray("Plugin Version"))
						   .equals(SchemManager.getInstance().getDescription().getVersion())) {
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> Updating Configs...");
				Update.checkConfigUpdate();
				System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config files are updated successfully.");
			}
		} catch (RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not update config files.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the Config File and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
		}

		InitMode.initVersion();
	}

	private void initVersion() {
		if (SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
			InitMode.worldEditPluginName = "FastAsyncWorldEdit";
			InitMode.initFastAsyncWorldEditMode();
		} else if (SchemManager.getPluginManager().getPlugin("WorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("WorldEdit")) {
			InitMode.worldEditPluginName = "WorldEdit";
			InitMode.initWorldEditMode();
		} else {
			InitMode.enableSleepMode();
		}
	}

	private void loadConfigs() {
		@Nullable Throwable cause = null;
		try {
			InitMode.config = StorageManager.thunderConfig(SchemManager.getInstance().getDataFolder(), "config")
											.fromResource("resources/config.tf")
											.reloadSetting(Reload.INTELLIGENT)
											.commentSetting(Comment.PRESERVE)
											.concurrentData(true)
											.create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' loaded.");
		} catch (final @NotNull RuntimeIOException | FileParseException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> 'config.tf' could not be loaded.");
			e.printStackTrace();
			cause = e;
		}
		if (cause != null) {
			throw new RuntimeIOException(cause);
		}
	}

	private void initWorldEditMode() {
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Launching WorldEdit Version of " + SchemManager.getInstance().getName() + "...");

		try {
			InitMode.weConfig = StorageManager.yamlFile(Objects.notNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
											  .reloadSetting(Reload.AUTOMATICALLY)
											  .commentSetting(Comment.SKIP)
											  .concurrentData(false)
											  .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded successfully.");
			InitMode.initWorldEditPlugin();
		} catch (final @NotNull RuntimeIOException | FileParseException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
			InitMode.enableSleepMode();
		}
	}

	private void initWorldEditPlugin() {
		try {
			SchemUtils.initSchemPath();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Schematic-Folder is loaded successfully.");
			RunningMode.onEnable();
		} catch (@NotNull FileNotFoundException | ObjectNullException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder.");
			InitMode.enableSleepMode();
		}
	}

	private void initFastAsyncWorldEditMode() {
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Launching FastAsyncWorldEdit Version of " + SchemManager.getInstance().getName() + "...");

		try {
			InitMode.weConfig = StorageManager.yamlFile(Objects.notNull(SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")).getDataFolder(), "config-legacy")
											  .reloadSetting(Reload.AUTOMATICALLY)
											  .commentSetting(Comment.SKIP)
											  .concurrentData(false)
											  .create();

			System.out.println("[" + SchemManager.getInstance().getName() + "] >> FastAsyncWorldEdit Config is loaded successfully.");
			InitMode.initFastAsyncWorldEditPlugin();
		} catch (final @NotNull RuntimeIOException | FileParseException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load FastAsyncWorldEdit Config file.");
			InitMode.enableSleepMode();
		}
	}

	private void initFastAsyncWorldEditPlugin() {
		try {
			SchemUtils.initSchemPath();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> FastAsyncWorldEdit Schematic-Folder is loaded successfully.");
			RunningMode.onEnable();
		} catch (@NotNull FileNotFoundException | ObjectNullException e) {
			e.printStackTrace();
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load FastAsyncWorldEdit Schematic folder.");
			InitMode.enableSleepMode();
		}
	}

	private void enableSleepMode() {
		SchemManager.getPluginManager().registerEvents(new WakeupListener(), SchemManager.getInstance());
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance().getName() + " will automatically activate when one of the above gets enabled.");
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> Rudimentary function like updating and disabling will still work.");
	}
}