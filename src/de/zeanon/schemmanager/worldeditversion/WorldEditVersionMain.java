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
        Helper.initSchemPath();
        Bukkit.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
        try {
            weConfig = new Config(new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEditr")).getDataFolder(), "config.yml"), "config");
            weFolderPath = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEditr")).getDataFolder().getAbsolutePath();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> Loading WorldEdit Config");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> [Configs] >> Could not load WorldEdit Config file");
        }
        if (!DefaultHelper.updateConfig(false)) {
            DefaultHelper.disable();
        } else {
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
        }
    }
}