package de.zeanon.schemmanager.worldeditversion;

import de.zeanon.schemmanager.globalutils.DefaultHelper;
import de.zeanon.schemmanager.worldeditversion.listener.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WorldEditVersionMain {

    public static Plugin plugin;


    public WorldEditVersionMain(Plugin plugin) {
        WorldEditVersionMain.plugin = plugin;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new CommandListener(plugin), plugin);

        if (!DefaultHelper.updateConfig(false)) {
            DefaultHelper.disable();
        } else {
            System.out.println("[" + plugin.getName() + "] >> " + plugin.getName() + " v" + plugin.getDescription().getVersion() + " successfully launched...");
        }
    }
}