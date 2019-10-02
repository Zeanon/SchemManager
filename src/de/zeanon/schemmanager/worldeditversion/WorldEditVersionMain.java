package de.zeanon.schemmanager.worldeditversion;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.helper.Helper;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import org.bukkit.Bukkit;

public class WorldEditVersionMain {

    public void onEnable() {
        Helper.initSchemPath();
        Bukkit.getPluginManager().registerEvents(new CommandListener(), SchemManager.getInstance());
        if (!DefaultHelper.updateConfig(false)) {
            DefaultHelper.disable();
        } else {
            System.out.println("[" + SchemManager.getInstance() + "] >> " + SchemManager.getInstance() + " v" + SchemManager.getInstance().getDescription().getVersion() + " successfully launched...");
        }
    }
}