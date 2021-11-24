package de.zeanon.schemmanager.plugin.schemmanagercommands;


import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.worldeditcommands.SchemHelp;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


@UtilityClass
public class Help {

    public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
        if (args.length == 2) {
            SchemHelp.executeInternally(p, slash, schemAlias);
        } else {
            p.sendMessage(ChatColor.RED + "Too many arguments.");
            GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                                                  ChatColor.GRAY + slash + schemAlias
                                                  + ChatColor.AQUA + " help",
                                                  ChatColor.LIGHT_PURPLE + ""
                                                  + ChatColor.UNDERLINE + ""
                                                  + ChatColor.ITALIC + ""
                                                  + ChatColor.BOLD + "OMG PLS HELP ME",
                                                  "/sm help",
                                                  p);
        }
    }

    public void executeInternally(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ConfigUtils.getBoolean("Space Lists")) {
                    p.sendMessage("");
                }

                p.sendMessage(ChatColor.AQUA + "=== SchemManager | Version "
                              + SchemManager.getInstance().getDescription().getVersion()
                              + " ===");
                GlobalMessageUtils.sendCommandMessage(ChatColor.RED + "Usage for schem: ",
                                                      ChatColor.GRAY + slash + schemAlias + " "
                                                      + ChatColor.AQUA + "help",
                                                      ChatColor.LIGHT_PURPLE + ""
                                                      + ChatColor.UNDERLINE + ""
                                                      + ChatColor.ITALIC + ""
                                                      + ChatColor.BOLD + "OMG PLS HELP ME",
                                                      slash + schemAlias + " help",
                                                      p);
                GlobalMessageUtils.sendCommandMessage(ChatColor.RED + "Usage for session: ",
                                                      ChatColor.GRAY + slash + "session "
                                                      + ChatColor.AQUA + "help",
                                                      ChatColor.LIGHT_PURPLE + ""
                                                      + ChatColor.UNDERLINE + ""
                                                      + ChatColor.ITALIC + ""
                                                      + ChatColor.BOLD + "OMG PLS HELP ME",
                                                      slash + "session help",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Update the plugin: ",
                                                      ChatColor.GRAY + "/schemmanager "
                                                      + ChatColor.AQUA + "update",
                                                      ChatColor.DARK_GREEN + ""
                                                      + ChatColor.UNDERLINE + ""
                                                      + ChatColor.ITALIC + ""
                                                      + ChatColor.BOLD + "!!UPDATE BABY!!",
                                                      "/schemmanager update",
                                                      p);
                GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Disable the plugin: ",
                                                      ChatColor.GRAY + "/schemmanager "
                                                      + ChatColor.AQUA + "disable",
                                                      ChatColor.DARK_RED + ""
                                                      + ChatColor.UNDERLINE + ""
                                                      + ChatColor.ITALIC + ""
                                                      + ChatColor.BOLD + "PLS DON'T D;",
                                                      "/schemmanager disable",
                                                      p);
            }
        }.runTaskAsynchronously(SchemManager.getInstance());
    }
}