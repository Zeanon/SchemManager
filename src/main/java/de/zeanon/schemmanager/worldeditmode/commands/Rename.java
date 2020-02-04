package de.zeanon.schemmanager.worldeditmode.commands;

import de.zeanon.schemmanager.SchemManager;
import de.zeanon.schemmanager.global.utils.ConfigUtils;
import de.zeanon.schemmanager.global.utils.InternalFileUtils;
import de.zeanon.schemmanager.global.utils.MessageUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeRequestUtils;
import de.zeanon.schemmanager.worldeditmode.utils.WorldEditModeSchemUtils;
import de.zeanon.storagemanager.internal.utility.basic.BaseFileUtils;
import de.zeanon.storagemanager.internal.utility.basic.Objects;
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
public class Rename {

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
						Rename.renameUsage(p, slash, schemAlias);
					} else if (args[2].contains("./") || args.length >= 4 && args[3].contains("./")) {
						String name = args[2].contains("./") ? args[2] : args[3];
						p.sendMessage(ChatColor.RED + "File '" + name + "'resolution error: Path is not allowed.");
						Rename.renameUsage(p, slash, schemAlias);
					} else if (args.length == 5 && !WorldEditModeRequestUtils.checkRenameRequest(p.getUniqueId(), args[2])
							   && !args[3].equalsIgnoreCase("confirm")
							   && !args[3].equalsIgnoreCase("deny")) {
						p.sendMessage(ChatColor.RED + "Too many arguments.");
						Rename.renameUsage(p, slash, schemAlias);
					} else {
						Rename.onRename(p, args);
					}
				} else {
					p.sendMessage(ChatColor.RED + "Too many arguments.");
					Rename.renameUsage(p, slash, schemAlias);
				}
			}
		}.runTaskAsynchronously(SchemManager.getInstance());
	}

	private void onRename(final @NotNull Player p, final @NotNull String[] args) {
		final @Nullable Path schemPath = WorldEditModeSchemUtils.getSchemPath();
		final @Nullable List<File> oldFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[2])) : null;
		final @Nullable List<File> newFiles = schemPath != null ? InternalFileUtils.getExistingFiles(schemPath.resolve(args[3])) : null;

		if (args.length == 4) {
			if (schemPath != null && !oldFiles.isEmpty()) {
				if (!newFiles.isEmpty()) {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GOLD + args[3] + ChatColor.RED + " already exists, the file will be overwritten.");
				}

				MessageUtils.sendBooleanMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
												ChatColor.RED + "Do you really want to rename " + ChatColor.GOLD + args[2] + ChatColor.RED + "?",
												"//schem rename " + args[2] + " " + args[3] + " confirm",
												"//schem rename " + args[2] + " " + args[3] + " deny", p);
				WorldEditModeRequestUtils.addRenameRequest(p.getUniqueId(), args[2]);
			} else {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
			}
		} else if (args.length == 5 && WorldEditModeRequestUtils.checkRenameRequest(p.getUniqueId(), args[2])) {
			if (args[4].equalsIgnoreCase("confirm")) {
				WorldEditModeRequestUtils.removeRenameRequest(p.getUniqueId());
				if (schemPath != null && !oldFiles.isEmpty()) {
					Rename.moveFile(p, args[2], oldFiles, newFiles, schemPath.resolve(args[3]));
				} else {
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
								  ChatColor.GOLD + args[2] + ChatColor.RED + " does not exist.");
				}
			} else if (args[4].equalsIgnoreCase("deny")) {
				WorldEditModeRequestUtils.removeRenameRequest(p.getUniqueId());
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.GOLD + args[2] + ChatColor.RED + " was not renamed.");
			}
		}
	}

	private void moveFile(final @NotNull Player p, final String fileName, final @NotNull List<File> oldFiles, final @Nullable List<File> newFiles, final @NotNull Path destPath) {
		try {
			if (newFiles != null) {
				for (final @NotNull File file : newFiles) {
					Files.delete(file.toPath());
				}
			}

			@Nullable String parentName = null;
			for (final @NotNull File file : oldFiles) {
				if (!Objects.containsIgnoreCase(ConfigUtils.getStringList("File Extensions"), BaseFileUtils.getExtension(destPath))) {
					FileUtils.moveFile(file, new File(destPath + BaseFileUtils.getExtension(file)));
				} else {
					FileUtils.moveFile(file, destPath.toFile());
				}

				parentName = Objects.notNull(file.getAbsoluteFile().getParentFile().listFiles()).length > 0
							 || ConfigUtils.getBoolean("Delete empty Folders") ? null : InternalFileUtils.deleteEmptyParent(file);

				if (file.getName().equals(parentName)) {
					parentName = null;
				}
			}

			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GOLD + fileName + ChatColor.RED + " was renamed successfully.");

			if (parentName != null) {
				p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
							  ChatColor.RED + "Folder " + ChatColor.GREEN + parentName + ChatColor.RED + " was deleted successfully due to being empty.");
			}
		} catch (IOException e) {
			p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + SchemManager.getInstance().getName() + ChatColor.DARK_GRAY + "] " +
						  ChatColor.GOLD + fileName + ChatColor.RED + " could not be renamed, for further information please see [console].");
			e.printStackTrace();
		}
	}

	private void renameUsage(final @NotNull Player p, final String slash, final String schemAlias) {
		MessageUtils.sendSuggestMessage(ChatColor.RED + "Usage: ",
										ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " rename "
										+ ChatColor.YELLOW + "<"
										+ ChatColor.GOLD + "filename"
										+ ChatColor.YELLOW + "> <"
										+ ChatColor.GOLD + "newname"
										+ ChatColor.YELLOW + ">",
										ChatColor.RED + "e.g. "
										+ ChatColor.GRAY + slash + schemAlias
										+ ChatColor.AQUA + " rename "
										+ ChatColor.GOLD + "example newname",
										slash + schemAlias + " rename ", p);
	}
}