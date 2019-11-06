package de.zeanon.schemmanager;

import de.leonhard.storage.internal.utils.basic.Objects;
import de.zeanon.schemmanager.global.handlers.CommandHandler;
import de.zeanon.schemmanager.global.handlers.TabCompleter;
import de.zeanon.schemmanager.global.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SchemManager extends JavaPlugin {

	@SuppressWarnings("CanBeFinal")
	private static SchemManager instance;
	@SuppressWarnings("CanBeFinal")
	private static PluginManager pluginManager;

	{
		instance = this;
		pluginManager = Bukkit.getPluginManager();
	}

	public static SchemManager getInstance() {
		return instance;
	}

	public static PluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public void onEnable() {
		Objects.notNull(this.getCommand("schemmanager")).setExecutor(new CommandHandler());
		Objects.notNull(this.getCommand("schemmanager")).setTabCompleter(new TabCompleter());
		Utils.initPlugin();
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded.");
	}
}