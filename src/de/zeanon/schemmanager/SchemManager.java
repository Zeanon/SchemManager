package de.zeanon.schemmanager;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.globalutils.*;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SchemManager extends JavaPlugin {

    public static Config config;
    private static volatile SchemManager instance;
    private static PluginManager pluginManager;

    public static SchemManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Couldn't get null");
        } else {
            return instance;
        }
    }

    public static PluginManager getPluginManager() {
        if (pluginManager == null) {
            throw new IllegalStateException("Couldn't get null");
        } else {
            return pluginManager;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        pluginManager = Bukkit.getPluginManager();
        InternalFileUtils.initiate();
        Objects.requireNonNull(getCommand("schemmanager")).setExecutor(new CommandHandler());
        Objects.requireNonNull(getCommand("schemmanager")).setTabCompleter(new TabCompleter());
		/*if (pluginManager.getPlugin("FastAsyncWorldEdit") != null && pluginManager.isPluginEnabled("FastAsyncWorldEdit"))) {
		//TODO
		}
		else */
        if (pluginManager.getPlugin("WorldEdit") != null && pluginManager.isPluginEnabled("WorldEdit")) {
            boolean failedToLoad = false;
            System.out.println("[" + getName() + "] >> Launching WorldEdit Version of " + getName() + ".");
            System.out.println("[" + getName() + "] >> Loading Configs.");
            try {
                config = new Config("config", getDataFolder().getAbsolutePath(), "resources/config");
                System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " loaded.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " could not be loaded");
                failedToLoad = true;
            }
            if (failedToLoad) {
                System.out.println("[" + getName() + "] >> Could not load config files... unloading Plugin...");
                SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
            } else {
                if (!Update.updateConfig(false)) {
                    SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
                } else {
                    System.out.println("[" + getName() + "] >> Config files are loaded sucessfully.");
                    new WorldEditVersionMain().onEnable();
                }
            }
        } else {
            pluginManager.registerEvents(new WakeupListener(), this);
            System.out.println("[" + getName() + "] >> Could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work.");
            System.out.println("[" + getName() + "] >> " + getName() + " will automatically activate when one of the above gets enabled.");
            System.out.println("[" + getName() + "] >> Rudimentary function like updating and disabling will still work.");
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[" + getName() + "] >> unloaded.");
    }
}