package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Objects;

public class WorldEditVersionMain {

    public static Config weConfig;
    public static String weFolderPath;

    public void onEnable() {
        try {
            System.out.println(new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config.yml").getAbsolutePath());
            weConfig = new Config(new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config.yml"), "config");
            weFolderPath = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder().getAbsolutePath();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded sucessfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file");
        }
        if (!DefaultHelper.updateConfig(false)) {
            DefaultHelper.disable();
        } else {
            Helper.initSchemPath();
            Bukkit.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
        }
    }
}