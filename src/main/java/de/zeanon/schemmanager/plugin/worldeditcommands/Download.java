package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Download {

    public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (args.length <= 5) {
                    if (args.length < 3) {
                        p.sendMessage(ChatColor.RED + "Missing argument for "
                                      + ChatColor.YELLOW + "<"
                                      + ChatColor.GREEN + "downloadlink"
                                      + ChatColor.YELLOW + ">"
                                      + ChatColor.RED + " and "
                                      + ChatColor.YELLOW + "<"
                                      + ChatColor.GOLD + "name"
                                      + ChatColor.YELLOW + ">");
                        Download.usage(p, slash, schemAlias);
                    } else if (args.length < 4) {
                        p.sendMessage(ChatColor.RED + "Missing argument for "
                                      + ChatColor.YELLOW + "<"
                                      + ChatColor.GOLD + "name"
                                      + ChatColor.YELLOW + ">");
                        Download.usage(p, slash, schemAlias);
                    } else if (args[3].contains("./") || args[2].contains(".\\")) {
                        p.sendMessage(ChatColor.RED + "File '" + args[3] + "' resolution error: Path is not allowed.");
                        Download.usage(p, slash, schemAlias);
                    } else if (args.length == 5 && !CommandRequestUtils.checkDownloadRequest(p.getUniqueId(), args[2])
                               && !args[4].equalsIgnoreCase("-confirm")
                               && !args[4].equalsIgnoreCase("-deny")) {
                        p.sendMessage(ChatColor.RED + "Too many arguments.");
                        Download.usage(p, slash, schemAlias);
                    } else {
                        Download.executeInternally(p, args);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Too many arguments.");
                    Download.usage(p, slash, schemAlias);
                }
            }
        }.runTaskAsynchronously(SchemManager.getInstance());
    }

    public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
        return ChatColor.GRAY + slash + schemAlias
               + ChatColor.AQUA + " download "
               + ChatColor.YELLOW + "<"
               + ChatColor.DARK_PURPLE + "downloadlink"
               + ChatColor.YELLOW + "> <"
               + ChatColor.GOLD + "filename"
               + ChatColor.YELLOW + ">";
    }

    public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
        return ChatColor.RED + "e.g. "
               + ChatColor.GRAY + slash + schemAlias
               + ChatColor.AQUA + " download "
               + ChatColor.DARK_PURPLE + "link"
               + ChatColor.GOLD + " name";
    }

    public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
        return slash + schemAlias + " download ";
    }

    private void executeInternally(final @NotNull Player p, final @NotNull String[] args) {
        //NOSONAR
        final @Nullable File file = Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(args[3])) //NOSONAR
                                    ? SchemUtils.getSchemPath().resolve(args[3]).toFile()
                                    : SchemUtils.getSchemPath().resolve(args[3] + "." + Objects.notNull(ConfigUtils.getStringList("File Extensions")).get(0)).toFile();

        if (args.length == 4) {
            CommandRequestUtils.addDownloadRequest(p.getUniqueId(), args[3]);
            if (file.exists()) {
                p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[3] + ChatColor.RED + " already exists.");
                GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[3] + ChatColor.RED + "?",
                                                      "//schem download " + args[2] + " " + args[3] + " -confirm",
                                                      "//schem download " + args[2] + " " + args[3] + " -deny", p);
            } else {
                GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "Do you really want to download " + ChatColor.GOLD + args[3] + ChatColor.RED + "?",
                                                      "//schem download " + args[2] + " " + args[3] + " -confirm",
                                                      "//schem download " + args[2] + " " + args[3] + " -deny", p);
            }
        } else if (args.length == 5 && CommandRequestUtils.checkDownloadRequest(p.getUniqueId(), args[3])) {
            if (args[4].equalsIgnoreCase("-confirm")) {
                CommandRequestUtils.removeDownloadRequest(p.getUniqueId());
                try {
                    BaseFileUtils.writeToFile(file, new BufferedInputStream(
                            new URL(args[2])
                                    .openStream()));
                    p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
                                  ChatColor.GOLD + args[3] + ChatColor.RED + " was downloaded successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
                                  ChatColor.GOLD + args[3] + ChatColor.RED + " could not be downloaded, for further information please see [console].");
                }
            } else if (args[4].equalsIgnoreCase("-deny")) {
                CommandRequestUtils.removeDownloadRequest(p.getUniqueId());
                p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + " was not downloaded.");
            }
        }
    }

    private void usage(final @NotNull Player p, @NotNull final String slash, @NotNull final String schemAlias) {
        GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
                                              Download.usageMessage(slash, schemAlias),
                                              Download.usageHoverMessage(slash, schemAlias),
                                              Download.usageCommand(slash, schemAlias), p);
    }
}
