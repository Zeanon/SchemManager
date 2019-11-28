package de.zeanon.schemmanager;

import de.zeanon.schemmanager.global.handlers.CommandHandler;
import de.zeanon.schemmanager.global.handlers.InternalTabCompleter;
import de.zeanon.schemmanager.global.utils.Utils;
import de.zeanon.storage.internal.utility.basic.Objects;
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
		setInstance(this);
		setPluginManager(Bukkit.getPluginManager());
		Objects.notNull(this.getCommand("schemmanager")).setExecutor(new CommandHandler());
		Objects.notNull(this.getCommand("schemmanager")).setTabCompleter(new InternalTabCompleter());
		Utils.initPlugin();
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.getName() + "] >> unloaded.");
	}
}