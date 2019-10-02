package de.zeanon.schemmanager.globallistener;

import de.zeanon.schemmanager.helper.Helper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class WakeupListener implements Listener {

    private Plugin plugin;

    public WakeupListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (e.getPlugin() == Bukkit.getPluginManager().getPlugin("WorldEdit") || e.getPlugin() == Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
            Helper.disable(this.plugin);
            Bukkit.getPluginManager().enablePlugin(this.plugin);
        }
    }
}