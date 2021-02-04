package de.zeanon.schemmanager.plugin.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.GlobalMessageUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Download {

	public void execute(final @NotNull String @NotNull [] args, final @NotNull Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length == 4) {

					final @Nullable Path schemPath = SchemUtils.getSchemPath();
					final @Nullable File file = schemPath != null
												? (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(args[2])) //NOSONAR
												   ? SchemUtils.getSchemPath().resolve(args[2]).toFile()
												   : SchemUtils.getSchemPath().resolve(args[2] + ".schem").toFile())
												: null;

					if (file != null && file.exists() && !file.isDirectory()) {
						CommandRequestUtils.addOverwriteRequest(p.getUniqueId(), args[2]);
						p.sendMessage(ChatColor.RED + "The schematic " + ChatColor.GOLD + args[2] + ChatColor.RED + " already exists.");
						GlobalMessageUtils.sendBooleanMessage(ChatColor.RED + "Do you want to overwrite " + ChatColor.GOLD + args[2] + ChatColor.RED + "?",
															  "//schem download " + args[2] + " confirm",
															  "//schem download " + args[2] + " deny", p);
					}
				} else if (args.length <= 5) {
					if (args.length < 4) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GREEN + "filename"
									  + ChatColor.YELLOW + ">");
						//TODO USAGE
					} else if (!args[4].equalsIgnoreCase("confirm") && !args[4].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
					} else {
						if (args[3].equalsIgnoreCase("confirm") && CommandRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
							CommandRequestUtils.removeOverWriteRequest(p.getUniqueId());
							final @Nullable Path schemPath = SchemUtils.getSchemPath();
							final @Nullable File file = schemPath != null
														? (Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(args[2])) //NOSONAR
														   ? SchemUtils.getSchemPath().resolve(args[2]).toFile()
														   : SchemUtils.getSchemPath().resolve(args[2] + ".schem").toFile())
														: null;
							if (file != null) {
								BaseFileUtils.createFile(file);
								try {
									BaseFileUtils.writeToFile(file, new BufferedInputStream(
											new URL(args[2])
													.openStream()));
								} catch (IOException e) {
									e.printStackTrace();
									//TODO HANDLING
								}
							}
						} else if (args[3].equalsIgnoreCase("deny") && CommandRequestUtils.checkOverWriteRequest(p.getUniqueId(), args[2])) {
							CommandRequestUtils.removeOverWriteRequest(p.getUniqueId());
							p.sendMessage(ChatColor.LIGHT_PURPLE + args[3] + " was not overwritten.");
						}
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}
}
