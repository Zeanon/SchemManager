package de.zeanon.schemmanager.init;

import de.steamwar.commandframework.SWCommand;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.WakeupListener;
import de.zeanon.schemmanager.plugin.schemmanagercommands.SchemmanagerCommand;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.Mapper;
import de.zeanon.storagemanagercore.internal.base.exceptions.RuntimeIOException;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class InitMode {


	private final @NotNull Set<SWCommand> registeredCommands = new HashSet<>();
	@Getter(onMethod_ = {@NotNull})
	private String worldEditPluginName;

	public void initPlugin() {
		InitMode.registerCommands();

		try {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Loading Config...");
			ConfigUtils.loadConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config file are loaded successfully.");
		} catch (final RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not load config file.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the config file and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}

		try {
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Initializing Config...");
			ConfigUtils.initConfigs();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Config file is initialized successfully.");
		} catch (final RuntimeIOException e) {
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Could not update config file.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Maybe try to delete the config file and reload the plugin.");
			System.err.println("[" + SchemManager.getInstance().getName() + "] >> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}

		InitMode.initVersion();
	}

	public void registerCommands() {
		Mapper.initialize();

		InitMode.registeredCommands.add(new SchemmanagerCommand());
	}

	public void unregisterCommands() {
		for (final @NotNull SWCommand command : InitMode.registeredCommands) {
			command.unregister();
		}
	}


	private void initVersion() {
		if (SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
			InitMode.worldEditPluginName = "FastAsyncWorldEdit";
			RunningMode.onEnable();
		} else if (SchemManager.getPluginManager().getPlugin("WorldEdit") != null && SchemManager.getPluginManager().isPluginEnabled("WorldEdit")) {
			InitMode.worldEditPluginName = "WorldEdit";
			RunningMode.onEnable();
		} else {
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