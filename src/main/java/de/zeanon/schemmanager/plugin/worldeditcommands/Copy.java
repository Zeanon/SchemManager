package de.zeanon.schemmanager.plugin.worldeditcommands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.plugin.utils.ConfigUtils;
import de.zeanon.schemmanager.plugin.utils.InternalFileUtils;
import de.zeanon.schemmanager.plugin.utils.SchemUtils;
import de.zeanon.schemmanager.plugin.utils.commands.CommandRequestUtils;
import de.zeanon.schemmanager.plugin.utils.commands.GlobalMessageUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Copy {

	public void execute(final @NotNull String[] args, final @NotNull Player p, final @NotNull String slash, final @NotNull String schemAlias) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (args.length <= 5) {
					if (args.length < 3) {
						p.sendMessage(ChatColor.RED + "Missing argument for "
									  + ChatColor.YELLOW + "<"
									  + ChatColor.GOLD + "filename"
									  + ChatColor.YELLOW + ">");
						Copy.usage(p, slash, schemAlias);
					} else if ((args[2].contains("./") || args[2].contains(".\\")) || args.length >= 4 && (args[3].contains("./") || args[3].contains(".\\"))) {
						final String name = args[2].contains("./") || args[2].contains(".\\") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "' resolution error: Path is not allowed.");
						Copy.usage(p, slash, schemAlias);
					} else if (args.length == 5
							   && !args[4].equalsIgnoreCase("-confirm")
							   && !args[4].equalsIgnoreCase("-deny")
							   && !CommandRequestUtils.checkCopyRequest(p.getUniqueId(), args[2])) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Copy.usage(p, slash, schemAlias);
					} else {
						Copy.executeInternally(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Copy.usage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	public @NotNull String usageMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " copy "
			   + ChatColor.YELLOW + "<"
			   + ChatColor.GOLD + "filename"
			   + ChatColor.YELLOW + "> <"
			   + ChatColor.GOLD + "newname"
			   + ChatColor.YELLOW + ">";
	}

	public @NotNull String usageHoverMessage(final @NotNull String slash, final @NotNull String schemAlias) {
		return ChatColor.RED + "e.g. "
			   + ChatColor.GRAY + slash + schemAlias
			   + ChatColor.AQUA + " copy "
			   + ChatColor.GOLD + "example newname";
	}

	public @NotNull String usageCommand(final @NotNull String slash, final @NotNull String schemAlias) {
		return slash + schemAlias + " copy ";
	}

	private void executeInternally(final @NotNull Player p, final @NotNull String[] args) {
		final @Nullable Path schemPath = SchemUtils.getSchemPath();
		final @Nullable List<File> oldFiles = InternalFileUtils.getExistingFiles(schemPath.resolve(args[2]));
		final @Nullable List<File> newFiles = InternalFileUtils.getExistingFiles(schemPath.resolve(args[3]));

		if (args.length == 4) {
			if (!oldFiles.isEmpty()) {
				if (!newFiles.isEmpty()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
				}

				GlobalMessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
													  ChatColor.RED + "Do you really want to copy " +
													  ChatColor.GOLD + args[2] +
													  ChatColor.RED + " to " +
													  ChatColor.GOLD + args[3] +
													  ChatColor.RED + "?",
													  "//schem copy " + args[2] + " " + args[3] + " -confirm",
													  "//schem copy " + args[2] + " " + args[3] + " -deny", p);
				CommandRequestUtils.addCopyRequest(p.getUniqueId(), args[2]);
			} else {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
			}
		} else if (args.length == 5 && CommandRequestUtils.checkCopyRequest(p.getUniqueId(), args[2])) {
			if (args[4].equalsIgnoreCase("-confirm")) {
				CommandRequestUtils.removeCopyRequest(p.getUniqueId());
				if (!oldFiles.isEmpty()) {
					Copy.copyFile(p, args[2], oldFiles, newFiles, schemPath.resolve(args[3]));
				} else {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				}
			} else if (args[4].equalsIgnoreCase("-deny")) {
				CommandRequestUtils.removeCopyRequest(p.getUniqueId());
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " was not copied.");
			}
		}
	}

	private void copyFile(final @NotNull Player p, final String fileName, final @NotNull List<File> oldFiles, final @Nullable List<File> newFiles, final @NotNull Path destPath) {
		try {
			if (newFiles != null) {
				for (final @NotNull File file : newFiles) {
					Files.delete(file.toPath());
				}
			}
			for (final @NotNull File file : oldFiles) {
				if (!Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(destPath))) {
					FileUtils.copyFile(file, new File(destPath + "." + BaseFileUtils.getExtension(file.getName())));
				} else {
					FileUtils.copyFile(file, destPath.toFile());
				}
			}
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GOLD + fileName + ChatColor.RED + " was copied successfully.");
		} catch (final IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GOLD + fileName + ChatColor.RED + " could not be copied, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void usage(final @NotNull Player p, final @NotNull String slash, @NotNull final String schemAlias) {
		GlobalMessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
											  Copy.usageMessage(slash, schemAlias),
											  Copy.usageHoverMessage(slash, schemAlias),
											  Copy.usageCommand(slash, schemAlias), p);
	}
}