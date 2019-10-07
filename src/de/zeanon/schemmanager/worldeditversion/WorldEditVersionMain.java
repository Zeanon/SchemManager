package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.globalutils.RequestUtils;
import de.zeanon.schemmanager.globalutils.Update;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class WorldEditVersionMain {

    public static Config weConfig;

    public void onEnable() {
        try {
            weConfig = new Config(new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config.yml"));
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded sucessfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file");
        }
        if (!Update.updateConfig(false)) {
            RequestUtils.disable();
        } else {
            try {
                Helper.initSchemPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Schematic folder");
            }
            Bukkit.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
            Bukkit.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
        }
    }
}