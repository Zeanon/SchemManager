package de.zeanon.schemmanager.worldeditversion.listener;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.utils.RequestUtils;
import de.zeanon.schemmanager.worldeditversion.utils.WorldEditVersionRequestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class EventListener implements Listener {

    static boolean worldguardEnabled = false;

    public EventListener() {
        if (SchemManager.getPluginManager().getPlugin("WorldGuard") != null && SchemManager.getPluginManager().isPluginEnabled("WorldGuard")) {
            worldguardEnabled = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTab(final TabCompleteEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String message = event.getBuffer();
                while (message.contains("  ")) {
                    message = message.replaceAll(" {2}", " ");
                }
                String[] args = message.replaceAll("worldedit:", "/").split(" ");
                if (args[0].equalsIgnoreCase("//schem") || args[0].equalsIgnoreCase("//schematic")) {
                    if (message.contains("./")) {
                        event.setCompletions(new ArrayList<>());
                    } else {
                        boolean deep = false;
                        if (args.length > 2 && args[2].equalsIgnoreCase("-deep")) {
                            args = (String[]) ArrayUtils.removeElement(args, "-deep");
                            deep = true;
                        }
                        if (args.length > 2 && args[2].equalsIgnoreCase("-d")) {
                            args = (String[]) ArrayUtils.removeElement(args, "-d");
                            deep = true;
                        }
                        event.setCompletions(WorldEditVersionTabCompleter.onTab(args, event.getBuffer(), deep, message.endsWith(" ")));
                    }
                } else if (args[0].equalsIgnoreCase("/stoplag")) {
                    if (args.length == 1 || (args.length == 2 && !message.endsWith(" "))) {
                        event.setCompletions(Collections.singletonList("-c"));
                    } else {
                        event.setCompletions(new ArrayList<>());
                    }
                }
            }
        }.runTaskAsynchronously(SchemManager.getInstance());
    }


    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        Player p = event.getPlayer();
        RequestUtils.removeDisableRequest(p);
        RequestUtils.removeUpdateRequest(p);
        WorldEditVersionRequestUtils.removeDeleteRequest(p);
        WorldEditVersionRequestUtils.removeDeleteFolderRequest(p);
        WorldEditVersionRequestUtils.removeRenameRequest(p);
        WorldEditVersionRequestUtils.removeRenameFolderRequest(p);
        WorldEditVersionRequestUtils.removeOverWriteRequest(p);
    }


    @EventHandler
    public void onPluginDisable(final PluginDisableEvent event) {
        if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldEdit")) {
            SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
            SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
        } else if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldGuard")) {
            worldguardEnabled = false;
        }
    }

    @EventHandler
    public void onPluginEnable(final PluginEnableEvent event) {
        if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("FastAsyncWorldEdit")) {
            SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
            SchemManager.getPluginManager().enablePlugin(SchemManager.getInstance());
        } else if (event.getPlugin() == SchemManager.getPluginManager().getPlugin("WorldGuard")) {
            worldguardEnabled = true;
        }
    }
}