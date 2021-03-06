package de.zeanon.schemmanager;

import de.zeanon.schemmanager.init.InitMode;
import de.zeanon.schemmanager.plugin.handlers.CommandHandler;
import de.zeanon.schemmanager.plugin.handlers.SchemManagerTabCompleter;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SchemManager extends JavaPlugin {

	@Getter
	@Setter(AccessLevel.PRIVATE)
	@SuppressWarnings("CanBeFinal")
	private static SchemManager instance;
	@Getter
	@Setter(AccessLevel.PRIVATE)
	@SuppressWarnings("CanBeFinal")
	private static PluginManager pluginManager;

	@Override
	public void onEnable() {
		SchemManager.setInstance(this);
		SchemManager.setPluginManager(Bukkit.getPluginManager());
		Objects.notNull(this.getCommand("schemmanager")).setExecutor(new CommandHandler());
		Objects.notNull(this.getCommand("schemmanager")).setTabCompleter(new SchemManagerTabCompleter());
		InitMode.initPlugin();
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded.");
	}
}