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
public class Disable {

    public void execute(final @NotNull Player p, final @Nullable CommandConfirmation confirmation) {
        if (p.hasPermission("schemmanager.disable")) {
            if (confirmation == null) {
                GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                                      + ChatColor.RED + "Do you really want to disable "
                                                      + ChatColor.DARK_RED + SchemManager.getInstance().getName()
                                                      + ChatColor.RED + "? "
                        , "/sm disable -confirm"
                        , "/sm disable -deny"
                        , p);
                GlobalRequestUtils.addDisableRequest(p.getUniqueId());
            } else {
                if (GlobalRequestUtils.checkDisableRequest(p.getUniqueId())) {
                    GlobalRequestUtils.removeDisableRequest(p.getUniqueId());
                    if (confirmation == CommandConfirmation.CONFIRM) {
                        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                      + ChatColor.RED + "is being disabled.");
                        if (RunningMode.isPaperSpigot()) {
                            SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    SchemManager.getPluginManager().disablePlugin(SchemManager.getInstance());
                                }
                            }.runTask(SchemManager.getInstance());
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                      + ChatColor.RED + "will not be disabled.");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] "
                                  + ChatColor.RED + "You don't have a pending disable request.");
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
                                              + ChatColor.AQUA + " disable",
                                              ChatColor.DARK_RED + ""
                                              + ChatColor.UNDERLINE + ""
                                              + ChatColor.ITALIC + ""
                                              + ChatColor.BOLD + "PLS DON'T D;",
                                              "/schemmanager disable", p);
    }
}
