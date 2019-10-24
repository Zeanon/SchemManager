package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.LightningStorage;
import de.leonhard.storage.lightningstorage.internal.base.enums.ConfigSetting;
import de.leonhard.storage.lightningstorage.internal.datafiles.config.YamlConfig;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import de.zeanon.schemmanager.worldeditversion.listener.tabcompleter.PaperTabListener;
import de.zeanon.schemmanager.worldeditversion.listener.tabcompleter.SpigotTabListener;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;
import java.io.FileNotFoundException;
import java.util.Objects;
import org.bukkit.Bukkit;


public class WorldEditVersionMain {

	public static YamlConfig weConfig;

	public static void onEnable() {
		try {
			weConfig = LightningStorage.create(Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config")
									   .configSetting(ConfigSetting.SKIP_COMMENTS)
									   .asYamlConfig();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded sucessfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file.");
			SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
			return;
		}
		try {
			WorldEditVersionSchemUtils.initWorldEditPlugin();
			WorldEditVersionSchemUtils.initSchemPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder.");
		}
		SchemManager.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
		SchemManager.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
		if (Bukkit.getVersion().contains("git-Paper")) {
			SchemManager.getPluginManager().registerEvents(new PaperTabListener(), SchemManager.getInstance());
		} else {
			SchemManager.getPluginManager().registerEvents(new SpigotTabListener(), SchemManager.getInstance());
		}
		System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
	}
}