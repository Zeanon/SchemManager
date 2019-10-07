package de.zeanon.schemmanager.worldeditversion;

import de.leonhard.storage.Config;
import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.globalutils.DefaultUtils;
import de.zeanon.schemmanager.worldeditversion.listener.EventListener;
import de.zeanon.schemmanager.worldeditversion.utils.Helper;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Objects;

public class WorldEditVersionMain {

    public static Config weConfig;

    public void onEnable() {
        try {
            weConfig = new Config(new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("WorldEdit")).getDataFolder(), "config.yml"), "config");
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> WorldEdit Config is loaded sucessfully");
            Helper.initSchemPath();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> Could not load WorldEdit Config file");
        }
        if (!DefaultUtils.updateConfig(false)) {
            DefaultUtils.disable();
        } else {
            Bukkit.getPluginManager().registerEvents(new EventListener(), SchemManager.getInstance());
            System.out.println("[" + SchemManager.getInstance().getName() + "] >> " + SchemManager.getInstance() + " launched successfully...");
        }
    }
}