package de.zeanon.schemmanager;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.globalutils.CommandHandler;
import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.globalutils.DefaultTabCompleter;
import de.zeanon.schemmanager.globalutils.WakeupListener;
import de.zeanon.schemmanager.worldeditversion.WorldEditVersionMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SchemManager extends JavaPlugin {

    public static Config config;
    private static volatile SchemManager instance;

    public static SchemManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Couldn't get null");
        }
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        DefaultHelper.initiate();
        PluginManager pm = Bukkit.getPluginManager();
        Objects.requireNonNull(getCommand("schemmanager")).setExecutor(new CommandHandler());
        Objects.requireNonNull(getCommand("schemmanager")).setTabCompleter(new DefaultTabCompleter());
		/*if (pm.getPlugin("FastAsyncWorldEdit") != null && pm.isPluginEnabled("FastAsyncWorldEdit"))) {
		//TODO
		}
		else */
        if (pm.getPlugin("WorldEdit") != null && pm.isPluginEnabled("WorldEdit")) {
            boolean failedToLoad = false;
            System.out.println("[" + getName() + "] >> Launching WorldEdit Version of " + getName());
            System.out.println("[" + getName() + "] >> Loading Configs.");
            try {
                config = new Config("config", getDataFolder().getAbsolutePath(), "config");
                System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " loaded");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[" + getName() + "] >> [Configs] >> " + config.getName() + " could not be loaded");
                failedToLoad = true;
            }
            if (failedToLoad) {
                System.out.println("[" + getName() + "] >> Could not load config files... unloading Plugin...");
                DefaultHelper.disable();
                return;
            } else {
                System.out.println("[" + getName() + "] >> Config files are loaded sucessfully");
            }
            new WorldEditVersionMain().onEnable();
        } else {
            System.out.println("[" + getName() + "] >> could not load plugin, it needs FastAsyncWorldEdit or WorldEdit to work");
            System.out.println("[" + getName() + "] >> It will automatically activate when one of the above gets enabled");
            System.out.println("[" + getName() + "] >> rudimentary function like updating and disabling will still work");
            pm.registerEvents(new WakeupListener(), this);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[" + getName() + "] >> unloaded");
    }
}