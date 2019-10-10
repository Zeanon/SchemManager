package de.zeanon.schemmanager.globalutils;

import de.zeanon.schemmanager.SchemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class WakeupListener implements Listener {

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldEdit") || event.getPlugin() == SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
            SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
            SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
        }
    }
}