package de.zeanon.schemmanager.init;

import de.steamwar.commandframework.SWCommand;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.handlers.WakeupListener;
import de.zeanon.schemmanager.plugin.schemmanagercommands.SchemmanagerCommand;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.Mapper;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;


@UtilityClass
public class InitMode {


	private final @NotNull Set<SWCommand> registeredCommands = new HashSet<>();
	@Getter(onMethod_ = {@NotNull})
	private String worldEditPluginName;

	public void initPlugin() {
		InitMode.registerCommands();

		try {
			SchemManager.getChatLogger().info(">> Loading Config...");
			ConfigUtils.loadConfigs();
			SchemManager.getChatLogger().info(">> Config file are loaded successfully.");
		} catch (final UncheckedIOException e) {
			SchemManager.getChatLogger().info(">> Could not load config file.");
			SchemManager.getChatLogger().info(">> Maybe try to delete the config file and reload the plugin.");
			SchemManager.getChatLogger().info(">> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}

		try {
			SchemManager.getChatLogger().info(">> Initializing default Configs...");
			ConfigUtils.initDefaultConfigs();
			SchemManager.getChatLogger().info(">> Default config is initialized successfully.");
		} catch (final UncheckedIOException e) {
			SchemManager.getChatLogger().info(">> Could not initialize default config.");
			SchemManager.getChatLogger().info(">> Maybe try to reload the plugin.");
			SchemManager.getChatLogger().info(">> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}

		try {
			SchemManager.getChatLogger().info(">> Initializing Config...");
			ConfigUtils.initConfigs();
			SchemManager.getChatLogger().info(">> Config file is initialized successfully.");
		} catch (final UncheckedIOException e) {
			SchemManager.getChatLogger().info(">> Could not update config file.");
			SchemManager.getChatLogger().info(">> Maybe try to delete the config file and reload the plugin.");
			SchemManager.getChatLogger().info(">> Unloading Plugin...");

			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}

		InitMode.initVersion();
	}

	public void registerCommands() {
		Bukkit.getScheduler().runTask(SchemManager.getInstance(), () -> {
			Mapper.initialize();

			InitMode.registeredCommands.add(new SchemmanagerCommand());
		});
	}

	public void unregisterCommands() {
		for (final @NotNull SWCommand command : InitMode.registeredCommands) {
			command.unregister();
		}
	}

	public void registerEvents(final @NotNull Listener listener) {
		SchemManager.getPluginManager().registerEvents(listener, SchemManager.getInstance());
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
		InitMode.registerEvents(new WakeupListener());
		SchemManager.getChatLogger().info(">> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
		SchemManager.getChatLogger().info(">> " + SchemManager.getInstance().getName() + " will automatically activate when one of the above gets enabled.");
		SchemManager.getChatLogger().info(">> Rudimentary function like updating and disabling will still work.");
	}
}