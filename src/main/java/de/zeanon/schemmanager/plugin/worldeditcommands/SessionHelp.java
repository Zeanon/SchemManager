package de.zeanon.schemmanager.plugin.worldeditcommands;


import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class SessionHelp {

    public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash) {
        if (args.length == 2) {
            SessionHelp.executeInternally(p, slash);
        } else {
            p.sendMessage(ChatColor.RED + "Too many arguments.");
            GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                                                  ChatColor.GRAY + slash + "session"
                                                  + ChatColor.AQUA + " help",
                                                  ChatColor.LIGHT_PURPLE + ""
                                                  + ChatColor.UNDERLINE + ""
                                                  + ChatColor.ITALIC + ""
                                                  + ChatColor.BOLD + "OMG PLS HELP ME",
                                                  slash + "session help", p);
        }
    }

    public void executeInternally(final @NotNull Player p, @NotNull final String slash) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ConfigUtils.getBoolean("Space Lists")) {
                    p.sendMessage("");
                }

                p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
                              + SchemManager.getInstance().getDescription().getVersion()
                              + " ===");
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Load a session: ",
                                                      ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " load "
                                                      + ChatColor.YELLOW + "<"
                                                      + ChatColor.GREEN + "sessionname"
                                                      + ChatColor.YELLOW + ">",
                                                      ChatColor.RED + "e.g. "
                                                      + ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " load "
                                                      + ChatColor.GREEN + "example",
                                                      slash + "session load ",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Save a session: ",
                                                      ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " save "
                                                      + ChatColor.YELLOW + "<"
                                                      + ChatColor.GREEN + "sessionname"
                                                      + ChatColor.YELLOW + ">",
                                                      ChatColor.RED + "e.g. "
                                                      + ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " save "
                                                      + ChatColor.GREEN + "example",
                                                      slash + "session save ",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Swap with a session: ",
                                                      ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " swap "
                                                      + ChatColor.YELLOW + "<"
                                                      + ChatColor.GREEN + "sessionname"
                                                      + ChatColor.YELLOW + ">",
                                                      ChatColor.RED + "e.g. "
                                                      + ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " swap "
                                                      + ChatColor.GREEN + "example",
                                                      slash + "session swap ",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Delete a session: ",
                                                      ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " delete "
                                                      + ChatColor.YELLOW + "<"
                                                      + ChatColor.GREEN + "sessionname"
                                                      + ChatColor.YELLOW + ">",
                                                      ChatColor.RED + "e.g. "
                                                      + ChatColor.GRAY + slash + "session"
                                                      + ChatColor.AQUA + " delete "
                                                      + ChatColor.GREEN + "example",
                                                      slash + "session delete ",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "List sessions: ",
                                                      ListSessions.usageMessage(slash),
                                                      ListSessions.usageHoverMessage(slash),
                                                      ListSessions.usageCommand(slash),
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Search for a session: ",
                                                      SearchSession.usageMessage(slash),
                                                      SearchSession.usageHoverMessage(slash),
                                                      SearchSession.usageCommand(slash),
                                                      p);
            }
        }.runTaskAsynchronously(SchemManager.getInstance());
    }
}