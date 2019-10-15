package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionSchemUtils;

import java.io.FileNotFoundException;
import java.util.Objects;

public class WorldEditVersionMain {

    public static Config weConfig;

    public static void onEnable() {
        try {
            weConfig = new Config(Objects.requireNonNull(SchemManager.getPluginManager().getPlugin("WorldEdit")).getDataFolder().getAbsolutePath(), "config.yml");
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
        System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
    }
}