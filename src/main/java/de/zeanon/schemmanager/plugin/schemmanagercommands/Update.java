package de.zeanon.schemmanager.plugin.schemmanagercommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.init.RunningMode;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalRequestUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Update {

    public void execute(final @NotNull Player p, final @Nullable CommandConfirmation confirmation) {
        if (p.hasPermission("schemmanager.update")) {
            if (confirmation == null) {
                if (!de.zeanon.schemmanager.plugin.update.Update.checkForUpdate()) {
                    p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                  + ChatColor.RED + "You are already running the latest Version.");
                }
                GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                                      + ChatColor.RED + "Do you really want to update?"
                        , "/sm update -confirm"
                        , "/sm update -deny"
                        , p);
                GlobalRequestUtils.addUpdateRequest(p.getUniqueId());
            } else {
                if (GlobalRequestUtils.checkUpdateRequest(p.getUniqueId())) {
                    GlobalRequestUtils.removeUpdateRequest(p.getUniqueId());
                    if (confirmation == CommandConfirmation.CONFIRM) {
                        if (RunningMode.isPaperSpigot()) {
                            de.zeanon.schemmanager.plugin.update.Update.updatePlugin(p, SchemManager.getInstance());
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    de.zeanon.schemmanager.plugin.update.Update.updatePlugin(p, SchemManager.getInstance());
                                }
                            }.runTask(SchemManager.getInstance());
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                      + ChatColor.RED + "Plugin will not be updated.");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                  + ChatColor.RED + "You don't have a pending update request.");
                }
            }
        }
    }

    public void usage(final @NotNull Player p, final @NotNull String... args) {
        if (args.length > 0) {
            p.sendMessage(ChatColor.RED + "Too many arguments.");
        }

        GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                                              ChatColor.GRAY + "/schemmanager"
                                              + ChatColor.AQUA + " update",
                                              ChatColor.DARK_GREEN + ""
                                              + ChatColor.UNDERLINE + ""
                                              + ChatColor.ITALIC + ""
                                              + ChatColor.BOLD + "!!UPDATE BABY!!",
                                              "/schemmanager update", p);
    }
}
