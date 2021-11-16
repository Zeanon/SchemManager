package de.zeanon.schemmanager;

import de.zeanon.schemmanager.init.InitMode;
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
		InitMode.initPlugin();
	}

	@Override
	public void onDisable() {
		InitMode.unregisterCommands();
		System.out.println("[" + this.getName() + "] >> unloaded.");
	}
}