package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class WakeupListener implements Listener {

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (e.getPlugin() == Bukkit.getPluginManager().getPlugin("WorldEdit") || e.getPlugin() == Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
            DefaultHelper.disable();
            Bukkit.getPluginManager().enablePlugin(SchemManager.getInstance());
        }
    }
}